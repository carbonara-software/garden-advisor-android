package com.carbonara.gardenadvisor.ai.task.impl;

import static com.carbonara.gardenadvisor.ai.prompt.ConstPrompt.CAMERA_GARDEN_PROMPT;
import static com.carbonara.gardenadvisor.util.ApiKeyUtility.getGeminiApiKey;
import static com.carbonara.gardenadvisor.util.LogUtil.loge;

import android.graphics.Bitmap;
import com.carbonara.gardenadvisor.ai.dto.GardeningItem;
import com.carbonara.gardenadvisor.ai.task.GeminiSingleOnSubscriber;
import com.carbonara.gardenadvisor.ai.task.GeminiTask;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.ai.client.generativeai.GenerativeModel;
import com.google.ai.client.generativeai.java.GenerativeModelFutures;
import com.google.ai.client.generativeai.type.Content;
import com.google.ai.client.generativeai.type.GenerateContentResponse;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.SingleEmitter;
import java.io.IOException;

public class GeminiGardenCameraTask extends GeminiTask
    implements GeminiSingleOnSubscriber<GardeningItem> {

  private final Bitmap pictureTaken;
  private final long gardenId;

  public GeminiGardenCameraTask(
      float lat, float lon, String locationName, long gardenId, Bitmap pictureTaken) {
    super(lat, lon, locationName);
    this.pictureTaken = pictureTaken;
    this.gardenId = gardenId;
  }

  @Override
  public void subscribe(@NonNull SingleEmitter<GardeningItem> emitter) throws Throwable {
    GenerativeModel gm = new GenerativeModel("gemini-1.5-flash", getGeminiApiKey());
    GenerativeModelFutures model = GenerativeModelFutures.from(gm);
    Content content =
        new Content.Builder().addImage(pictureTaken).addText(getPrompt(weatherData())).build();
    GenerateContentResponse response = model.generateContent(content).get();

    ObjectMapper mapper = new ObjectMapper();
    mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    try {
      loge("eccolo: " + response.getText());
      GardeningItem gardeningItem = mapper.readValue(response.getText(), GardeningItem.class);

      if (!emitter.isDisposed()) {
        emitter.onSuccess(gardeningItem);
      }

    } catch (IOException e) {
      loge("Could not get camera suggestions for garden", e);

      if (!emitter.isDisposed()) {
        emitter.onError(e);
      }
    }
  }

  @Override
  public String getPrompt(String weather) {
    return weather + "\nLocation Name: " + getLocationName() + "\n" + CAMERA_GARDEN_PROMPT;
  }
}
