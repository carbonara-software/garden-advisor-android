package com.carbonara.gardenadvisor.ai.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class GeminiWeather implements GeminiResult {

  @JsonProperty("weather")
  private Weather weather;

  @JsonProperty("location")
  private Location location;
}
