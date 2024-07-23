package com.carbonara.gardenadvisor.ai;

import static com.carbonara.gardenadvisor.ai.prompt.ConstPrompt.GARDEN_SUGGESTION_PROMPT;

import com.carbonara.gardenadvisor.persistence.entity.GardenWithPlants;
import com.carbonara.gardenadvisor.persistence.entity.Plant;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Set;
import lombok.SneakyThrows;

public class GardenGeminiWrapper extends GeminiWrapper {

  private final Set<Plant> plants;

  public GardenGeminiWrapper(GardenWithPlants garden) {
    super(
        garden.getGarden().getLocation().getLat(),
        garden.getGarden().getLocation().getLon(),
        garden.getGarden().getLocation().getLocationName());
    this.plants = garden.getPlants();
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
