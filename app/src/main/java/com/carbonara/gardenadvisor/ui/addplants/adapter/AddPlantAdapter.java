package com.carbonara.gardenadvisor.ui.addplants.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import com.carbonara.gardenadvisor.R;
import com.carbonara.gardenadvisor.ui.addplants.adapter.callbacks.DeletePlantCallback;
import com.carbonara.gardenadvisor.ui.addplants.adapter.viewholder.AddPlantViewHolder;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

public class AddPlantAdapter extends Adapter<AddPlantViewHolder> {

  @Getter private final List<String> plants;
  @Setter private DeletePlantCallback delCallback;

  public AddPlantAdapter(List<String> plants) {
    this.plants = plants;
  }

  @NonNull
  @Override
  public AddPlantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    return new AddPlantViewHolder(
        LayoutInflater.from(parent.getContext()).inflate(R.layout.row_add_plant, parent, false),
        this::itemTextChange,
        this::removeItem);
  }

  @Override
  public void onBindViewHolder(@NonNull AddPlantViewHolder holder, int position) {
    holder.setS(plants.get(position));
  }

  @Override
  public int getItemCount() {
    return plants.size();
  }

  public void addItem() {
    plants.add("");
    notifyItemInserted(plants.size() - 1);
  }

  public void removeItem(int position) {
    plants.remove(position);
    if (delCallback != null) delCallback.onDeleteClicked(position);
    notifyItemRemoved(position);
  }

  public void itemTextChange(int position, String s) {
    plants.set(position, s);
    // notifyItemChanged(position);
  }
}
