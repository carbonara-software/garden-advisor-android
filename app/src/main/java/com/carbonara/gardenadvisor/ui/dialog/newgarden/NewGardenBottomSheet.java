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
import androidx.core.content.ContextCompat;
import com.carbonara.gardenadvisor.R;
import com.carbonara.gardenadvisor.databinding.BottomsheetNewGardenBinding;
import com.carbonara.gardenadvisor.persistence.entity.Garden;
import com.carbonara.gardenadvisor.persistence.entity.Location;
import com.carbonara.gardenadvisor.ui.dialog.newgarden.callback.NewGardenCallback;
import com.carbonara.gardenadvisor.util.AppUtil;
import com.carbonara.gardenadvisor.util.task.CreateGardenEmitter;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import java.util.Timer;
import java.util.TimerTask;

public class NewGardenBottomSheet extends BottomSheetDialogFragment {

  BottomsheetNewGardenBinding binding;
  Location location;
  private Disposable disposable;
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
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    binding
        .tfGardenLocation
        .getEditText()
        .addTextChangedListener(
            new TextWatcher() {
              private Timer timer = null;
              private final long DELAY = 1000; // 1 second delay

              @Override
              public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

              @Override
              public void onTextChanged(CharSequence s, int start, int before, int count) {}

              @Override
              public void afterTextChanged(Editable s) {
                if (timer != null) {
                  timer.cancel();
                }
                timer = new Timer();
                timer.schedule(
                    new TimerTask() {
                      @Override
                      public void run() {
                        if (s != null && s.length() > 0 && getContext() != null) {
                          Address address =
                              AppUtil.getCurrentLocationLatLon(getContext(), s.toString());
                          requireActivity()
                              .runOnUiThread(
                                  () -> { // Update UI on main thread
                                    if (address != null) {
                                      location = new Location();
                                      location.setLat((float) address.getLatitude());
                                      location.setLon((float) address.getLongitude());
                                      location.setLocationName(address.getLocality());
                                      binding
                                          .tfGardenLocation
                                          .getEditText()
                                          .setText(address.getLocality()); // Set resolved location
                                      binding.tfGardenLocation.setError(null);
                                      binding.tfGardenLocation.setHintEnabled(true);
                                      binding.tfGardenLocation.setHint(
                                          "location risolta con successo");
                                      binding.tfGardenLocation.setBoxStrokeColor(
                                          ContextCompat.getColor(getContext(), R.color.green));
                                    } else {
                                      binding.tfGardenLocation.setError("No location found");
                                    }
                                  });
                        } else {
                          if (timer != null) timer.cancel();
                        }
                      }
                    },
                    DELAY);
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

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  private void addPressed(View view) {
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
    disposable =
        Observable.create(new CreateGardenEmitter(requireContext(), garden))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(this::handleOnNext, this::handleError, this::handleComplete);
  }

  private void handleOnNext(Boolean aBoolean) {}

  private void handleComplete() {

    callback.successGardenCreation();
    dismiss();
  }

  private void handleError(Throwable throwable) {

    callback.errorGardenCreation(throwable);
    dismiss();
  }

  @Override
  public void onDismiss(@NonNull DialogInterface dialog) {
    super.onDismiss(dialog);
    if (disposable != null && !disposable.isDisposed()) {
      disposable.dispose();
    }
  }
}
