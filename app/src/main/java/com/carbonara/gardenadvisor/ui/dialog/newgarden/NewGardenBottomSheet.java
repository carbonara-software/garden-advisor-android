package com.carbonara.gardenadvisor.ui.dialog.newgarden;

import android.content.DialogInterface;
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
import com.carbonara.gardenadvisor.persistence.AppDatabase;
import com.carbonara.gardenadvisor.persistence.dao.GardenDao;
import com.carbonara.gardenadvisor.persistence.dao.PlantDao;
import com.carbonara.gardenadvisor.persistence.entity.Garden;
import com.carbonara.gardenadvisor.persistence.entity.Location;
import com.carbonara.gardenadvisor.persistence.repository.GardenRepository;
import com.carbonara.gardenadvisor.ui.dialog.newgarden.callback.NewGardenCallback;
import com.carbonara.gardenadvisor.util.AppUtil;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import io.reactivex.rxjava3.disposables.Disposable;

public class NewGardenBottomSheet extends BottomSheetDialogFragment {

  BottomsheetNewGardenBinding binding;
  Location location;
  private Disposable d;
  private final NewGardenCallback callback;

  public NewGardenBottomSheet(NewGardenCallback callback) {
    this.callback = callback;
  }

  @Nullable
  @Override
  public View onCreateView(
      @NonNull LayoutInflater inflater,
      @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    binding = BottomsheetNewGardenBinding.inflate(inflater, container, false);
    return binding.getRoot();
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    binding
        .tfGardenLocation
        .getEditText()
        .addTextChangedListener(
            new TextWatcher() {
              @Override
              public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

              @Override
              public void onTextChanged(CharSequence s, int start, int before, int count) {}

              @Override
              public void afterTextChanged(Editable s) {
                Address address = AppUtil.getCurrentLocationLatLon(requireContext(), s.toString());
                if (address != null) {
                  location = new Location();
                  location.setLat((float) address.getLatitude());
                  location.setLon((float) address.getLongitude());
                  location.setLocationName(address.getLocality());
                  s.delete(0, s.length()).append(address.getLocality());
                  binding.tfGardenLocation.setError(null);
                } else {
                  binding.tfGardenLocation.setError("No location found");
                }
              }
            });
    binding
        .tfGardenName
        .getEditText()
        .addTextChangedListener(
            new TextWatcher() {
              @Override
              public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

              @Override
              public void onTextChanged(CharSequence s, int start, int before, int count) {}

              @Override
              public void afterTextChanged(Editable s) {
                // TODO: Manage checks for already used name in LocalDB
              }
            });
    binding.btnAddGarden.setOnClickListener(this::addPressed);
  }

  private void addPressed(View view) {
    // TODO: Add new garden
    if (location == null) {
      binding.tfGardenLocation.setError("No location set");
      return;
    }
    if ((binding.tfGardenName.getEditText() != null
            && binding.tfGardenName.getEditText().getText().toString().isEmpty())
        || binding.tfGardenName.getEditText() == null) {
      binding.tfGardenName.setError("No description set");
      return;
    }
    binding.tfGardenLocation.setError(null);
    binding.tfGardenName.setError(null);
    Garden garden = new Garden();
    garden.setDescription(binding.tfGardenName.getEditText().getText().toString());
    garden.setLocation(location);
    GardenDao gardenDao = AppDatabase.getDatabase(requireContext()).gardenDao();
    PlantDao plantDao = AppDatabase.getDatabase(requireContext()).plantDao();
    GardenRepository repository = new GardenRepository(gardenDao, plantDao);
    d =
        repository
            .insertGarden(garden)
            .doOnComplete(this::handleComplete)
            .doOnError(this::handleError)
            .subscribe();
  }

  private void handleComplete() {
    dismiss();
    callback.successGardenCreation();
  }

  private void handleError(Throwable throwable) {
    dismiss();
    callback.errorGardenCreation(throwable);
  }

  @Override
  public void onDismiss(@NonNull DialogInterface dialog) {
    super.onDismiss(dialog);
    if (d != null && !d.isDisposed()) {
      d.dispose();
    }
  }
}
