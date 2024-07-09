package com.carbonara.gardenadvisor.ui.dialog.detail;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.carbonara.gardenadvisor.ai.dto.GardeningItem;
import com.carbonara.gardenadvisor.databinding.BottomsheetSuggestionsBinding;
import com.carbonara.gardenadvisor.ui.dialog.detail.adapter.SuggestionsAdapter;
import com.carbonara.gardenadvisor.util.ui.BackgroundChooser;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class SuggestionsBottomSheet extends BottomSheetDialogFragment {

  private final GardeningItem item;

  BottomsheetSuggestionsBinding binding;

  public SuggestionsBottomSheet(GardeningItem item) {
    this.item = item;
  }

  @Nullable
  @Override
  public View onCreateView(
      @NonNull LayoutInflater inflater,
      @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    binding = BottomsheetSuggestionsBinding.inflate(inflater, container, false);
    return binding.getRoot();
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    binding.name.setText(item.getName());
    binding.bottomsheetLayout.setBackgroundResource(BackgroundChooser.getBackgroundForItem(item));
    SuggestionsAdapter adapterSuggestions = new SuggestionsAdapter(item.getSuggestions());
    SuggestionsAdapter adapterCautions = new SuggestionsAdapter(item.getCautions());
    SuggestionsAdapter adapterPositives = new SuggestionsAdapter(item.getPositives());
    LinearLayoutManager llmSugg = new LinearLayoutManager(requireContext());
    LinearLayoutManager llmCaut = new LinearLayoutManager(requireContext());
    LinearLayoutManager llmPosit = new LinearLayoutManager(requireContext());
    binding.rvSuggestions.setAdapter(adapterSuggestions);
    binding.rvSuggestions.setLayoutManager(llmSugg);
    binding.rvCautions.setAdapter(adapterCautions);
    binding.rvCautions.setLayoutManager(llmCaut);
    binding.rvPositives.setAdapter(adapterPositives);
    binding.rvPositives.setLayoutManager(llmPosit);
    binding.close.setOnClickListener(v -> dismiss());
  }
}
