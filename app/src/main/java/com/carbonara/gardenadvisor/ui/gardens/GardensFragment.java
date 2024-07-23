package com.carbonara.gardenadvisor.ui.gardens;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.carbonara.gardenadvisor.R;
import com.carbonara.gardenadvisor.databinding.FragmentGardensBinding;


public class GardensFragment extends Fragment {
    FragmentGardensBinding binding;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentGardensBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
}