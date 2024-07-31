package com.carbonara.gardenadvisor.ai.task;

import static com.carbonara.gardenadvisor.util.ApiKeyUtility.getGeminiApiKey;

import com.carbonara.gardenadvisor.ai.cache.CachedData;
import com.carbonara.gardenadvisor.ai.dto.GeminiWeather;
import com.carbonara.gardenadvisor.openmeteo.OkHttpOpenMeteoClient;
import com.carbonara.gardenadvisor.openmeteo.request.OpenMeteoRequest;
import com.carbonara.gardenadvisor.util.AppUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.ai.client.generativeai.GenerativeModel;
import com.google.ai.client.generativeai.java.GenerativeModelFutures;
import com.google.ai.client.generativeai.type.Content;
import com.google.ai.client.generativeai.type.GenerateContentResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import lombok.Getter;
import okhttp3.OkHttpClient;

@Getter
public abstract class GeminiTask {

  private final float lat, lon;
  private final String locationName;

  protected GeminiTask(float lat, float lon, String locationName) {
    this.lat = lat;
    this.lon = lon;
    this.locationName = locationName;
  }

  public abstract String getPrompt(String weather) throws JsonProcessingException;

  public String weatherData() throws IOException{
    CachedData data = AppUtil.getCachedData(locationName);
    if(data != null){
      return data.getWeatherString();
    }else{
        OkHttpOpenMeteoClient client = new OkHttpOpenMeteoClient(new OkHttpClient());
        OpenMeteoRequest request =
            OpenMeteoRequest.builder().lon(lon).lat(lat).build();
      return client.getWeatherData(request);
    }
  }

  public GeminiWeather geminiWeather() throws IOException{
    CachedData data = AppUtil.getCachedData(locationName);
    if(data != null){
      return data.getWeather();
    }else{
      return null;
    }
  }
}
