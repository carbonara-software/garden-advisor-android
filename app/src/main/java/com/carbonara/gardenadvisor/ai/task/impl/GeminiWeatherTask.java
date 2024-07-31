package com.carbonara.gardenadvisor.ai.task.impl;

import static com.carbonara.gardenadvisor.ai.prompt.ConstPrompt.WEATHER_PROMPT;
import static com.carbonara.gardenadvisor.util.ApiKeyUtility.getGeminiApiKey;

import com.carbonara.gardenadvisor.ai.cache.CachedData;
import com.carbonara.gardenadvisor.ai.dto.GeminiWeather;
import com.carbonara.gardenadvisor.ai.task.GeminiSingleOnSubscriber;
import com.carbonara.gardenadvisor.ai.task.GeminiTask;
import com.carbonara.gardenadvisor.openmeteo.OkHttpOpenMeteoClient;
import com.carbonara.gardenadvisor.openmeteo.request.OpenMeteoRequest;
import com.carbonara.gardenadvisor.util.AppUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.ai.client.generativeai.GenerativeModel;
import com.google.ai.client.generativeai.java.GenerativeModelFutures;
import com.google.ai.client.generativeai.type.Content;
import com.google.ai.client.generativeai.type.GenerateContentResponse;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.SingleEmitter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatterBuilder;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import lombok.Getter;
import okhttp3.OkHttpClient;

@Getter
public class GeminiWeatherTask extends GeminiTask
    implements GeminiSingleOnSubscriber<GeminiWeather> {

  private final String openMeteoResult;

  public GeminiWeatherTask(float lat, float lon, String locationName, String openMeteoResult) {
    super(lat, lon, locationName);
    this.openMeteoResult = openMeteoResult;
  }

  @Override
  public String getPrompt() {
    return openMeteoResult
        + "\nLocation Name: "
        + getLocationName()
        + "\n"
        + WEATHER_PROMPT
        + LocalDate.now()
            .format(new DateTimeFormatterBuilder().appendPattern("yyyy/MM/dd").toFormatter());
  }

  public void subscribe(@NonNull SingleEmitter<GeminiWeather> emitter) {
    try {
      CachedData lastCachedData = AppUtil.getCachedData(getLat(), super.getLon());
      if (lastCachedData != null
          && lastCachedData.getWeather() != null
          && lastCachedData.getLastUpdated().isAfter(LocalDateTime.now().minusHours(6))) {
        if (!emitter.isDisposed()) emitter.onSuccess(lastCachedData.getWeather());
        return;
      }
      OkHttpOpenMeteoClient client = new OkHttpOpenMeteoClient(new OkHttpClient());
      OpenMeteoRequest request =
          OpenMeteoRequest.builder().lon(super.getLon()).lat(super.getLon()).build();
      String weatherString = client.getWeatherData(request);
      Content content = new Content.Builder().addText(weatherString + getPrompt()).build();
      GenerativeModel gm = new GenerativeModel("gemini-1.5-flash", getGeminiApiKey());
      GenerativeModelFutures model = GenerativeModelFutures.from(gm);
      GenerateContentResponse response = model.generateContent(content).get();
      String resultText = response.getText();
      ObjectMapper mapper = new ObjectMapper();
      mapper.registerModule(new JavaTimeModule());
      GeminiWeather weather = mapper.readValue(resultText, GeminiWeather.class);
      if (!emitter.isDisposed()) emitter.onSuccess(weather);
      AppUtil.addCachedData(
          new CachedData(weatherString, getLat(), getLon(), getLocationName(), weather));
    } catch (IOException e) {
      if (!emitter.isDisposed()) emitter.onError(e);
    } catch (CancellationException e) {
      if (!emitter.isDisposed()) emitter.onError(e);
    } catch (ExecutionException e) {
      if (!emitter.isDisposed()) emitter.onError(e);
    } catch (InterruptedException e) {
      if (!emitter.isDisposed()) emitter.onError(e);
    }
  }
}
