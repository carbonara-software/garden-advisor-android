package com.carbonara.gardenadvisor.ai.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class GardeningItem {

  @JsonProperty("cautions")
  private List<String> cautions;

  @JsonProperty("name")
  private String name;

  //  @JsonProperty("imageUrl")
  //  private String imageUrl;

  @JsonProperty("positives")
  private List<String> positives;

  @JsonProperty("suggestions")
  private List<String> suggestions;

  @JsonProperty("recommended")
  private boolean recommended;
}
