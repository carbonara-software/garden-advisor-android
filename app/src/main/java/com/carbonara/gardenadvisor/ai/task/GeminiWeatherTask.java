package com.carbonara.gardenadvisor.ai.task;

import static com.carbonara.gardenadvisor.util.ApiKeyUtility.getGeminiApiKey;

import com.carbonara.gardenadvisor.ai.dto.CachedData;
import com.carbonara.gardenadvisor.ai.dto.GeminiWeather;
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
import io.reactivex.rxjava3.core.SingleOnSubscribe;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import okhttp3.OkHttpClient;

public class GeminiWeatherTask implements SingleOnSubscribe<GeminiWeather> {

  private final String prompt;
  private final float lat;
  private final float lon;
  private final String locationName;

  public GeminiWeatherTask(String prompt, float lat, float lon, String locationName) {
    this.prompt = prompt;
    this.lat = lat;
    this.lon = lon;
    this.locationName = locationName;
  }

  @Override
  public void subscribe(@NonNull SingleEmitter<GeminiWeather> emitter) {
    try {
      CachedData lastCachedData = AppUtil.getCachedData(lat, lon);
      if (lastCachedData != null
          && lastCachedData.getWeather() != null
          && lastCachedData.getLastUpdated().isAfter(LocalDateTime.now().minusHours(6))) {
        if (!emitter.isDisposed()) emitter.onSuccess(lastCachedData.getWeather());
        return;
      }
      OkHttpOpenMeteoClient client = new OkHttpOpenMeteoClient(new OkHttpClient());
      OpenMeteoRequest request = OpenMeteoRequest.builder().lon(lon).lat(lat).build();
      String weatherString = client.getWeatherData(request);
      Content content = new Content.Builder().addText(weatherString + prompt).build();
      GenerativeModel gm = new GenerativeModel("gemini-1.5-flash", getGeminiApiKey());
      GenerativeModelFutures model = GenerativeModelFutures.from(gm);
      GenerateContentResponse response = model.generateContent(content).get();
      String resultText = response.getText();
      ObjectMapper mapper = new ObjectMapper();
      mapper.registerModule(new JavaTimeModule());
      GeminiWeather weather = mapper.readValue(resultText, GeminiWeather.class);
      if (!emitter.isDisposed()) emitter.onSuccess(weather);
      AppUtil.addCachedData(new CachedData(weatherString, lat, lon, locationName, weather));
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
