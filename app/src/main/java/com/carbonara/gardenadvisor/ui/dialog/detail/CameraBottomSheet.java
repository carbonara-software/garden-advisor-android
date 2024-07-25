package com.carbonara.gardenadvisor.ui.dialog.detail;

import static com.carbonara.gardenadvisor.util.LogUtil.loge;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.carbonara.gardenadvisor.ai.GeminiCameraSuggestionWrapper;
import com.carbonara.gardenadvisor.ai.dto.GeminiCameraSuggestion;
import com.carbonara.gardenadvisor.databinding.BottomsheetCameraBinding;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.otaliastudios.cameraview.CameraListener;
import com.otaliastudios.cameraview.CameraView;
import com.otaliastudios.cameraview.PictureResult;
import com.otaliastudios.cameraview.controls.Audio;
import com.otaliastudios.cameraview.controls.Mode;
import java.util.Arrays;
import java.util.List;

public class CameraBottomSheet extends BottomSheetDialogFragment {

  private BottomsheetCameraBinding binding;

  private GeminiCameraSuggestionWrapper cameraSuggestionWrapper;

  private List<View> layouts;

  @Nullable
  @Override
  public View onCreateView(
      @NonNull LayoutInflater inflater,
      @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    binding = BottomsheetCameraBinding.inflate(inflater, container, false);
    cameraSuggestionWrapper = new GeminiCameraSuggestionWrapper();

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

    camera.addCameraListener(
        new CameraListener() {
          @Override
          public void onPictureTaken(PictureResult result) {
            result.toBitmap(1500, 1500, bitmap -> pictureTaken(bitmap));
          }
        });

    ImageView takePictureButton = binding.takePictureButton;
    takePictureButton.setOnClickListener(v -> camera.takePicture());

    binding.close.setOnClickListener(v -> dismiss());
    binding.errorRetryButton.setOnClickListener(
        v -> {
          hideAllLayouts();
          binding.cameraLayout.setVisibility(View.VISIBLE);
        });
  }

  private void pictureTaken(Bitmap bitmap) {
    cameraSuggestionWrapper.getGeminiResult(
        bitmap, this::cameraSuggestionSuccess, this::cameraSuggestionFailure);

    hideAllLayouts();
    binding.loadingLayout.setVisibility(View.VISIBLE);
  }

  public void cameraSuggestionSuccess(GeminiCameraSuggestion cameraSuggestion) {
    requireActivity()
        .runOnUiThread(
            () -> {
              loge(cameraSuggestion.toString());
              hideAllLayouts();
              binding.resultLayout.setVisibility(View.VISIBLE);
            });
  }

  public void cameraSuggestionFailure(Throwable t) {
    requireActivity()
        .runOnUiThread(
            () -> {
              loge(t);
              hideAllLayouts();
              binding.errorLayout.setVisibility(View.VISIBLE);
            });
  }

  private void hideAllLayouts() {
    layouts.forEach(f -> f.setVisibility(View.GONE));
  }
}
