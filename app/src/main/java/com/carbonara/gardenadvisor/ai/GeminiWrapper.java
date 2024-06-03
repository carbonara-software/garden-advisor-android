package com.carbonara.gardenadvisor.ai;

import static com.carbonara.gardenadvisor.ai.prompt.ConstPrompt.RETURN_SUGGESTIONS;
import static com.carbonara.gardenadvisor.ai.prompt.ConstPrompt.RETURN_WEATHER;
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

public class GeminiWrapper {

  private static GeminiWrapper instance;

  private final GenerativeModelFutures model;

  private float lat, lon;
  private String locationName;
  private String weatherString;

  private boolean updatedLocation;

  private OnGeminiWrapperWeatherSuccess success;
  private OnGeminiWrapperFail fail;
  private OnGeminiWrapperSuggestionsSuccess successSugg;

  public static GeminiWrapper getInstance(String locationName, float lat, float lon) {
    if (instance == null) instance = new GeminiWrapper(lat, lon, locationName);
    if (instance.lat != lat || instance.lon != lon) {
      instance.updateLocation(locationName, lat, lon);
    }
    return instance;
  }

  private GeminiWrapper(float lat, float lon, String locationName) {
    this.lat = lat;
    this.lon = lon;
    this.locationName = locationName;
    loge("Chiave <==> " + getGeminiApiKey());
    GenerativeModel gm =
        new GenerativeModel(
            "gemini-1.5-flash", getGeminiApiKey() // TODO: must find a safer place
            );
    model = GenerativeModelFutures.from(gm);
  }

  private void updateLocation(String locationName, float lat, float lon) {
    this.lat = lat;
    this.lon = lon;
    this.locationName = locationName;
    updatedLocation = true;
  }

  private void getWeatherGeminized() {
    String message =
        "\nLocation Name: "
            + locationName
            + "\n"
            + RETURN_WEATHER
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

  private void getWeather() {
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
                  getWeatherGeminized();
                  getGardeningSuggGeminized();
                  updatedLocation = false;
                }
              }
            });
      } catch (IOException e) {
        fail.getAnswerFail(e);
      }
    }
  }

  public void getWeather(
      OnGeminiWrapperWeatherSuccess success,
      OnGeminiWrapperSuggestionsSuccess successSugg,
      OnGeminiWrapperFail fail) {
    this.success = success;
    this.successSugg = successSugg;
    this.fail = fail;
    getWeather();
  }

  private void getGardeningSuggGeminized() {
    String message = "\nLocation Name: " + locationName + "\n" + RETURN_SUGGESTIONS;
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

  //  public void getGardeningSuggestions(
  //      OnGeminiWrapperSuggestionsSuccess success, OnGeminiWrapperFail fail) {
  //    this.successSugg = success;
  //    this.fail = fail;
  //    getWeather();
  //
  //  }
}
