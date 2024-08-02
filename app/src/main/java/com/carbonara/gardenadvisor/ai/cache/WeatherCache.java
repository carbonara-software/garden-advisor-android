package com.carbonara.gardenadvisor.ai.cache;

import com.carbonara.gardenadvisor.ai.dto.GeminiWeather;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.jackson.Jacksonized;

@Getter
@Jacksonized
@Builder
public class WeatherCache implements Comparable<WeatherCache> {

  private final String weatherString;
  private final float lat;
  private final float lon;
  private final String locationName;
  private final GeminiWeather weather;
  @Builder.Default
  private final long lastUpdated = System.currentTimeMillis();



  @Override
  public int compareTo(WeatherCache o) {
    return locationName.compareToIgnoreCase(o.locationName);
  }
}
