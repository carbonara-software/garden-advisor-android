package com.carbonara.gardenadvisor;

import static com.carbonara.gardenadvisor.util.LogUtil.logd;
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
import com.carbonara.gardenadvisor.util.AppCache;
import com.carbonara.gardenadvisor.util.GAMenuItems;
import java.io.IOException;
import lombok.Getter;
import lombok.Setter;

public class MainActivity extends AppCompatActivity {

  ActivityMainBinding binding;
  GAMenuItems selected = GAMenuItems.HOME;
  @Getter
  @Setter
  private boolean isSingleGardenView = false;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    binding = ActivityMainBinding.inflate(getLayoutInflater());
    setContentView(binding.getRoot());
    binding.topBar.cameraButton.setOnClickListener(
        new OnClickListener() {
          @Override
          public void onClick(View v) {
            CameraBottomSheet bottomSheet = new CameraBottomSheet(isSingleGardenView);
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
      logd("Home page Pressed");
      navController.navigate(HomeFragmentDirections.actionGlobalHome());
    } else if (menuItem.getItemId() == R.id.navigation_gardens && selected != GAMenuItems.GARDENS) {
      selected = GAMenuItems.GARDENS;
      navController.navigate(HomeFragmentDirections.actionGlobalGardensFragment());
    } else if (menuItem.getItemId() == R.id.navigation_settings
        && selected != GAMenuItems.SETTINGS) {
      logd("Settings page not implemented yet");
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
      AppCache.getInstance().persistWeather(getApplicationContext());
    } catch (IOException e) {
      loge("error persistingWeather: ", e);
    }
    try {
      AppCache.getInstance().persistHome(getApplicationContext());
    } catch (IOException e) {
      loge("error persistingHome: ", e);
    }
  }

  @Override
  protected void onResume() {
    super.onResume();
    try {
      AppCache.getInstance().restoreHome(getApplicationContext());
    } catch (Exception e) {
      loge("error restoringHome: ", e);
    }
    try {
      AppCache.getInstance().restoreWeather(getApplicationContext());
    } catch (Exception e) {
      loge("error restoringWeather: ", e);
    }
  }
}
