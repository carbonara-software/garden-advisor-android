package com.carbonara.gardenadvisor.ai.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Weather {

  @JsonProperty("forecast")
  private List<Day> forecast;

  @JsonProperty("todayForecast")
  private Day todayForecast;
}
