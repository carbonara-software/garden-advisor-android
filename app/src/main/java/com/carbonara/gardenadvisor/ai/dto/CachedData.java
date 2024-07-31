package com.carbonara.gardenadvisor.ai.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CachedData implements Comparable<CachedData> {

  private String weatherString;
  private float lat;
  private float lon;
  private String locationName;
  private GeminiWeather weather;
  private final LocalDateTime lastUpdated = LocalDateTime.now();

  @Override
  public int compareTo(CachedData o) {
    // Compare datetime
    int dateCompare = lastUpdated.compareTo(o.lastUpdated);
    if (dateCompare != 0) {
      return dateCompare;
    }
    // Compare latitude
    int latComparison = Float.compare(this.lat, o.lat);
    if (latComparison != 0) {
      return latComparison;
    }
    // Compare longitude
    int lonComparison = Float.compare(this.lon, o.lon);
    if (lonComparison != 0) {
      return lonComparison;
    }
    // Compare location name (case-insensitive)
    return this.locationName.compareToIgnoreCase(o.locationName);
  }
}
