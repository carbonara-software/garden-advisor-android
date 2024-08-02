package com.carbonara.gardenadvisor.ai.cache;

import com.carbonara.gardenadvisor.ai.dto.GeminiGardeningSugg;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.jackson.Jacksonized;

@Getter
@Setter
@AllArgsConstructor
@Jacksonized
@Builder
public class HomeCache {

  private float lat;
  private float lon;
  private String locationName;
  private GeminiGardeningSugg garden;
  @Builder.Default private final long lastUpdated = System.currentTimeMillis();
}
