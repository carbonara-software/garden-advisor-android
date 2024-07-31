package com.carbonara.gardenadvisor.ai.task;

import lombok.Getter;

@Getter
public abstract class GeminiWeatherDependentTask extends GeminiTask {

  private final String openMeteoResultString;

  public GeminiWeatherDependentTask(
      float lat, float lon, String locationName, String openMeteoResultString) {
    super(lat, lon, locationName);
    this.openMeteoResultString = openMeteoResultString;
  }
}
