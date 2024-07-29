package com.carbonara.gardenadvisor.ui.garden;

import static android.view.View.GONE;
import static com.carbonara.gardenadvisor.util.LogUtil.loge;
import static com.carbonara.gardenadvisor.util.ui.IconChooser.getIcon;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.carbonara.gardenadvisor.ai.GardenGeminiWrapper;
import com.carbonara.gardenadvisor.ai.dto.GardeningItem;
import com.carbonara.gardenadvisor.ai.dto.GeminiGardeningSugg;
import com.carbonara.gardenadvisor.ai.dto.GeminiWeather;
import com.carbonara.gardenadvisor.databinding.FragmentGardenBinding;
import com.carbonara.gardenadvisor.persistence.entity.GardenWithPlants;
import com.carbonara.gardenadvisor.ui.home.adapter.GardeningItemAdapter;
import com.carbonara.gardenadvisor.ui.home.adapter.WeatherAdapter;
import com.carbonara.gardenadvisor.util.ui.BaseFragment;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class GardenFragment extends BaseFragment {

  FragmentGardenBinding binding;
  GardenWithPlants gardenWithPlants;
  GardenGeminiWrapper gardenGeminiWrapper;
  private boolean hasFinishWeather;
  private boolean hasFinishSuggestions;

  private void fail(Throwable throwable) {
    displayErrorDialog("Error retrieving weather and suggestions");
  }

  private void successSuggestions(GeminiGardeningSugg geminiGardeningSugg) {
    loge(geminiGardeningSugg.toString());
    hasFinishSuggestions = true;
    if (hasFinishWeather) closeDialog();
    List<GardeningItem> items = new ArrayList<>();
    items.addAll(geminiGardeningSugg.getFlowers());
    items.addAll(geminiGardeningSugg.getFruits());
    items.addAll(geminiGardeningSugg.getVegetables());
    if (getActivity() != null) {
      getActivity()
          .runOnUiThread(
              new Runnable() {
                @Override
                public void run() {
                  if (items.isEmpty()) {
                    binding.emptyListGardens.setVisibility(View.VISIBLE);
                    binding.listaItems.setVisibility(GONE);
                  } else {
                    binding.emptyListGardens.setVisibility(GONE);
                    binding.listaItems.setVisibility(View.VISIBLE);
                    GardeningItemAdapter adp = new GardeningItemAdapter(items);
                    GridLayoutManager llm = new GridLayoutManager(requireContext(), 2);
                    binding.listaItems.setAdapter(adp);
                    binding.listaItems.setLayoutManager(llm);
                  }
                }
              });
    }
  }

  private void successWeather(GeminiWeather s) {
    //    closeDialog();
    hasFinishWeather = true;
    if (hasFinishSuggestions) closeDialog();
    requireActivity()
        .runOnUiThread(
            () -> {
              WeatherAdapter adp = new WeatherAdapter(s.getWeather().getForecast());
              LinearLayoutManager llm =
                  new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
              binding.listWeather.setAdapter(adp);
              binding.listWeather.setLayoutManager(llm);
              binding.city.setText(gardenWithPlants.getGarden().getLocation().getLocationName());
              binding.iconWeather.setImageResource(
                  getIcon(s.getWeather().getTodayForecast().getIcon()));
              binding.cityTempMaxValue.setText(
                  String.format(
                      Locale.getDefault(),
                      "%.1f°",
                      s.getWeather().getTodayForecast().getMaxTemp()));
              binding.cityTempMinValue.setText(
                  String.format(
                      Locale.getDefault(),
                      "%.1f°",
                      s.getWeather().getTodayForecast().getMinTemp()));
              binding.cityTemp.setText(
                  String.format(
                      Locale.getDefault(),
                      "%.1f° | %s",
                      s.getWeather().getTodayForecast().getCurrentTemp(),
                      s.getWeather().getTodayForecast().getConditions()));
            });
  }

  @Override
  public View onCreateView(
      @NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    binding = FragmentGardenBinding.inflate(inflater, container, false);
    return binding.getRoot();
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    hideBottomBar();
    if (getArguments() != null) {
      gardenWithPlants = GardenFragmentArgs.fromBundle(getArguments()).getGarden();
      gardenGeminiWrapper = new GardenGeminiWrapper(gardenWithPlants);
      binding.city.setText(gardenWithPlants.getGarden().getLocation().getLocationName());

      gardenGeminiWrapper.getGeminiResult(
          this::successWeather, this::successSuggestions, this::fail);
      displayLoadingDialog();
      binding.addPlant.setOnClickListener(this::addPlantPressed);
    } else {
      displayErrorDialog("Error retrieving garden with plants");
    }
  }

  private void addPlantPressed(View view) {}
}
