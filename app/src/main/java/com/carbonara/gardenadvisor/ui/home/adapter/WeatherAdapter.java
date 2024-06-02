package com.carbonara.gardenadvisor.ui.home.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.carbonara.gardenadvisor.R;
import com.carbonara.gardenadvisor.ai.dto.Day;
import com.carbonara.gardenadvisor.ui.home.adapter.viewholder.WeatherViewHolder;

import java.util.List;

public class WeatherAdapter extends RecyclerView.Adapter<WeatherViewHolder> {

    private final List<Day> days;

    public WeatherAdapter(List<Day> days) {
        this.days = days;
    }

    @NonNull
    @Override
    public WeatherViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new WeatherViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_weather,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull WeatherViewHolder holder, int position) {
        holder.setDay(days.get(position));
    }

    @Override
    public int getItemCount() {
        return days.size();
    }
}
