package com.carbonara.gardenadvisor.ui.dialog.newgarden;

import android.location.Address;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.carbonara.gardenadvisor.databinding.BottomsheetNewGardenBinding;
import com.carbonara.gardenadvisor.util.AppUtil;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class NewGardenBottomSheet extends BottomSheetDialogFragment {

    BottomsheetNewGardenBinding binding;

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    binding = BottomsheetNewGardenBinding.inflate(inflater, container, false);
    return binding.getRoot();
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    binding.tfGardenLocation.getEditText().addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {

      }

      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {

      }

      @Override
      public void afterTextChanged(Editable s) {
        Address address = AppUtil.getCurrentLocationLatLon(requireContext(), s.toString());
        if(address != null) {
          s.delete(0, s.length()).append(address.getLocality());
          binding.tfGardenLocation.setError(null);
        }else{
          binding.tfGardenLocation.setError("No location found");
        }
      }
    });
    binding.tfGardenName.getEditText().addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {

      }

      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {

      }

      @Override
      public void afterTextChanged(Editable s) {
        // TODO: Manage checks for already used name in LocalDB
      }
    });
    binding.btnAddGarden.setOnClickListener(this::addPressed);
  }

  private void addPressed(View view) {
    // TODO: Add new garden
  }
}
