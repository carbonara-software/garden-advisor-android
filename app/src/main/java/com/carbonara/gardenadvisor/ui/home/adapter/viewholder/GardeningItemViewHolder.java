package com.carbonara.gardenadvisor.ui.home.adapter.viewholder;

import android.view.View;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.carbonara.gardenadvisor.ai.dto.GardeningItem;
import com.carbonara.gardenadvisor.databinding.RowGardeningItemBinding;

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
    binding.recommended.setVisibility(item.isRecommended() ? View.VISIBLE : View.GONE);
  }
}
