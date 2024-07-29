package com.carbonara.gardenadvisor.ui.addplants.adapter.viewholder;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import com.carbonara.gardenadvisor.databinding.RowAddPlantBinding;
import com.carbonara.gardenadvisor.ui.addplants.adapter.callbacks.AddPlantCallback;
import com.carbonara.gardenadvisor.ui.addplants.adapter.callbacks.DeletePlantCallback;

public class AddPlantViewHolder extends ViewHolder {

  RowAddPlantBinding binding;
  private final AddPlantCallback callbackAdd;
  private final DeletePlantCallback callbackDel;
  String s;

  public AddPlantViewHolder(@NonNull View itemView, AddPlantCallback callbackAdd,DeletePlantCallback callbackDel) {
    super(itemView);
    binding = RowAddPlantBinding.bind(itemView);
    this.callbackAdd = callbackAdd;
    this.callbackDel = callbackDel;
  }


  public void setS(String s) {
    this.s = s;
    if (binding.tfPlantName.getEditText() != null) {
      binding
          .tfPlantName
          .getEditText()
              .setOnFocusChangeListener(this::onFocusChange);
//          .addTextChangedListener(
//              new TextWatcher() {
//                @Override
//                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
//
//                @Override
//                public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//                }
//
//                @Override
//                public void afterTextChanged(Editable s) {
//                  callbackAdd.onPlantTextAdded(getAdapterPosition(), s.toString());
//                }
//              });
      binding.imgDelete.setOnClickListener(v -> callbackDel.onDeleteClicked(getAdapterPosition()));
      binding.txtClose.setOnClickListener(v -> showDelete());
    }
  }

  private void onFocusChange(View view, boolean b) {
    if(!b && binding.tfPlantName.getEditText()!=null
        && binding.tfPlantName.getEditText().getText()!=null
        && !binding.tfPlantName.getEditText().getText().toString().isEmpty()) {
      callbackAdd.onPlantTextAdded(getAdapterPosition(),
          binding.tfPlantName.getEditText().getText().toString());
    }
  }

  public void showDelete(){
      binding.imgDelete.setVisibility(binding.imgDelete.getVisibility() != View.VISIBLE?View.VISIBLE:View.GONE);
  }

}
