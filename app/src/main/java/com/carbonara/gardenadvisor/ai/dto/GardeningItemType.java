package com.carbonara.gardenadvisor.ai.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum GardeningItemType {
  @JsonProperty("flower")
  FLOWER,
  @JsonProperty("vegetable")
  VEGETABLE,
  @JsonProperty("fruit")
  FRUIT
}
