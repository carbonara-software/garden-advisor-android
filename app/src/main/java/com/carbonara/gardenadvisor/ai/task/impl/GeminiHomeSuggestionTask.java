package com.carbonara.gardenadvisor.ai.task.impl;

import static com.carbonara.gardenadvisor.ai.prompt.ConstPrompt.HOME_SUGGESTION_PROMPT;
import static com.carbonara.gardenadvisor.util.ApiKeyUtility.getGeminiApiKey;
import static com.carbonara.gardenadvisor.util.LogUtil.loge;

import com.carbonara.gardenadvisor.ai.dto.GeminiGardeningSugg;
import com.carbonara.gardenadvisor.ai.task.GeminiSingleOnSubscriber;
import com.carbonara.gardenadvisor.ai.task.GeminiTask;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.ai.client.generativeai.GenerativeModel;
import com.google.ai.client.generativeai.java.GenerativeModelFutures;
import com.google.ai.client.generativeai.type.Content;
import com.google.ai.client.generativeai.type.GenerateContentResponse;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.SingleEmitter;

public class GeminiHomeSuggestionTask extends GeminiTask
    implements GeminiSingleOnSubscriber<GeminiGardeningSugg> {

  public GeminiHomeSuggestionTask(Float lat, Float lon, String locationName) {
    super(lat, lon, locationName);
  }

  @Override
  public void subscribe(@NonNull SingleEmitter<GeminiGardeningSugg> emitter) throws Throwable {
    try {
      GenerativeModel gm = new GenerativeModel("gemini-1.5-flash", getGeminiApiKey());
      GenerativeModelFutures model = GenerativeModelFutures.from(gm);
      Content content = new Content.Builder().addText(getPrompt(weatherData())).build();
      GenerateContentResponse response = model.generateContent(content).get();
      String resultText = response.getText();

      loge("JSON: " + resultText);
      ObjectMapper mapper = new ObjectMapper();
      GeminiGardeningSugg sugg = mapper.readValue(resultText, GeminiGardeningSugg.class);
      if (!emitter.isDisposed()) emitter.onSuccess(sugg);
    } catch (JsonProcessingException e) {
      loge("Error parsing gemini response:", e);
      if (!emitter.isDisposed()) emitter.onError(e);
    } catch (NullPointerException ex) {
      loge("Error parsing gemini response null:", ex);
      if (!emitter.isDisposed()) emitter.onError(ex);
    } catch (InterruptedException ex) {
      loge("Error parsing gemini response undeliverable:", ex);
      // if (!emitter.isDisposed()) emitter.onError(ex);
    }
  }

  @Override
  public String getPrompt(String weather) {
    return weather + "\nLocation Name: " + getLocationName() + "\n" + HOME_SUGGESTION_PROMPT;
  }
}
