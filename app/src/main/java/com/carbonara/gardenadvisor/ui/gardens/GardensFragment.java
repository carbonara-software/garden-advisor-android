package com.carbonara.gardenadvisor.ui.gardens;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.carbonara.gardenadvisor.databinding.FragmentGardensBinding;
import com.carbonara.gardenadvisor.persistence.AppDatabase;
import com.carbonara.gardenadvisor.persistence.dao.GardenDao;
import com.carbonara.gardenadvisor.persistence.dao.PlantDao;
import com.carbonara.gardenadvisor.persistence.entity.GardenWithPlants;
import com.carbonara.gardenadvisor.persistence.repository.GardenRepository;
import com.carbonara.gardenadvisor.ui.dialog.newgarden.NewGardenBottomSheet;
import com.carbonara.gardenadvisor.ui.dialog.newgarden.callback.NewGardenCallback;
import com.carbonara.gardenadvisor.ui.gardens.adapter.GardensAdapter;
import com.carbonara.gardenadvisor.util.ui.BaseFragment;
import io.reactivex.rxjava3.disposables.Disposable;
import java.util.List;

public class GardensFragment extends BaseFragment implements NewGardenCallback {
  FragmentGardensBinding binding;
  GardenRepository repository;
  Disposable d;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    GardenDao gardenDao = AppDatabase.getDatabase(requireContext()).gardenDao();
    PlantDao plantDao = AppDatabase.getDatabase(requireContext()).plantDao();
    repository = new GardenRepository(gardenDao, plantDao);
    initDB();
    binding.addGarden.setOnClickListener(this::onAddGarden);
    binding.btnAddGarden.setOnClickListener(this::onAddGarden);
  }

  private void initDB() {
    d =
        repository
            .getGardensWithPlants()
            .doOnNext(this::populateGardenList)
            .doOnError(this::handleError)
            .subscribe();
  }

  private void onAddGarden(View view) {
    NewGardenBottomSheet bottomSheet = new NewGardenBottomSheet(this);
    bottomSheet.show(requireActivity().getSupportFragmentManager(), "NewGardenBottomSheet");
  }

  private void handleError(Throwable throwable) {
    displayErrorDialog("Error retrieving gardens list");
  }

  private void populateGardenList(List<GardenWithPlants> g) {
    if (g != null && !g.isEmpty()) {
      GardensAdapter adp = new GardensAdapter(g);
      LinearLayoutManager llm = new LinearLayoutManager(requireContext());
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
