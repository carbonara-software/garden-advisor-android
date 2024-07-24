package com.carbonara.gardenadvisor;

import static com.carbonara.gardenadvisor.util.LogUtil.loge;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import com.carbonara.gardenadvisor.databinding.ActivityMainBinding;
import com.carbonara.gardenadvisor.ui.home.HomeFragmentDirections;
import com.carbonara.gardenadvisor.util.GAMenuItems;

public class MainActivity extends AppCompatActivity {

  ActivityMainBinding binding;
  GAMenuItems selected = GAMenuItems.HOME;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    binding = ActivityMainBinding.inflate(getLayoutInflater());
    setContentView(binding.getRoot());
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
}
