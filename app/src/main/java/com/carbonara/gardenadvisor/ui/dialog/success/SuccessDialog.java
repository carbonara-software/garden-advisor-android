package com.carbonara.gardenadvisor.ui.dialog.success;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import com.carbonara.gardenadvisor.R;
import com.carbonara.gardenadvisor.databinding.DialogSuccessBinding;

public class SuccessDialog extends DialogFragment {

  public static final String TAG = "SuccessDialog";
  DialogSuccessBinding binding;

  String message;

  public SuccessDialog(String message) {
    this.message = message;
  }

  @NonNull
  @Override
  public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
    binding = DialogSuccessBinding.inflate(getLayoutInflater());
    return new AlertDialog.Builder(requireContext(), R.style.TransparentDialog)
        .setView(binding.getRoot())
        .create();
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    binding.message.setText(message);
  }
}
