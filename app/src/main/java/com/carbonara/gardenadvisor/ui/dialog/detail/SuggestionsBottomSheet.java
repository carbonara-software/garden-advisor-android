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
import com.carbonara.gardenadvisor.util.ui.GAScoreUtil;
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
    binding.sheetGascore.setText(String.valueOf(GAScoreUtil.getGaScore(item)));
    binding.bottomsheetLayout.setBackgroundResource(BackgroundChooser.getBackgroundForItem(item));

    showSuggestions();
    showCautions();
    showPositives();

    binding.close.setOnClickListener(v -> dismiss());
  }

  private void showSuggestions() {
    LinearLayoutManager llmSugg = new LinearLayoutManager(requireContext());
    SuggestionsAdapter adapterSuggestions = new SuggestionsAdapter(item.getSuggestions());

    if (adapterSuggestions.getItemCount() == 0) {
      binding.labelCautions.setVisibility(View.GONE);
    }

    binding.rvSuggestions.setAdapter(adapterSuggestions);
    binding.rvSuggestions.setLayoutManager(llmSugg);
  }

  private void showPositives() {
    SuggestionsAdapter adapterPositives = new SuggestionsAdapter(item.getPositives());

    if (adapterPositives.getItemCount() == 0) {
      binding.labelCautions.setVisibility(View.GONE);
    }

    LinearLayoutManager llmPosit = new LinearLayoutManager(requireContext());
    binding.rvPositives.setAdapter(adapterPositives);
    binding.rvPositives.setLayoutManager(llmPosit);
  }

  private void showCautions() {
    SuggestionsAdapter adapterCautions = new SuggestionsAdapter(item.getCautions());

    if (adapterCautions.getItemCount() == 0) {
      binding.labelCautions.setVisibility(View.GONE);
    }

    LinearLayoutManager llmCaut = new LinearLayoutManager(requireContext());
    binding.rvCautions.setAdapter(adapterCautions);
    binding.rvCautions.setLayoutManager(llmCaut);
  }
}
