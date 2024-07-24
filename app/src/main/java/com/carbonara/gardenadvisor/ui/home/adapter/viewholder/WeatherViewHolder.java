package com.carbonara.gardenadvisor.ui.home.adapter.viewholder;

import static com.carbonara.gardenadvisor.util.ui.IconChooser.getIcon;

import android.view.View;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.carbonara.gardenadvisor.ai.dto.Day;
import com.carbonara.gardenadvisor.databinding.RowWeatherBinding;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Locale;

public class WeatherViewHolder extends RecyclerView.ViewHolder {

  RowWeatherBinding binding;

  Day day;

  public WeatherViewHolder(@NonNull View itemView) {
    super(itemView);
    binding = RowWeatherBinding.bind(itemView);
  }

  public void setDay(Day day) {
    this.day = day;
    binding.name.setText(
        day.getDate().format(new DateTimeFormatterBuilder().appendPattern("dd/MM").toFormatter()));
    binding.temp.setText(String.format(Locale.getDefault(), "%.1f", day.getCurrentTemp()));
    binding.iconWeather.setImageResource(getIcon(day.getIcon()));
  }
}
