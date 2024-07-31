package com.carbonara.gardenadvisor.ai.task.impl;

import static com.carbonara.gardenadvisor.ai.prompt.ConstPrompt.GARDEN_SUGGESTION_PROMPT;

import com.carbonara.gardenadvisor.ai.dto.GeminiGardeningSugg;
import com.carbonara.gardenadvisor.ai.task.GeminiSingleOnSubscriber;
import com.carbonara.gardenadvisor.ai.task.GeminiWeatherDependentTask;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.SingleEmitter;
import java.util.Set;

public class GeminiGardenTask extends GeminiWeatherDependentTask
    implements GeminiSingleOnSubscriber<GeminiGardeningSugg> {

  private final Set<String> plants;

  public GeminiGardenTask(
      Float lat, Float lon, String locationName, String openMeteoResultString, Set<String> plants) {
    super(lat, lon, locationName, openMeteoResultString);
    this.plants = plants;
  }

  @Override
  public String getPrompt() throws JsonProcessingException {
    return getOpenMeteoResultString()
        + "\n"
        + "Location Name: "
        + getLocationName()
        + "\n"
        + "My plants: "
        + new ObjectMapper().writeValueAsString(plants)
        + "\n"
        + GARDEN_SUGGESTION_PROMPT;
  }

  @Override
  public void subscribe(@NonNull SingleEmitter<GeminiGardeningSugg> emitter) {
    // TODO implement subscribe
  }
}
