package com.carbonara.gardenadvisor.ui.gardens.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import com.carbonara.gardenadvisor.R;
import com.carbonara.gardenadvisor.persistence.entity.GardenWithPlants;
import com.carbonara.gardenadvisor.ui.gardens.adapter.viewholder.GardenViewHolder;
import java.util.List;

public class GardensAdapter extends Adapter<GardenViewHolder> {

  private final List<GardenWithPlants> gardens;

  public GardensAdapter(List<GardenWithPlants> gardens) {
    this.gardens = gardens;
  }

  @NonNull
  @Override
  public GardenViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    return new GardenViewHolder(
        LayoutInflater.from(parent.getContext()).inflate(R.layout.row_gardens_item, parent, false));
  }

  @Override
  public void onBindViewHolder(@NonNull GardenViewHolder holder, int position) {
    holder.setGarden(gardens.get(position));
  }

  @Override
  public int getItemCount() {
    return gardens.size();
  }
}
