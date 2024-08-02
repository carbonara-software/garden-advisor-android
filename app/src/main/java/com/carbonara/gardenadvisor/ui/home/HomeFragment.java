package com.carbonara.gardenadvisor.ui.home;

import static com.carbonara.gardenadvisor.util.AppUtil.getCurrentLocationName;
import static com.carbonara.gardenadvisor.util.LogUtil.logd;
import static com.carbonara.gardenadvisor.util.LogUtil.loge;
import static com.carbonara.gardenadvisor.util.ui.IconChooser.getIcon;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.carbonara.gardenadvisor.R;
import com.carbonara.gardenadvisor.ai.dto.GeminiGardeningSugg;
import com.carbonara.gardenadvisor.ai.dto.GeminiWeather;
import com.carbonara.gardenadvisor.ai.task.impl.GeminiHomeSuggestionTask;
import com.carbonara.gardenadvisor.ai.task.impl.GeminiWeatherTask;
import com.carbonara.gardenadvisor.databinding.FragmentHomeBinding;
import com.carbonara.gardenadvisor.ui.home.adapter.GardeningItemAdapter;
import com.carbonara.gardenadvisor.ui.home.adapter.WeatherAdapter;
import com.carbonara.gardenadvisor.util.ui.BaseFragment;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import java.util.Locale;

public class HomeFragment extends BaseFragment {
  private static final String TAG = "HomeFrag";
  FragmentHomeBinding binding;

  private FusedLocationProviderClient fusedLocationClient;
  private boolean hasFinishWeather;
  private boolean hasFinishSuggestions;
  private Address current;

  @SuppressLint("MissingPermission")
  private final ActivityResultLauncher<String[]> requestPermissionLauncher =
      registerForActivityResult(
          new ActivityResultContracts.RequestMultiplePermissions(),
          isGranted -> {
            if ((isGranted != null)
                && (Boolean.TRUE.equals(
                        isGranted.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false))
                    || Boolean.TRUE.equals(
                        isGranted.getOrDefault(
                            Manifest.permission.ACCESS_COARSE_LOCATION, false)))) {
              fusedLocationClient =
                  LocationServices.getFusedLocationProviderClient(requireContext());
              fusedLocationClient.getLastLocation().addOnSuccessListener(this::locationRetrieved);
            } else {
              // TODO: Permission denied, handle accordingly
            }
          });

  private Disposable weatherDisposable;
  private Disposable gardenDisposable;

  @Override
  public View onCreateView(
      @NonNull LayoutInflater inflater,
      @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    binding = FragmentHomeBinding.inflate(inflater, container, false);
    return binding.getRoot();
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    showBottomBar();

    if (checkAndRequestLocationPermission()) {
      fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext());
      fusedLocationClient.getLastLocation().addOnSuccessListener(this::locationRetrieved);
    } else {
      logd("No permission found");
    }
  }

  private void locationRetrieved(Location location) {
    if (location != null) {
      current =
          getCurrentLocationName(requireContext(), location.getLatitude(), location.getLongitude());
      if (current == null) {
        displayErrorDialog(getString(R.string.error_location));
        return;
      }
      if (gardenDisposable != null && !gardenDisposable.isDisposed()) gardenDisposable.dispose();
      if (weatherDisposable != null && !weatherDisposable.isDisposed()) weatherDisposable.dispose();
      displayLoadingDialog();

      weatherDisposable =
          Single.create(
                  new GeminiWeatherTask(
                      (float) current.getLatitude(),
                      (float) current.getLongitude(),
                      current.getLocality()))
              .subscribeOn(Schedulers.io())
              .observeOn(AndroidSchedulers.mainThread())
              .subscribe(this::successWeather, this::failWeather);
      gardenDisposable =
          Single.create(
                  new GeminiHomeSuggestionTask(
                      (float) current.getLatitude(),
                      (float) current.getLongitude(),
                      current.getLocality()))
              .subscribeOn(Schedulers.io())
              .observeOn(AndroidSchedulers.mainThread())
              .subscribe(this::successGarden, this::failGarden);
      hasFinishSuggestions = false;
      hasFinishWeather = false;
    } else {
      displayErrorDialog(getString(R.string.error_location));
    }
  }

  private void failGarden(Throwable throwable) {
    if (gardenDisposable != null && !gardenDisposable.isDisposed()) gardenDisposable.dispose();
    closeDialog();
    loge(throwable);
    displayErrorDialog(getString(R.string.error_gemini));
  }

  private void successGarden(GeminiGardeningSugg geminiGardeningSugg) {
    hasFinishSuggestions = true;
    if (hasFinishWeather) closeDialog();
    LinearLayoutManager llmFruit =
        new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
    LinearLayoutManager llmFlo =
        new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
    LinearLayoutManager llmVeg =
        new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
    GardeningItemAdapter adpFruit = new GardeningItemAdapter(geminiGardeningSugg.getFruits());
    GardeningItemAdapter adpVeg = new GardeningItemAdapter(geminiGardeningSugg.getVegetables());
    GardeningItemAdapter adpFlo = new GardeningItemAdapter(geminiGardeningSugg.getFlowers());
    binding.listFruit.setAdapter(adpFruit);
    binding.listFruit.setLayoutManager(llmFruit);
    binding.listVegetables.setAdapter(adpVeg);
    binding.listVegetables.setLayoutManager(llmVeg);
    binding.listFlowers.setAdapter(adpFlo);
    binding.listFlowers.setLayoutManager(llmFlo);
  }

  private void failWeather(Throwable throwable) {
    if (weatherDisposable != null && !weatherDisposable.isDisposed()) weatherDisposable.dispose();
    closeDialog();
    loge(throwable);
    displayErrorDialog(getString(R.string.error_gemini));
  }

  private void successWeather(GeminiWeather s) {
    hasFinishWeather = true;
    if (hasFinishSuggestions) closeDialog();
    if (getContext() == null) return;
    WeatherAdapter adp = new WeatherAdapter(s.getWeather().getForecast());
    LinearLayoutManager llm =
        new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
    binding.listWeather.setAdapter(adp);
    binding.listWeather.setLayoutManager(llm);
    binding.city.setText(
        current.getLocality() != null ? current.getLocality() : current.getCountryName());
    binding.iconWeather.setImageResource(getIcon(s.getWeather().getTodayForecast().getIcon()));
    binding.cityTempMaxValue.setText(
        String.format(
            Locale.getDefault(), "%.1f°", s.getWeather().getTodayForecast().getMaxTemp()));
    binding.cityTempMinValue.setText(
        String.format(
            Locale.getDefault(), "%.1f°", s.getWeather().getTodayForecast().getMinTemp()));
    binding.cityTemp.setText(
        String.format(
            Locale.getDefault(),
            "%.1f° | %s",
            s.getWeather().getTodayForecast().getCurrentTemp(),
            s.getWeather().getTodayForecast().getConditions()));
  }

  private boolean checkAndRequestLocationPermission() {
    if (ContextCompat.checkSelfPermission(
            requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
        == PackageManager.PERMISSION_GRANTED) {
      return true;
    } else if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
      // Explain why the permission is needed
      displayErrorDialog(getString(R.string.error_rationale));
    } else {
      // Request the permission
      requestPermissionLauncher.launch(
          new String[] {
            Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION
          });
    }
    return false;
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    if (weatherDisposable != null && !weatherDisposable.isDisposed()) weatherDisposable.dispose();
    if (gardenDisposable != null && !gardenDisposable.isDisposed()) gardenDisposable.dispose();
  }
}
