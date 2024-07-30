package com.carbonara.gardenadvisor.ui.garden.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.carbonara.gardenadvisor.R;
import com.carbonara.gardenadvisor.ai.dto.GardeningItem;
import com.carbonara.gardenadvisor.ui.garden.adapter.viewholder.GardenPlantItemViewHolder;
import com.carbonara.gardenadvisor.ui.home.adapter.viewholder.GardeningItemViewHolder;

import java.util.List;

public class GardenPlantItemAdapter extends RecyclerView.Adapter<GardenPlantItemViewHolder> {

  private final List<GardeningItem> list;

  public GardenPlantItemAdapter(List<GardeningItem> list) {
    this.list = list;
  }

  @NonNull
  @Override
  public GardenPlantItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    return new GardenPlantItemViewHolder(
        LayoutInflater.from(parent.getContext())
            .inflate(R.layout.row_garden_plant_item, parent, false));
  }

  @Override
  public void onBindViewHolder(@NonNull GardenPlantItemViewHolder holder, int position) {
    holder.setItem(list.get(position));
  }

  @Override
  public int getItemCount() {
    return list.size();
  }
}
