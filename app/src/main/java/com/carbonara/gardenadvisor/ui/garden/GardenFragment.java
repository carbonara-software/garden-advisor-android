package com.carbonara.gardenadvisor.ui.garden;

import static android.view.View.GONE;
import static com.carbonara.gardenadvisor.util.LogUtil.logd;
import static com.carbonara.gardenadvisor.util.LogUtil.loge;
import static com.carbonara.gardenadvisor.util.ui.IconChooser.getIcon;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.carbonara.gardenadvisor.ai.dto.GardeningItem;
import com.carbonara.gardenadvisor.ai.dto.GeminiGardeningSugg;
import com.carbonara.gardenadvisor.ai.dto.GeminiWeather;
import com.carbonara.gardenadvisor.ai.task.impl.GeminiGardenTask;
import com.carbonara.gardenadvisor.ai.task.impl.GeminiWeatherTask;
import com.carbonara.gardenadvisor.databinding.FragmentGardenBinding;
import com.carbonara.gardenadvisor.persistence.entity.Garden;
import com.carbonara.gardenadvisor.persistence.entity.Plant;
import com.carbonara.gardenadvisor.ui.garden.adapter.GardenPlantItemAdapter;
import com.carbonara.gardenadvisor.ui.home.adapter.WeatherAdapter;
import com.carbonara.gardenadvisor.util.task.CreatePlantsInGardenEmitter;
import com.carbonara.gardenadvisor.util.ui.BaseFragment;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class GardenFragment extends BaseFragment {

  FragmentGardenBinding binding;
  GardenFragmentArgs args;
  private Disposable fragmentDisposable;
  private Disposable gardenDisposable;
  private Disposable weatherDisposable;

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
      args = GardenFragmentArgs.fromBundle(getArguments());
      init();
      updateWeather();
      if (args.getPlants() != null && args.getPlants().length > 0) {
        // opens from AddPlantsFragment with a valid list of plants
        Garden g = args.getGarden().getGarden();
        if (args.getGarden().getPlants() != null && !args.getGarden().getPlants().isEmpty()) {
          // check for possible duplicates
          List<String> savedPlants =
              args.getGarden().getPlants().stream()
                  .map(Plant::getPlantName)
                  .map(String::toLowerCase)
                  .collect(Collectors.toList());
          List<String> toSent =
              Arrays.stream(args.getPlants())
                  .map(String::toLowerCase)
                  .filter(lowerCase -> !savedPlants.contains(lowerCase))
                  .collect(Collectors.toList());
          savedPlants.addAll(toSent);

          if (gardenDisposable != null && !gardenDisposable.isDisposed())
            gardenDisposable.dispose();

          gardenDisposable =
              Single.create(
                      new GeminiGardenTask(
                          g.getLocation().getLat(),
                          g.getLocation().getLon(),
                          g.getLocation().getLocationName(),
                          new HashSet<>(savedPlants)))
                  .subscribeOn(Schedulers.io())
                  .observeOn(AndroidSchedulers.mainThread())
                  .subscribe(this::successGarden, this::failGarden);
          displayLoadingDialog();
        } else {
          gardenDisposable =
              Single.create(
                      new GeminiGardenTask(
                          g.getLocation().getLat(),
                          g.getLocation().getLon(),
                          g.getLocation().getLocationName(),
                          Arrays.stream(args.getPlants()).collect(Collectors.toSet())))
                  .subscribeOn(Schedulers.io())
                  .observeOn(AndroidSchedulers.mainThread())
                  .subscribe(this::successGarden, this::failGarden);
          displayLoadingDialog();
        }
      } else {
        // opens from GardenFragment first Time
        if (args.getGarden().getPlants() != null && !args.getGarden().getPlants().isEmpty()) {
          showPlants(
              args.getGarden().getPlants().stream()
                  .map(GardeningItem::toDO)
                  .collect(Collectors.toList()));
        } else {
          showNoPlants();
        }
      }
    } else {
      displayErrorDialog("Error retrieving garden with plants");
    }
  }

  private void init() {
    binding.addPlant.setOnClickListener(this::addPlantPressed);
    binding.btnAddGarden.setOnClickListener(this::addPlantPressed);
  }

  private void updateWeather() {
    if (weatherDisposable != null && !weatherDisposable.isDisposed()) weatherDisposable.dispose();
    Garden g = args.getGarden().getGarden();
    weatherDisposable =
        Single.create(
                new GeminiWeatherTask(
                    g.getLocation().getLat(),
                    g.getLocation().getLon(),
                    g.getLocation().getLocationName()))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(this::successWeather, this::failWeather);
  }

  private void failWeather(Throwable throwable) {
    displayErrorDialog("Error retrieving weather from Gemini");
  }

  private void successWeather(GeminiWeather s) {
    if (getContext() != null) {
      WeatherAdapter adp = new WeatherAdapter(s.getWeather().getForecast());

      LinearLayoutManager llm =
          new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
      binding.listWeather.setAdapter(adp);
      binding.listWeather.setLayoutManager(llm);
      binding.city.setText(args.getGarden().getGarden().getLocation().getLocationName());
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
  }

  private void failGarden(Throwable throwable) {
    displayErrorDialog("Error retrieving garden with plants from Gemini");
  }

  private void successGarden(GeminiGardeningSugg geminiGardeningSugg) {
    List<GardeningItem> plants = geminiGardeningSugg.getFlowers();
    plants.addAll(geminiGardeningSugg.getVegetables());
    plants.addAll(geminiGardeningSugg.getFruits());
    showPlants(plants);
    fragmentDisposable =
        Observable.create(
                new CreatePlantsInGardenEmitter(
                    requireContext(),
                    args.getGarden().getGarden(),
                    plants.stream().map(GardeningItem::toDO).collect(Collectors.toSet())))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(this::handleOnNext, this::handleError, this::handleComplete);
  }

  private void handleComplete() {
    displaySuccessDialog("All Done");
    logd("All Done");
  }

  private void handleError(Throwable throwable) {
    displayErrorDialog("Error while saving new plants and data... try again later...");
    loge("handleError", throwable);
  }

  private void handleOnNext(Boolean aBoolean) {
    displaySuccessDialog("Plants saved successfully!\n all data as been updated...");
    logd("Plants saved successfully!\n all data as been updated...");
    logd("handleOnNext: " + aBoolean);
  }

  private void showNoPlants() {
    binding.emptyListGardens.setVisibility(View.VISIBLE);
    binding.listaItems.setVisibility(GONE);
  }

  private void showPlants(List<GardeningItem> plants) {
    binding.emptyListGardens.setVisibility(View.GONE);
    binding.listaItems.setVisibility(View.VISIBLE);
    args.getGarden()
        .setPlants(plants.stream().map(GardeningItem::toDO).collect(Collectors.toSet()));
    GardenPlantItemAdapter adp = new GardenPlantItemAdapter(plants);
    GridLayoutManager llm = new GridLayoutManager(requireContext(), 2);
    binding.listaItems.setAdapter(adp);
    binding.listaItems.setLayoutManager(llm);
  }

  private void addPlantPressed(View view) {
    if (weatherDisposable != null && !weatherDisposable.isDisposed()) {
      weatherDisposable.dispose();
    }
    if (gardenDisposable != null && !gardenDisposable.isDisposed()) {
      gardenDisposable.dispose();
    }
    Navigation.findNavController(view)
        .navigate(
            GardenFragmentDirections.actionGardenFragmentToAddPlantsFragment(args.getGarden()));
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    if (weatherDisposable != null && !weatherDisposable.isDisposed()) {
      weatherDisposable.dispose();
    }
    if (gardenDisposable != null && !gardenDisposable.isDisposed()) {
      gardenDisposable.dispose();
    }
    if (fragmentDisposable != null && !fragmentDisposable.isDisposed()) {
      fragmentDisposable.dispose();
    }
  }
}
