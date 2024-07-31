package com.carbonara.gardenadvisor.util.task;

import static com.carbonara.gardenadvisor.util.ApiKeyUtility.getGeminiApiKey;
import static com.carbonara.gardenadvisor.util.LogUtil.loge;

import com.carbonara.gardenadvisor.ai.dto.GeminiGardeningSugg;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.ai.client.generativeai.GenerativeModel;
import com.google.ai.client.generativeai.java.GenerativeModelFutures;
import com.google.ai.client.generativeai.type.Content;
import com.google.ai.client.generativeai.type.GenerateContentResponse;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.SingleEmitter;
import io.reactivex.rxjava3.core.SingleOnSubscribe;

public class GetGeminiResultSuggestions implements SingleOnSubscribe<GeminiGardeningSugg> {

  private final String prompt;

  public GetGeminiResultSuggestions(String prompt) {
    this.prompt = prompt;
  }

  @Override
  public void subscribe(@NonNull SingleEmitter<GeminiGardeningSugg> emitter) throws Throwable {
    GenerativeModel gm = new GenerativeModel("gemini-1.5-flash", getGeminiApiKey());
    GenerativeModelFutures model = GenerativeModelFutures.from(gm);
    Content content = new Content.Builder().addText(prompt).build();
    GenerateContentResponse response = model.generateContent(content).get();
    String resultText = response.getText();
    try {
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
    }
  }
}
