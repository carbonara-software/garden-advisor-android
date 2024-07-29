package com.carbonara.gardenadvisor.ui.addplants.adapter.viewholder;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import com.carbonara.gardenadvisor.databinding.RowAddPlantBinding;
import com.carbonara.gardenadvisor.ui.addplants.adapter.callbacks.AddPlantCallback;

public class AddPlantViewHolder extends ViewHolder {

  RowAddPlantBinding binding;
  private AddPlantCallback callback;
  String s;

  public AddPlantViewHolder(@NonNull View itemView, AddPlantCallback callback) {
    super(itemView);
    binding = RowAddPlantBinding.bind(itemView);

  }

  public void setS(String s) {
    this.s = s;
    if(binding.tfPlantName.getEditText() != null) {
      binding.tfPlantName.getEditText().addTextChangedListener(new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
          callback.onPlantTextAdded(getAdapterPosition(),s.toString());
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
      });
    }
  }
}
