package com.carbonara.gardenadvisor.ui.addplants;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.carbonara.gardenadvisor.databinding.FragmentAddPlantsBinding;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.carbonara.gardenadvisor.persistence.entity.GardenWithPlants;
import com.carbonara.gardenadvisor.ui.addplants.adapter.AddPlantAdapter;
import com.carbonara.gardenadvisor.ui.addplants.adapter.swipe.SwipeToDeleteCallback;

import java.util.ArrayList;
import java.util.List;

public class AddPlantsFragment extends Fragment {

  FragmentAddPlantsBinding binding;
  private AddPlantAdapter adapter;
  private List<String> plants;
  GardenWithPlants gardenWithPlants;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    binding = FragmentAddPlantsBinding.inflate(inflater, container, false);
    return binding.getRoot();
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    if(getArguments() != null) {
      AddPlantsFragmentArgs args = AddPlantsFragmentArgs.fromBundle(getArguments());
      gardenWithPlants = args.getGarden();
      List<String> plants = new ArrayList<>();
      adapter = new AddPlantAdapter(plants);
      binding.listaItems.setLayoutManager(new LinearLayoutManager(getContext()));
      binding.listaItems.setAdapter(adapter);
      ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new SwipeToDeleteCallback(adapter));
      itemTouchHelper.attachToRecyclerView(binding.listaItems);
      binding.savePlants.setOnClickListener(this::savePlants);
      binding.addPlant.setOnClickListener(this::addPlant);
    }
  }

  private void addPlant(View view) {
    if (adapter != null) {
      adapter.addItem();
    }
  }

  private void savePlants(View view) {
    if (adapter != null) {
      plants = adapter.getPlants();
      Navigation.findNavController(view)
          .navigate(AddPlantsFragmentDirections
              .actionAddPlantsFragmentToGardenFragment(gardenWithPlants)
              .setPlants(plants.toArray(new String[plants.size()])));
    }
  }

}
