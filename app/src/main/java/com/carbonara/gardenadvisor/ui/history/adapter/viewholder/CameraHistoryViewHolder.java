package com.carbonara.gardenadvisor.ui.history.adapter.viewholder;

import android.view.View;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import com.carbonara.gardenadvisor.ai.dto.GeminiCameraSuggestion;
import com.carbonara.gardenadvisor.databinding.RowHistoryItemBinding;
import com.carbonara.gardenadvisor.ui.dialog.detail.CameraBottomSheet;

public class CameraHistoryViewHolder extends ViewHolder {

  RowHistoryItemBinding binding;
  String cachedCameraSuggestionId;

  public CameraHistoryViewHolder(@NonNull View itemView) {
    super(itemView);
    binding = RowHistoryItemBinding.bind(itemView);
  }

  public void setCameraSuggestion(GeminiCameraSuggestion cameraSuggestion) {
    binding.name.setText(cameraSuggestion.getName());
    binding.status.setText(cameraSuggestion.getStatusEmoji());
    cachedCameraSuggestionId = cameraSuggestion.getCachedId();
    binding
        .getRoot()
        .setOnClickListener(
            v -> {
              if (binding.getRoot().getContext() != null
                  && binding.getRoot().getContext() instanceof FragmentActivity) {
                CameraBottomSheet bottomSheet = new CameraBottomSheet(cachedCameraSuggestionId);
                bottomSheet.show(
                    ((FragmentActivity) binding.getRoot().getContext()).getSupportFragmentManager(),
                    "CameraBottomSheet");
              }
            });
  }
}
