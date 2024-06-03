package com.carbonara.gardenadvisor.ui.home;

import static com.carbonara.gardenadvisor.util.AppUtil.getCurrentLocationName;
import static com.carbonara.gardenadvisor.util.LogUtil.loge;

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
import com.carbonara.gardenadvisor.ai.GeminiWrapper;
import com.carbonara.gardenadvisor.ai.dto.GeminiGardeningSugg;
import com.carbonara.gardenadvisor.ai.dto.GeminiWeather;
import com.carbonara.gardenadvisor.databinding.FragmentHomeBinding;
import com.carbonara.gardenadvisor.ui.home.adapter.GardeningItemAdapter;
import com.carbonara.gardenadvisor.ui.home.adapter.WeatherAdapter;
import com.carbonara.gardenadvisor.util.ui.BaseFragment;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

public class HomeFragment extends BaseFragment {
  private static final String TAG = "HomeFrag";
  FragmentHomeBinding binding;

  private FusedLocationProviderClient fusedLocationClient;

  private boolean hasFinishWeather;
  private boolean hasFinishSuggestions;
  private Address current;

  // messo solo perche android studio non e l'ide piu smart al mondo...
  // l'if verifica appunto se sono stati acconsentiti i permessi
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
    // Controllo se ha i permessi
    if (checkAndRequestLocationPermission()) {
      // Tutt appo possiamo procedere
      fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext());
      fusedLocationClient.getLastLocation().addOnSuccessListener(this::locationRetrieved);

    } else {
      // qui potremmo trovarci in due situazioni:
      //  - non avevamo i permessi ma essendo la prima volta e comparsa la dialog e l'utente puo o
      // non puo accettare i permessi ma lo gestiamo sopra
      //  - L'utente ha rifiutato i permessi richiesti...
      // quindi
      // TODO: Fix this situation...
      loge("No permission found");
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
      displayLoadingDialog();
      GeminiWrapper wrapper =
          GeminiWrapper.getInstance(
              current.getLocality(), (float) current.getLatitude(), (float) current.getLongitude());
      wrapper.getWeather(this::successWeather, this::successSuggestions, this::fail);
      hasFinishSuggestions = false;
      hasFinishWeather = false;
    } else {
      displayErrorDialog(getString(R.string.error_location));
    }
  }

  private void successSuggestions(GeminiGardeningSugg s) {
    requireActivity()
        .runOnUiThread(
            () -> {
              hasFinishSuggestions = true;
              if (hasFinishWeather) closeDialog();
              // TODO: populate UI for weather
              LinearLayoutManager llmFruit =
                  new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
              LinearLayoutManager llmFlo =
                  new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
              LinearLayoutManager llmVeg =
                  new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
              GardeningItemAdapter adpFruit = new GardeningItemAdapter(s.getFruits());
              GardeningItemAdapter adpVeg = new GardeningItemAdapter(s.getVegetables());
              GardeningItemAdapter adpFlo = new GardeningItemAdapter(s.getFlowers());
              binding.listFruit.setAdapter(adpFruit);
              binding.listFruit.setLayoutManager(llmFruit);

              binding.listVegetables.setAdapter(adpVeg);
              binding.listVegetables.setLayoutManager(llmVeg);

              binding.listFlowers.setAdapter(adpFlo);
              binding.listFlowers.setLayoutManager(llmFlo);
              loge("Eccolo: " + s);
            });
  }

  private void fail(Throwable throwable) {
    closeDialog();
    loge(throwable);
    displayErrorDialog(getString(R.string.error_gemini));
  }

  private void successWeather(GeminiWeather s) {
    hasFinishWeather = true;
    if (hasFinishSuggestions) closeDialog();
    // TODO: populate UI for gardening suggestions
    requireActivity()
        .runOnUiThread(
            () -> {
              WeatherAdapter adp = new WeatherAdapter(s.getWeather().getForecast());
              LinearLayoutManager llm =
                  new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
              binding.listWeather.setAdapter(adp);
              binding.listWeather.setLayoutManager(llm);
              binding.city.setText(current.getLocality()!=null?current.getLocality():current.getCountryName());
              // TODO: update other data about Weather (City name and current temperature)
            });
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
}
