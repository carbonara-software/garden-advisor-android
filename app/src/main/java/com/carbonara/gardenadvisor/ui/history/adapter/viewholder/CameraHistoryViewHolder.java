package com.carbonara.gardenadvisor.ui.history.adapter.viewholder;

import android.view.View;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import com.carbonara.gardenadvisor.ai.dto.GeminiCameraSuggestion;
import com.carbonara.gardenadvisor.databinding.RowHistoryItemBinding;

public class CameraHistoryViewHolder extends ViewHolder {

  RowHistoryItemBinding binding;

  public CameraHistoryViewHolder(@NonNull View itemView) {
    super(itemView);
    binding = RowHistoryItemBinding.bind(itemView);
  }

  public void setCameraSuggestion(GeminiCameraSuggestion cameraSuggestion) {
    binding.name.setText(cameraSuggestion.getName());
    binding.status.setText(cameraSuggestion.getStatusEmoji());
  }
}
