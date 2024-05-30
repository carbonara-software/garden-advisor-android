package com.alexinnocenzi.gardenadvisor.ui.home;

import static com.alexinnocenzi.gardenadvisor.util.LogUtil.loge;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alexinnocenzi.gardenadvisor.ai.GeminiWrapper;
import com.alexinnocenzi.gardenadvisor.ai.dto.GeminiWeather;
import com.alexinnocenzi.gardenadvisor.databinding.FragmentHomeBinding;
import com.alexinnocenzi.gardenadvisor.util.ui.BaseFragment;

public class HomeFragment extends BaseFragment {
    private static final String TAG = "HomeFrag";
    FragmentHomeBinding binding;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //ToDo: Define and implement home fragment...
        loge("Sta per partire...");
        displayLoadingDialog();
        GeminiWrapper wrapper = GeminiWrapper.getInstance();
        wrapper.getAnswerWeather(this::success,this::fail);

    }

    private void fail(Throwable throwable) {
        closeDialog();
        loge(throwable);
    }

    private void success(GeminiWeather s) {
        closeDialog();
        loge("Eccolo: " + s);
    }

}