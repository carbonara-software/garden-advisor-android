package com.carbonara.gardenadvisor.ai;

import static com.carbonara.gardenadvisor.ai.prompt.ConstPrompt.GARDEN_SUGGESTION_PROMPT;

import com.carbonara.gardenadvisor.persistence.entity.Garden;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Set;
import lombok.SneakyThrows;

public class PlantsGeminiWrapper extends GeminiWrapper {

  private final Set<String> plants;

  public PlantsGeminiWrapper(Garden garden, Set<String> plants) {
    super(
        garden.getLocation().getLat(),
        garden.getLocation().getLon(),
        garden.getLocation().getLocationName());
    this.plants = plants;
  }

  @SneakyThrows
  @Override
  public String getGardeningSuggestionPrompt() {
    ObjectMapper mapper = new ObjectMapper();
    mapper.setSerializationInclusion(Include.NON_EMPTY);
    return weatherString
        + "\n"
        + "Location Name: "
        + locationName
        + "\n"
        + "My plants: "
        + new ObjectMapper().writeValueAsString(plants)
        + "\n"
        + GARDEN_SUGGESTION_PROMPT;
  }
}
