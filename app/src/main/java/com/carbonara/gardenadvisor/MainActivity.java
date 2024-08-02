package com.carbonara.gardenadvisor;

import static com.carbonara.gardenadvisor.util.AppUtil.persistHome;
import static com.carbonara.gardenadvisor.util.AppUtil.persistWeather;
import static com.carbonara.gardenadvisor.util.AppUtil.restoreHome;
import static com.carbonara.gardenadvisor.util.AppUtil.restoreWeather;
import static com.carbonara.gardenadvisor.util.LogUtil.loge;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import com.carbonara.gardenadvisor.databinding.ActivityMainBinding;
import com.carbonara.gardenadvisor.ui.dialog.detail.CameraBottomSheet;
import com.carbonara.gardenadvisor.ui.home.HomeFragmentDirections;
import com.carbonara.gardenadvisor.util.GAMenuItems;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

  ActivityMainBinding binding;
  GAMenuItems selected = GAMenuItems.HOME;

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
    binding.bottomBar.bottomNavigationView.setOnItemSelectedListener(this::onItemSelected);
  }

  private boolean onItemSelected(MenuItem menuItem) {
    NavController navController = Navigation.findNavController(binding.navHostFragment);
    if (menuItem.getItemId() == R.id.navigation_home && selected != GAMenuItems.HOME) {
      selected = GAMenuItems.HOME;
      loge("Home page Pressed");
      navController.navigate(HomeFragmentDirections.actionGlobalHome());
    } else if (menuItem.getItemId() == R.id.navigation_gardens && selected != GAMenuItems.GARDENS) {
      selected = GAMenuItems.GARDENS;
      navController.navigate(HomeFragmentDirections.actionGlobalGardensFragment());
    } else if (menuItem.getItemId() == R.id.navigation_settings
        && selected != GAMenuItems.SETTINGS) {
      loge("Settings page not implemented yet");
      return false;
    } else {
      return false;
    }
    return true;
  }

  public void showBottomBar() {
    binding.bottomBar.bottomNavigationView.setVisibility(View.VISIBLE);
  }

  public void hideBottomBar() {
    binding.bottomBar.bottomNavigationView.setVisibility(View.GONE);
  }

  @Override
  protected void onPause() {
    super.onPause();
    try {
      persistWeather(getApplicationContext());
    } catch (IOException e) {
      // TODO How to handle this?
    }
    try {
      persistHome(getApplicationContext());
    } catch (IOException e) {
      // TODO How to handle this?
    }
  }

  @Override
  protected void onResume() {
    super.onResume();
    try {
      restoreHome(getApplicationContext());
    } catch (IOException e) {
      // TODO How to handle this?
    }
    try {
      restoreWeather(getApplicationContext());
    } catch (IOException e) {
      // TODO How to handle this?
    }


  }
}
