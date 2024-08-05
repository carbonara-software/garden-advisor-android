package com.carbonara.gardenadvisor.ui.history.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import com.carbonara.gardenadvisor.R;
import com.carbonara.gardenadvisor.ai.dto.GeminiCameraSuggestion;
import com.carbonara.gardenadvisor.ui.history.adapter.viewholder.CameraHistoryViewHolder;
import java.util.List;

public class CameraHistoryAdapter extends Adapter<CameraHistoryViewHolder> {

  private final List<GeminiCameraSuggestion> cameraSuggestions;

  public CameraHistoryAdapter(List<GeminiCameraSuggestion> cameraSuggestions) {
    this.cameraSuggestions = cameraSuggestions;
  }

  @NonNull
  @Override
  public CameraHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    return new CameraHistoryViewHolder(
        LayoutInflater.from(parent.getContext()).inflate(R.layout.row_history_item, parent, false));
  }

  @Override
  public void onBindViewHolder(@NonNull CameraHistoryViewHolder holder, int position) {
    holder.setCameraSuggestion(cameraSuggestions.get(position));
  }

  @Override
  public int getItemCount() {
    return cameraSuggestions.size();
  }
}
