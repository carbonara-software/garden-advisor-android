package com.carbonara.gardenadvisor.ui.gardens.adapter.viewholder;

import android.view.View;
import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import com.carbonara.gardenadvisor.databinding.RowGardensItemBinding;
import com.carbonara.gardenadvisor.persistence.entity.GardenWithPlants;
import com.carbonara.gardenadvisor.ui.gardens.GardensFragmentDirections;

public class GardenViewHolder extends ViewHolder {

  private GardenWithPlants garden;
  private RowGardensItemBinding binding;

  public GardenViewHolder(@NonNull View itemView) {
    super(itemView);
    binding = RowGardensItemBinding.bind(itemView);
  }

  public void setGarden(GardenWithPlants garden) {
    this.garden = garden;
    binding.gaScore.setText("" + garden.getPlants().size());
    binding.name.setText(garden.getGarden().getLocation().getLocationName());
    binding.getRoot().setOnClickListener(this::onGardenClicked);
  }

  private void onGardenClicked(View view) {
    Navigation.findNavController(view)
        .navigate(GardensFragmentDirections.actionGardensFragmentToGardenFragment(garden));
  }
}
