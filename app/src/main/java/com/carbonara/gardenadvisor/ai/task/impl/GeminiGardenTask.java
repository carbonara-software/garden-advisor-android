package com.carbonara.gardenadvisor.ai.task.impl;

import static com.carbonara.gardenadvisor.ai.prompt.ConstPrompt.GARDEN_SUGGESTION_PROMPT;
import static com.carbonara.gardenadvisor.util.ApiKeyUtility.getGeminiApiKey;

import com.carbonara.gardenadvisor.ai.dto.GeminiGardeningSugg;
import com.carbonara.gardenadvisor.ai.task.GeminiSingleOnSubscriber;
import com.carbonara.gardenadvisor.ai.task.GeminiTask;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.ai.client.generativeai.GenerativeModel;
import com.google.ai.client.generativeai.java.GenerativeModelFutures;
import com.google.ai.client.generativeai.type.Content;
import com.google.ai.client.generativeai.type.GenerateContentResponse;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.SingleEmitter;
import java.io.IOException;
import java.util.Set;
import java.util.concurrent.ExecutionException;

public class GeminiGardenTask extends GeminiTask
    implements GeminiSingleOnSubscriber<GeminiGardeningSugg> {

  private final Set<String> plants;

  public GeminiGardenTask(
      Float lat, Float lon, String locationName, Set<String> plants) {
    super(lat, lon, locationName);
    this.plants = plants;
  }

  @Override
  public String getPrompt(String meteo) throws JsonProcessingException {
    return meteo
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
    try {
      String weatherString = weatherData();
      Content content = new Content.Builder().addText(getPrompt(weatherString)).build();
      GenerativeModel gm = new GenerativeModel("gemini-1.5-flash", getGeminiApiKey());
      GenerativeModelFutures model = GenerativeModelFutures.from(gm);
      GenerateContentResponse response = model.generateContent(content).get();
      String resultText = response.getText();
      ObjectMapper mapper = new ObjectMapper();
      mapper.registerModule(new JavaTimeModule());
      GeminiGardeningSugg sugg = mapper.readValue(resultText, GeminiGardeningSugg.class);
      if (!emitter.isDisposed()) emitter.onSuccess(sugg);
    } catch (IOException | ExecutionException | InterruptedException e) {
      if (!emitter.isDisposed()) emitter.onError(e);
    }
  }
}
