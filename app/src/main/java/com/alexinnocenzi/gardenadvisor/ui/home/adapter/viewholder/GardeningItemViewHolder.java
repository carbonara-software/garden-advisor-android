package com.alexinnocenzi.gardenadvisor.ui.home.adapter.viewholder;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alexinnocenzi.gardenadvisor.ai.dto.GardeningItem;
import com.alexinnocenzi.gardenadvisor.databinding.RowGardeningItemBinding;

public class GardeningItemViewHolder extends RecyclerView.ViewHolder {

    RowGardeningItemBinding binding;
    GardeningItem item;
    public GardeningItemViewHolder(@NonNull View itemView) {
        super(itemView);
        binding = RowGardeningItemBinding.bind(itemView);
    }

    public void setItem(GardeningItem item) {
        this.item = item;
        binding.name.setText(item.getName());
        binding.recommended.setVisibility(item.isRecommended()?View.VISIBLE:View.GONE);
    }
}
