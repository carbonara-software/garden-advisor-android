package com.carbonara.gardenadvisor.ai.cache;

import com.carbonara.gardenadvisor.ai.dto.GeminiGardeningSugg;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class HomeCache implements Comparable<HomeCache>{

  private float lat;
  private float lon;
  private String locationName;
  private GeminiGardeningSugg garden;
  private final LocalDateTime lastUpdated = LocalDateTime.now();


  @Override
  public int compareTo(HomeCache o) {
    // Compare datetime
    int dateCompare = lastUpdated.compareTo(o.lastUpdated);
    if (dateCompare != 0) {
      return dateCompare;
    }
    // Compare location name (case-insensitive)
    return this.locationName.compareToIgnoreCase(o.locationName);
  }
}
