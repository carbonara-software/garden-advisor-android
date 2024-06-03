package com.carbonara.gardenadvisor.ai.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Day {

  @JsonProperty("date")
  private LocalDate date;

  @JsonProperty("min_temp")
  private float minTemp;

  @JsonProperty("current_temp")
  private float currentTemp;

  @JsonProperty("icon")
  private int icon;

  @JsonProperty("max_temp")
  private float maxTemp;

  @JsonProperty("conditions")
  private String conditions;
}
