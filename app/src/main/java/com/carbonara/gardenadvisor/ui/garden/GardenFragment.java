package com.carbonara.gardenadvisor.ui.garden;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.carbonara.gardenadvisor.R;
import com.carbonara.gardenadvisor.databinding.FragmentGardenBinding;


public class GardenFragment extends Fragment {

    FragmentGardenBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentGardenBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
}