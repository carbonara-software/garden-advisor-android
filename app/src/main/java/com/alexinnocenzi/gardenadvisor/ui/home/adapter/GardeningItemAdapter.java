package com.alexinnocenzi.gardenadvisor.ui.home.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alexinnocenzi.gardenadvisor.R;
import com.alexinnocenzi.gardenadvisor.ai.dto.GardeningItem;
import com.alexinnocenzi.gardenadvisor.ui.home.adapter.viewholder.GardeningItemViewHolder;

import java.util.List;

public class GardeningItemAdapter extends RecyclerView.Adapter<GardeningItemViewHolder> {

    private final List<GardeningItem> list;

    public GardeningItemAdapter(List<GardeningItem> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public GardeningItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new GardeningItemViewHolder(
                LayoutInflater
                        .from(parent.getContext())
                        .inflate(
                                R.layout.row_gardening_item,
                                parent,
                                false));
    }

    @Override
    public void onBindViewHolder(@NonNull GardeningItemViewHolder holder, int position) {
        holder.setItem(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
