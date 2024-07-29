package com.carbonara.gardenadvisor.ui.addplants.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import com.carbonara.gardenadvisor.R;
import com.carbonara.gardenadvisor.ui.addplants.adapter.viewholder.AddPlantViewHolder;
import java.util.ArrayList;
import java.util.List;

public class AddPlantAdapter extends Adapter<AddPlantViewHolder> {

  private List<String> plants;

  public AddPlantAdapter(List<String> plants) {
    this.plants = plants;
  }

  @NonNull
  @Override
  public AddPlantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    return new AddPlantViewHolder(
        LayoutInflater.from(parent.getContext()).inflate(R.layout.row_add_plant, parent, false),
        this::itemTextChange);
  }

  @Override
  public void onBindViewHolder(@NonNull AddPlantViewHolder holder, int position) {}

  @Override
  public int getItemCount() {
    return plants.size();
  }

  public List<String> getDataList() {
    List<String> result = new ArrayList<>();
    for (int i = 0; i < plants.size(); i++) {
      result.add(plants.get(i));
    }
    return result;
  }

  public void addItem() {
    plants.add("");
    notifyItemInserted(plants.size() - 1);
  }

  public void itemTextChange(int position, String s) {
    plants.set(position, s);
    notifyItemChanged(position);
  }
}
