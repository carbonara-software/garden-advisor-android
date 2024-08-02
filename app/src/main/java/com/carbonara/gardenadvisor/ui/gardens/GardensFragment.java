package com.carbonara.gardenadvisor.ui.gardens;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import com.carbonara.gardenadvisor.databinding.FragmentGardensBinding;
import com.carbonara.gardenadvisor.persistence.entity.GardenWithPlants;
import com.carbonara.gardenadvisor.ui.dialog.newgarden.NewGardenBottomSheet;
import com.carbonara.gardenadvisor.ui.dialog.newgarden.callback.NewGardenCallback;
import com.carbonara.gardenadvisor.ui.gardens.adapter.GardensAdapter;
import com.carbonara.gardenadvisor.util.task.GardensWithPlantEmitter;
import com.carbonara.gardenadvisor.util.ui.BaseFragment;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import java.util.List;

public class GardensFragment extends BaseFragment implements NewGardenCallback {
  FragmentGardensBinding binding;

  Disposable disposable;

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    showBottomBar();
    initDB();
    binding.addGarden.setOnClickListener(this::onAddGarden);
    binding.btnAddGarden.setOnClickListener(this::onAddGarden);
  }

  private void initDB() {
    disposable =
        Observable.create(new GardensWithPlantEmitter(requireContext()))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(this::populateGardenList, this::handleError);
  }

  private void onAddGarden(View view) {
    NewGardenBottomSheet bottomSheet = new NewGardenBottomSheet(this);
    bottomSheet.show(requireActivity().getSupportFragmentManager(), "NewGardenBottomSheet");
  }

  private void handleError(Throwable throwable) {
    displayErrorDialog("Error retrieving gardens list");
  }

  private void populateGardenList(List<GardenWithPlants> gardenWithPlantsList) {
    if (gardenWithPlantsList != null && !gardenWithPlantsList.isEmpty()) {
      if (getContext() == null) return;
      GardensAdapter adp = new GardensAdapter(gardenWithPlantsList);
      GridLayoutManager llm = new GridLayoutManager(getContext(), 2);
      binding.recyclerGardens.setVisibility(View.VISIBLE);
      binding.emptyListGardens.setVisibility(View.GONE);
      binding.recyclerGardens.setLayoutManager(llm);
      binding.recyclerGardens.setAdapter(adp);
    } else {
      binding.recyclerGardens.setVisibility(View.GONE);
      binding.emptyListGardens.setVisibility(View.VISIBLE);
    }
  }

  @Override
  public View onCreateView(
      @NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    binding = FragmentGardensBinding.inflate(inflater, container, false);
    return binding.getRoot();
  }

  @Override
  public void successGardenCreation() {
    initDB();
    displaySuccessDialog("Garden created successfully");
  }

  @Override
  public void errorGardenCreation(Throwable error) {
    initDB();
    displayErrorDialog("Error creating garden, please try again");
  }
}
