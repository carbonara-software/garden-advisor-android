package com.carbonara.gardenadvisor.ai;

import static com.carbonara.gardenadvisor.ai.prompt.ConstPrompt.GARDEN_SUGGESTION_PROMPT;

import com.carbonara.gardenadvisor.persistence.entity.GardenWithPlants;
import com.carbonara.gardenadvisor.persistence.entity.Plant;
import java.util.Set;
import java.util.stream.Collectors;

public class GardenGeminiWrapper extends GeminiWrapper {

  private final Set<Plant> plants;

  public GardenGeminiWrapper(GardenWithPlants garden) {
    super(
        garden.getGarden().getLocation().getLat(),
        garden.getGarden().getLocation().getLon(),
        garden.getGarden().getLocation().getLocationName());
    this.plants = garden.getPlants();
  }

  @Override
  public String getGardeningSuggestionPrompt() {

    return weatherString
        + "\n"
        + "Location Name: "
        + locationName
        + "\n"
        + "My plants: "
        + this.plants.stream().map(Plant::getPlantName).collect(Collectors.toSet())
        + "\n"
        + GARDEN_SUGGESTION_PROMPT;
  }
}
