package com.carbonara.gardenadvisor;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import com.carbonara.gardenadvisor.databinding.ActivityMainBinding;
import com.carbonara.gardenadvisor.ui.dialog.detail.CameraBottomSheet;

public class MainActivity extends AppCompatActivity {

  ActivityMainBinding binding;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    binding = ActivityMainBinding.inflate(getLayoutInflater());
    setContentView(binding.getRoot());

    binding.topBar.cameraButton.setOnClickListener(
        new OnClickListener() {
          @Override
          public void onClick(View v) {
            CameraBottomSheet bottomSheet = new CameraBottomSheet();
            bottomSheet.show(
                ((FragmentActivity) binding.getRoot().getContext()).getSupportFragmentManager(),
                "CameraBottomSheet");
          }
        });
  }
}
