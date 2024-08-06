package com.carbonara.gardenadvisor.ui.dialog.detail;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.carbonara.gardenadvisor.ai.dto.GardeningItem;
import com.carbonara.gardenadvisor.ai.dto.GeminiCameraSuggestion;
import com.carbonara.gardenadvisor.ai.task.impl.GeminiCameraTask;
import com.carbonara.gardenadvisor.ai.task.impl.GeminiGardenCameraTask;
import com.carbonara.gardenadvisor.databinding.BottomsheetCameraBinding;
import com.carbonara.gardenadvisor.ui.dialog.detail.adapter.SuggestionsAdapter;
import com.carbonara.gardenadvisor.ui.dialog.detail.callback.GardenCameraResult;
import com.carbonara.gardenadvisor.util.AppCache;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.otaliastudios.cameraview.CameraListener;
import com.otaliastudios.cameraview.CameraView;
import com.otaliastudios.cameraview.PictureResult;
import com.otaliastudios.cameraview.controls.Audio;
import com.otaliastudios.cameraview.controls.Mode;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import java.util.Arrays;
import java.util.List;

public class CameraBottomSheet extends BottomSheetDialogFragment {

  private BottomsheetCameraBinding binding;
  private List<View> layouts;
  private Disposable cameraDisposable;
  private float lat, lon;
  private String locationName = null;
  private long gardenId;
  private GardenCameraResult callback;

  public CameraBottomSheet() {}

  public CameraBottomSheet(
      float lat, float lon, String locationName, long gardenId, GardenCameraResult callback) {
    this.lat = lat;
    this.lon = lon;
    this.locationName = locationName;
    this.gardenId = gardenId;
    this.callback = callback;
  }

  @Nullable
  @Override
  public View onCreateView(
      @NonNull LayoutInflater inflater,
      @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    binding = BottomsheetCameraBinding.inflate(inflater, container, false);

    layouts =
        Arrays.asList(
            binding.cameraLayout, binding.loadingLayout, binding.resultLayout, binding.errorLayout);

    return binding.getRoot();
  }

  public void onViewCreated(View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    CameraView camera = binding.camera;
    camera.setMode(Mode.PICTURE);
    camera.setAudio(Audio.OFF);
    camera.setLifecycleOwner(getViewLifecycleOwner());

    camera.setSnapshotMaxHeight(600);
    camera.setSnapshotMaxWidth(600);

    camera.addCameraListener(
        new CameraListener() {
          @Override
          public void onPictureTaken(@NonNull PictureResult result) {
            result.toBitmap(bitmap -> pictureTaken(bitmap));
          }
        });

    ImageView takePictureButton = binding.takePictureButton;
    takePictureButton.setOnClickListener(v -> camera.takePictureSnapshot());

    binding.close.setOnClickListener(v -> dismiss());
    binding.errorRetryButton.setOnClickListener(v -> startOver());

    startOver();
  }

  private void pictureTaken(Bitmap bitmap) {
    binding.pictureView.setImageBitmap(bitmap);
    if (locationName == null) {
      cameraDisposable =
          Single.create(new GeminiCameraTask(bitmap, getActivity().getApplicationContext()))
              .subscribeOn(Schedulers.io())
              .observeOn(AndroidSchedulers.mainThread())
              .subscribe(this::cameraSuggestionSuccess, this::cameraSuggestionFailure);
    } else {
      cameraDisposable =
          Single.create(new GeminiGardenCameraTask(lat, lon, locationName, gardenId, bitmap))
              .subscribeOn(Schedulers.io())
              .observeOn(AndroidSchedulers.mainThread())
              .subscribe(this::cameraGardeningItemSuccess, this::cameraGardeningItemFailure);
    }
    hideAllLayouts();
    binding.loadingLayout.setVisibility(View.VISIBLE);
  }

  private void cameraGardeningItemFailure(Throwable throwable) {
    callback.onCameraResult(null);
  }

  private void cameraGardeningItemSuccess(GardeningItem gardeningItem) {
    callback.onCameraResult(gardeningItem);
  }

  public void cameraSuggestionSuccess(GeminiCameraSuggestion cameraSuggestion) {
    updatePlantResult(cameraSuggestion);

    saveSuggestionIntoCache(cameraSuggestion);
  }

  private void updatePlantResult(GeminiCameraSuggestion cameraSuggestion) {
    binding.plantName.setText(cameraSuggestion.getName());
    binding.plantScientificName.setText(cameraSuggestion.getScientificName());

    binding.statusEmoji.setText(cameraSuggestion.getStatusEmoji());
    binding.statusText.setText(cameraSuggestion.getPrintableStatus());

    if (cameraSuggestion.getSuggestions() != null && !cameraSuggestion.getSuggestions().isEmpty()) {
      binding.rvSuggestions.setVisibility(View.VISIBLE);
      SuggestionsAdapter adapterSuggestions =
          new SuggestionsAdapter(cameraSuggestion.getSuggestions());
      LinearLayoutManager llm = new LinearLayoutManager(requireContext());
      binding.rvSuggestions.setAdapter(adapterSuggestions);
      binding.rvSuggestions.setLayoutManager(llm);
    } else {
      binding.rvSuggestions.setVisibility(View.GONE);
    }

    binding.successRetryButton.setOnClickListener(v -> startOver());

    if (canBeDisposed()) {
      cameraDisposable.dispose();
    }

    hideAllLayouts();
    binding.resultLayout.setVisibility(View.VISIBLE);
  }

  public void cameraSuggestionFailure(Throwable t) {
    if (canBeDisposed()) {
      cameraDisposable.dispose();
    }

    hideAllLayouts();
    binding.errorLayout.setVisibility(View.VISIBLE);
  }

  private void saveSuggestionIntoCache(GeminiCameraSuggestion suggestion) {
    AppCache.getInstance().addCameraSuggestion(suggestion);
  }

  private void startOver() {
    hideAllLayouts();
    binding.cameraLayout.setVisibility(View.VISIBLE);
  }

  private void hideAllLayouts() {
    layouts.forEach(f -> f.setVisibility(View.GONE));
  }

  private boolean canBeDisposed() {
    return cameraDisposable != null && !cameraDisposable.isDisposed();
  }
}
