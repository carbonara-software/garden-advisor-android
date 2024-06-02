package com.carbonara.gardenadvisor.ai.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class GeminiGardeningSugg {

  @JsonProperty("flowers")
  private List<GardeningItem> flowers;

  @JsonProperty("fruits")
  private List<GardeningItem> fruits;

  @JsonProperty("vegetables")
  private List<GardeningItem> vegetables;
}
