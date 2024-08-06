package com.carbonara.gardenadvisor.ai.task.impl;

import static com.carbonara.gardenadvisor.ai.prompt.ConstPrompt.CAMERA_SUGGESTION_PROMPT;
import static com.carbonara.gardenadvisor.util.ApiKeyUtility.getGeminiApiKey;
import static com.carbonara.gardenadvisor.util.AppUtil.writeBitmapToFile;
import static com.carbonara.gardenadvisor.util.LogUtil.loge;
import static com.google.common.base.Strings.isNullOrEmpty;

import android.content.Context;
import android.graphics.Bitmap;
import com.carbonara.gardenadvisor.ai.dto.GeminiCameraSuggestion;
import com.carbonara.gardenadvisor.ai.task.GeminiSingleOnSubscriber;
import com.carbonara.gardenadvisor.util.AppCache;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.ai.client.generativeai.GenerativeModel;
import com.google.ai.client.generativeai.java.GenerativeModelFutures;
import com.google.ai.client.generativeai.type.Content;
import com.google.ai.client.generativeai.type.GenerateContentResponse;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.SingleEmitter;
import java.io.IOException;

public class GeminiCameraTask implements GeminiSingleOnSubscriber<GeminiCameraSuggestion> {

  private final Bitmap pictureTaken;
  private final Context context;

  public GeminiCameraTask(Bitmap pictureTaken, Context context) {
    this.pictureTaken = pictureTaken;
    this.context = context;
  }

  @Override
  public void subscribe(@NonNull SingleEmitter<GeminiCameraSuggestion> emitter) throws Throwable {
    GenerativeModel gm = new GenerativeModel("gemini-1.5-flash", getGeminiApiKey());
    GenerativeModelFutures model = GenerativeModelFutures.from(gm);
    Content content =
        new Content.Builder().addImage(pictureTaken).addText(CAMERA_SUGGESTION_PROMPT).build();
    GenerateContentResponse response = model.generateContent(content).get();

    ObjectMapper mapper = new ObjectMapper();
    mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    try {
      GeminiCameraSuggestion cameraSuggestion =
          mapper.readValue(response.getText(), GeminiCameraSuggestion.class);

      if (isNullOrEmpty(cameraSuggestion.getStatus())
          || isNullOrEmpty(cameraSuggestion.getName())
          || isNullOrEmpty(cameraSuggestion.getScientificName()))
        throw new IOException("Data is missing");

      if (!emitter.isDisposed()) {
        emitter.onSuccess(cameraSuggestion);
        final String path = cameraSuggestion.getCachedId();
        writeBitmapToFile(context, pictureTaken, path);
        AppCache.getInstance().persistCameraCache(context);
      }

    } catch (IOException e) {
      loge("Could not get camera suggestions", e);

      if (!emitter.isDisposed()) {
        emitter.onError(e);
      }
    }
  }
}
