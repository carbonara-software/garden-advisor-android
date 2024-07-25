package com.carbonara.gardenadvisor.ai;

import static com.carbonara.gardenadvisor.ai.prompt.ConstPrompt.CAMERA_SUGGESTION_PROMPT;
import static com.carbonara.gardenadvisor.util.ApiKeyUtility.getGeminiApiKey;

import android.graphics.Bitmap;
import com.carbonara.gardenadvisor.ai.dto.GeminiCameraSuggestion;
import com.carbonara.gardenadvisor.ai.funct.OnGeminiCameraSuggestionFail;
import com.carbonara.gardenadvisor.ai.funct.OnGeminiCameraSuggestionSuccess;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.ai.client.generativeai.GenerativeModel;
import com.google.ai.client.generativeai.java.GenerativeModelFutures;
import com.google.ai.client.generativeai.type.Content;
import com.google.ai.client.generativeai.type.GenerateContentResponse;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class GeminiCameraSuggestionWrapper {

  private final GenerativeModelFutures model;

  public GeminiCameraSuggestionWrapper() {
    GenerativeModel gm = new GenerativeModel("gemini-1.5-flash", getGeminiApiKey());
    model = GenerativeModelFutures.from(gm);
  }

  public void getGeminiResult(
      Bitmap imageBitmap,
      OnGeminiCameraSuggestionSuccess onSuccess,
      OnGeminiCameraSuggestionFail onFail) {

    Content content =
        new Content.Builder().addImage(imageBitmap).addText(CAMERA_SUGGESTION_PROMPT).build();

    Executor executor = Executors.newSingleThreadExecutor();
    ListenableFuture<GenerateContentResponse> resp = model.generateContent(content);
    Futures.addCallback(
        resp,
        new FutureCallback<GenerateContentResponse>() {
          @Override
          public void onSuccess(GenerateContentResponse result) {
            ObjectMapper mapper = new ObjectMapper();
            mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            try {
              GeminiCameraSuggestion cameraSuggestion =
                  mapper.readValue(result.getText(), GeminiCameraSuggestion.class);
              onSuccess.getAnswer(cameraSuggestion);
            } catch (JsonProcessingException e) {
              onFail.getAnswerFail(e);
            }
          }

          @Override
          public void onFailure(Throwable t) {
            onFail.getAnswerFail(t);
          }
        },
        executor);
  }
}
