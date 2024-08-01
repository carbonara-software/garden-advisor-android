package com.carbonara.gardenadvisor.ai.task;

import com.carbonara.gardenadvisor.ai.cache.CachedData;
import com.carbonara.gardenadvisor.ai.dto.GeminiWeather;
import com.carbonara.gardenadvisor.openmeteo.OkHttpOpenMeteoClient;
import com.carbonara.gardenadvisor.openmeteo.request.OpenMeteoRequest;
import com.carbonara.gardenadvisor.util.AppUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.io.IOException;
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

  public String weatherData() throws IOException {
    CachedData data = AppUtil.getCachedData(locationName);
    if (data != null) {
      return data.getWeatherString();
    } else {
      OkHttpOpenMeteoClient client = new OkHttpOpenMeteoClient(new OkHttpClient());
      OpenMeteoRequest request = OpenMeteoRequest.builder().lon(lon).lat(lat).build();
      return client.getWeatherData(request);
    }
  }

  public GeminiWeather geminiWeather() throws IOException {
    CachedData data = AppUtil.getCachedData(locationName);
    return (data != null) ? data.getWeather() : null;
  }
}
