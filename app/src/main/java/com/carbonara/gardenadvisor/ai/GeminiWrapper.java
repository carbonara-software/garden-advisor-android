package com.carbonara.gardenadvisor.ai;

import static com.carbonara.gardenadvisor.ai.prompt.ConstPrompt.WEATHER_PROMPT;
import static com.carbonara.gardenadvisor.util.ApiKeyUtility.getGeminiApiKey;
import static com.carbonara.gardenadvisor.util.LogUtil.loge;

import androidx.annotation.NonNull;
import com.carbonara.gardenadvisor.ai.dto.GeminiGardeningSugg;
import com.carbonara.gardenadvisor.ai.dto.GeminiWeather;
import com.carbonara.gardenadvisor.ai.exceptions.GeminiWeatherException;
import com.carbonara.gardenadvisor.ai.funct.OnGeminiWrapperFail;
import com.carbonara.gardenadvisor.ai.funct.OnGeminiWrapperSuggestionsSuccess;
import com.carbonara.gardenadvisor.ai.funct.OnGeminiWrapperWeatherSuccess;
import com.carbonara.gardenadvisor.openmeteo.OkHttpOpenMeteoClient;
import com.carbonara.gardenadvisor.openmeteo.request.OpenMeteoRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.ai.client.generativeai.GenerativeModel;
import com.google.ai.client.generativeai.java.GenerativeModelFutures;
import com.google.ai.client.generativeai.type.Content;
import com.google.ai.client.generativeai.type.GenerateContentResponse;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatterBuilder;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Response;

public abstract class GeminiWrapper {
  protected final GenerativeModelFutures model;
  protected float lat, lon;
  protected String locationName;

  protected OnGeminiWrapperWeatherSuccess success;
  protected OnGeminiWrapperFail fail;
  protected OnGeminiWrapperSuggestionsSuccess successSugg;

  protected boolean updatedLocation;

  protected String weatherString;

  public GeminiWrapper(float lat, float lon, String locationName) {
    this.lat = lat;
    this.lon = lon;
    this.locationName = locationName;
    GenerativeModel gm = new GenerativeModel("gemini-1.5-flash", getGeminiApiKey());
    model = GenerativeModelFutures.from(gm);
    updateLocation(locationName, lat, lon);
  }

  private void updateLocation(String locationName, float lat, float lon) {
    this.lat = lat;
    this.lon = lon;
    this.locationName = locationName;
    updatedLocation = true;
  }

  public void getGeminiResult(
      OnGeminiWrapperWeatherSuccess success,
      OnGeminiWrapperSuggestionsSuccess successSugg,
      OnGeminiWrapperFail fail) {
    this.success = success;
    this.successSugg = successSugg;
    this.fail = fail;
    processGeminiRequest();
  }

  private void processGeminiRequest() {
    if (weatherString == null || updatedLocation) {
      OkHttpOpenMeteoClient client = new OkHttpOpenMeteoClient(new OkHttpClient());
      OpenMeteoRequest request = OpenMeteoRequest.builder().lon(lon).lat(lat).build();
      try {
        client.getWeatherDataAsync(
            request,
            new Callback() {
              @Override
              public void onFailure(@NonNull Call call, @NonNull IOException e) {
                fail.getAnswerFail(e);
              }

              @Override
              public void onResponse(@NonNull Call call, @NonNull Response response)
                  throws IOException {
                if (!response.isSuccessful() || response.body() == null) {
                  loge("Errore response");
                  loge(response.toString());
                  fail.getAnswerFail(
                      new GeminiWeatherException(
                          "Weather not returned...Strange and sad at the same time...."));
                } else {
                  weatherString = response.body().string();
                  getGeminiProcessedWeather();
                  getGeminiGardeningSuggestion();
                  updatedLocation = false;
                }
              }
            });
      } catch (IOException e) {
        fail.getAnswerFail(e);
      }
    }
  }

  private void getGeminiProcessedWeather() {
    String message =
        "\nLocation Name: "
            + locationName
            + "\n"
            + WEATHER_PROMPT
            + LocalDate.now()
                .format(new DateTimeFormatterBuilder().appendPattern("yyyy/MM/dd").toFormatter());
    loge(weatherString + message);
    Content content = new Content.Builder().addText(weatherString + message).build();
    Executor executor = Executors.newSingleThreadExecutor();
    ListenableFuture<GenerateContentResponse> resp = model.generateContent(content);
    Futures.addCallback(
        resp,
        new FutureCallback<GenerateContentResponse>() {
          @Override
          public void onSuccess(GenerateContentResponse result) {
            String resultText = result.getText();
            try {
              //              loge("JSON: " + resultText);
              ObjectMapper mapper = new ObjectMapper();
              mapper.registerModule(new JavaTimeModule()); // To enable LocalDate Parse
              GeminiWeather weather = mapper.readValue(resultText, GeminiWeather.class);
              success.getAnswer(weather);
            } catch (JsonProcessingException e) {
              fail.getAnswerFail(e);
            }
          }

          @Override
          public void onFailure(@NonNull Throwable t) {
            fail.getAnswerFail(t);
          }
        },
        executor);
  }

  public abstract String getGardeningSuggestionPrompt();

  private void getGeminiGardeningSuggestion() {
    String prompt = getGardeningSuggestionPrompt();
    loge(prompt);
    Content content = new Content.Builder().addText(prompt).build();
    Executor executor = Executors.newSingleThreadExecutor();
    ListenableFuture<GenerateContentResponse> resp = model.generateContent(content);
    Futures.addCallback(
        resp,
        new FutureCallback<GenerateContentResponse>() {
          @Override
          public void onSuccess(GenerateContentResponse result) {
            String resultText = result.getText();
            try {
              loge("JSON: " + resultText);
              ObjectMapper mapper = new ObjectMapper();
              GeminiGardeningSugg sugg = mapper.readValue(resultText, GeminiGardeningSugg.class);
              successSugg.getAnswer(sugg);
            } catch (JsonProcessingException e) {
              fail.getAnswerFail(e);
            }
          }

          @Override
          public void onFailure(@NonNull Throwable t) {
            fail.getAnswerFail(t);
          }
        },
        executor);
  }
}
