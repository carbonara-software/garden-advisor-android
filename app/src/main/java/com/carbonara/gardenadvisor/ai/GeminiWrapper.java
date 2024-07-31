package com.carbonara.gardenadvisor.ai;

import static com.carbonara.gardenadvisor.ai.prompt.ConstPrompt.WEATHER_PROMPT;
import static com.carbonara.gardenadvisor.util.ApiKeyUtility.getGeminiApiKey;
import static com.carbonara.gardenadvisor.util.LogUtil.loge;

import androidx.annotation.NonNull;
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
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatterBuilder;
import lombok.Getter;
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
  @Getter private Disposable weatherDisposable;
  @Getter private Disposable gardSuggDisposable;

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

  public void getGeminiResultWeather(
      OnGeminiWrapperWeatherSuccess success, OnGeminiWrapperFail fail) {
    this.success = success;
    this.fail = fail;
    processGeminiRequestMeteo();
  }

  public void getGeminiResultGarden(
      OnGeminiWrapperSuggestionsSuccess successSugg, OnGeminiWrapperFail fail) {
    this.successSugg = successSugg;
    this.fail = fail;
    processGeminiRequestSugg();
  }

  private void processGeminiRequestMeteo() {
    if (weatherString == null || updatedLocation) {
      OkHttpOpenMeteoClient client = new OkHttpOpenMeteoClient(new OkHttpClient());
      OpenMeteoRequest request = OpenMeteoRequest.builder().lon(lon).lat(lat).build();
      try {
        client.getWeatherDataAsync(
            request,
            new Callback() {
              @Override
              public void onFailure(@NonNull Call call, @NonNull IOException e) {
                fail.getAnswerFail(e); // This will be executed on the main thread
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
                  updatedLocation = false;
                }
              }
            });
      } catch (IOException e) {
        fail.getAnswerFail(e); // This will be executed on the main thread
      }
    }
  }

  private void processGeminiRequestSugg() {
    if (weatherString == null || updatedLocation) {
      OkHttpOpenMeteoClient client = new OkHttpOpenMeteoClient(new OkHttpClient());
      OpenMeteoRequest request = OpenMeteoRequest.builder().lon(lon).lat(lat).build();
      try {
        client.getWeatherDataAsync(
            request,
            new Callback() {
              @Override
              public void onFailure(@NonNull Call call, @NonNull IOException e) {
                fail.getAnswerFail(e); // This will be executed on the main thread
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
                  // getGeminiProcessedWeather();
                  getGeminiGardeningSuggestion();
                  updatedLocation = false;
                }
              }
            });
      } catch (IOException e) {
        fail.getAnswerFail(e); // This will be executed on the main thread
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

    weatherDisposable =
        Single.fromCallable(() -> model.generateContent(content).get())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                result -> {
                  String resultText = result.getText();
                  try {
                    ObjectMapper mapper = new ObjectMapper();
                    mapper.registerModule(new JavaTimeModule());
                    GeminiWeather weather = mapper.readValue(resultText, GeminiWeather.class);
                    loge("weather");
                    loge(weather.toString());
                    loge("isweatherDisposable = " + (weatherDisposable == null));
                    loge("isweatherDisposable dispose = " + (weatherDisposable.isDisposed()));
                    success.getAnswer(weather);
                  } catch (JsonProcessingException e) {
                    fail.getAnswerFail(e);
                  }
                },
                throwable -> {
                  fail.getAnswerFail(throwable);
                });
  }

  public abstract String getGardeningSuggestionPrompt();

  private void getGeminiGardeningSuggestion() {
    String prompt = getGardeningSuggestionPrompt();
    loge(prompt);
    //    Content content = new Content.Builder().addText(prompt).build();

    //    gardSuggDisposable =
    //        Single.fromCallable(() -> model.generateContent(content).get())
    //            .subscribeOn(Schedulers.io())
    //            .observeOn(AndroidSchedulers.mainThread())
    //            .subscribe(
    //                result -> {
    //                  String resultText = result.getText();
    //                  try {
    //                    loge("JSON: " + resultText);
    //                    ObjectMapper mapper = new ObjectMapper();
    //                    GeminiGardeningSugg sugg =
    //                        mapper.readValue(resultText, GeminiGardeningSugg.class);
    //                    loge("Suggestion");
    //                    loge(sugg.toString());
    //                    loge("isgardensuggDisposable = " + (gardSuggDisposable == null));
    //                    loge("isgardensuggDisposable dispose = " +
    // (gardSuggDisposable.isDisposed()));
    //                      successSugg.getAnswer(sugg);
    //                  } catch (JsonProcessingException e) {
    //                    loge("Error parsing gemini response:", e);
    //                      fail.getAnswerFail(e);
    //                  } catch (NullPointerException ex) {
    //                    loge("Error parsing gemini response null:", ex);
    //                      fail.getAnswerFail(ex);
    //                  }
    //                },
    //                throwable -> {
    //                    fail.getAnswerFail(throwable);
    //                });
  }
}
