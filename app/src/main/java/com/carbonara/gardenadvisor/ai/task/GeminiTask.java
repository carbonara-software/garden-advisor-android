package com.carbonara.gardenadvisor.ai.task;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.Getter;

@Getter
public abstract class GeminiTask {

  private final float lat, lon;
  private final String locationName;

  protected GeminiTask(float lat, float lon, String locationName) {
    this.lat = lat;
    this.lon = lon;
    this.locationName = locationName;
  }

  public abstract String getPrompt() throws JsonProcessingException;
}
