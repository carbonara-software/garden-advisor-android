package com.carbonara.gardenadvisor.ui.garden;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import com.carbonara.gardenadvisor.ai.GardenGeminiWrapper;
import com.carbonara.gardenadvisor.ai.dto.GeminiGardeningSugg;
import com.carbonara.gardenadvisor.ai.dto.GeminiWeather;
import com.carbonara.gardenadvisor.databinding.FragmentGardenBinding;
import com.carbonara.gardenadvisor.persistence.entity.GardenWithPlants;

public class GardenFragment extends Fragment {

  FragmentGardenBinding binding;
  GardenWithPlants gardenWithPlants;
  GardenGeminiWrapper gardenGeminiWrapper;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    gardenGeminiWrapper = new GardenGeminiWrapper(gardenWithPlants);
    binding.city.setText(gardenWithPlants.getGarden().getLocation().getLocationName());
    gardenGeminiWrapper.getGeminiResult(this::successWeather, this::successSuggestions, this::fail);
  }

  private void fail(Throwable throwable) {}

  private void successSuggestions(GeminiGardeningSugg geminiGardeningSugg) {}

  private void successWeather(GeminiWeather geminiWeather) {}

  @Override
  public View onCreateView(
      LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    binding = FragmentGardenBinding.inflate(inflater, container, false);
    return binding.getRoot();
  }
}
