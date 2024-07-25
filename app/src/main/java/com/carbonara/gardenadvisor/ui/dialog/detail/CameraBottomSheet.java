package com.carbonara.gardenadvisor.ui.dialog.detail;

import static com.carbonara.gardenadvisor.util.LogUtil.loge;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.carbonara.gardenadvisor.ai.GeminiCameraSuggestionWrapper;
import com.carbonara.gardenadvisor.ai.dto.GeminiCameraSuggestion;
import com.carbonara.gardenadvisor.databinding.BottomsheetCameraBinding;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.otaliastudios.cameraview.CameraListener;
import com.otaliastudios.cameraview.CameraView;
import com.otaliastudios.cameraview.PictureResult;
import com.otaliastudios.cameraview.controls.Mode;

public class CameraBottomSheet extends BottomSheetDialogFragment {

  private BottomsheetCameraBinding binding;

  private GeminiCameraSuggestionWrapper cameraSuggestionWrapper;

  @Nullable
  @Override
  public View onCreateView(
      @NonNull LayoutInflater inflater,
      @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    binding = BottomsheetCameraBinding.inflate(inflater, container, false);
    cameraSuggestionWrapper = new GeminiCameraSuggestionWrapper();
    return binding.getRoot();
  }

  public void onViewCreated(View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    CameraView camera = binding.camera;
    camera.setLifecycleOwner(getViewLifecycleOwner());
    camera.setMode(Mode.PICTURE);
    camera.addCameraListener(
        new CameraListener() {
          @Override
          public void onPictureTaken(PictureResult result) {
            result.toBitmap(1500, 1500, bitmap -> pictureTaken(bitmap));
          }
        });
  }

  private void pictureTaken(Bitmap bitmap) {
    cameraSuggestionWrapper.getGeminiResult(
        bitmap, this::cameraSuggestionSuccess, this::cameraSuggestionFailure);
  }

  public void cameraSuggestionSuccess(GeminiCameraSuggestion cameraSuggestion) {
    requireActivity()
        .runOnUiThread(
            () -> {
              loge(cameraSuggestion.toString());
            });
  }

  public void cameraSuggestionFailure(Throwable t) {
    loge(t);
  }
}
