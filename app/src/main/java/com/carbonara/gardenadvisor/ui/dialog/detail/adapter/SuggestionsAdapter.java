package com.carbonara.gardenadvisor.ui.dialog.detail.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import com.carbonara.gardenadvisor.R;
import com.carbonara.gardenadvisor.ui.dialog.detail.adapter.viewholder.SuggestionViewHolder;
import java.util.List;

public class SuggestionsAdapter extends Adapter<SuggestionViewHolder> {

  private final List<String> suggestions;

  public SuggestionsAdapter(List<String> suggestions) {
    this.suggestions = suggestions;
  }

  @NonNull
  @Override
  public SuggestionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    return new SuggestionViewHolder(
        LayoutInflater.from(parent.getContext()).inflate(R.layout.row_suggestion, parent, false));
  }

  @Override
  public void onBindViewHolder(@NonNull SuggestionViewHolder holder, int position) {
    holder.setSuggestion(suggestions.get(position));
  }

  @Override
  public int getItemCount() {
    return suggestions != null ? suggestions.size() : 0;
  }
}
