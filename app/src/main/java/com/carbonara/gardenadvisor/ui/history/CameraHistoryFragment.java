package com.carbonara.gardenadvisor.ui.history;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import com.carbonara.gardenadvisor.ai.dto.GeminiCameraSuggestion;
import com.carbonara.gardenadvisor.databinding.FragmentCameraHistoryBinding;
import com.carbonara.gardenadvisor.ui.history.adapter.CameraHistoryAdapter;
import com.carbonara.gardenadvisor.util.task.CameraHistoryEmitter;
import com.carbonara.gardenadvisor.util.ui.BaseFragment;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import java.util.List;

public class CameraHistoryFragment extends BaseFragment {

  FragmentCameraHistoryBinding binding;
  Disposable disposable;

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    showBottomBar();
    initHistory();
  }

  @Override
  public View onCreateView(
      @NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    binding = FragmentCameraHistoryBinding.inflate(inflater, container, false);
    return binding.getRoot();
  }

  private void initHistory() {
    disposable =
        Single.create(new CameraHistoryEmitter())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(this::populateHistory, this::handleError);
  }

  private void populateHistory(List<GeminiCameraSuggestion> cameraHistoryList) {
    if (cameraHistoryList != null && !cameraHistoryList.isEmpty()) {
      if (getContext() == null) return;
      CameraHistoryAdapter adp = new CameraHistoryAdapter(cameraHistoryList);
      GridLayoutManager llm = new GridLayoutManager(getContext(), 2);
      binding.recyclerCameraHistory.setVisibility(View.VISIBLE);
      binding.emptyCameraHistory.setVisibility(View.GONE);
      binding.recyclerCameraHistory.setLayoutManager(llm);
      binding.recyclerCameraHistory.setAdapter(adp);
    } else {
      binding.recyclerCameraHistory.setVisibility(View.GONE);
      binding.emptyCameraHistory.setVisibility(View.VISIBLE);
    }
  }

  private void handleError(Throwable throwable) {
    displayErrorDialog("Could not retrieve recent AI Camera Suggestions");
  }
}
