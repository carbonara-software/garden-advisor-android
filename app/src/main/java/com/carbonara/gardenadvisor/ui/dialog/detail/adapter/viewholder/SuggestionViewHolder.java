package com.carbonara.gardenadvisor.ui.dialog.detail.adapter.viewholder;

import android.view.View;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import com.carbonara.gardenadvisor.databinding.RowSuggestionBinding;

public class SuggestionViewHolder extends ViewHolder {

  RowSuggestionBinding binding;

  String suggestion;

  public SuggestionViewHolder(@NonNull View itemView) {
    super(itemView);
    binding = RowSuggestionBinding.bind(itemView);
  }

  public void setSuggestion(String suggestion) {
    this.suggestion = suggestion;
    binding.name.setText(suggestion);
  }
}
