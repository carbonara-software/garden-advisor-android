package com.carbonara.gardenadvisor.ui.home.adapter.viewholder;

import android.view.View;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import com.carbonara.gardenadvisor.ai.dto.GardeningItem;
import com.carbonara.gardenadvisor.databinding.RowGardeningItemBinding;
import com.carbonara.gardenadvisor.ui.dialog.detail.SuggestionsBottomSheet;
import com.carbonara.gardenadvisor.util.ui.BackgroundChooser;
import com.carbonara.gardenadvisor.util.ui.GAScoreUtil;

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

    int gaScore = GAScoreUtil.getGaScore(item);
    binding.recommended.setVisibility(gaScore >= 5 ? View.VISIBLE : View.GONE);
    binding.gaScore.setText(String.valueOf(gaScore));

    binding.innerCardLayout.setBackgroundResource(BackgroundChooser.getBackgroundForItem(item));

    binding
        .getRoot()
        .setOnClickListener(
            v -> {
              if (binding.getRoot().getContext() != null
                  && binding.getRoot().getContext() instanceof FragmentActivity) {
                SuggestionsBottomSheet bottomSheet = new SuggestionsBottomSheet(item);
                bottomSheet.show(
                    ((FragmentActivity) binding.getRoot().getContext()).getSupportFragmentManager(),
                    "DetailBottomSheet");
              }
            });
  }
}
