package com.carbonara.gardenadvisor.util;

import static com.carbonara.gardenadvisor.util.LogUtil.loge;

import android.content.Context;
import com.carbonara.gardenadvisor.ai.cache.HomeCache;
import com.carbonara.gardenadvisor.ai.cache.WeatherCache;
import com.carbonara.gardenadvisor.ai.dto.GeminiCameraSuggestion;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import lombok.Getter;

public class AppCache {

  private final Set<WeatherCache> cachedWeather = new TreeSet<>();
  private HomeCache cachedHome = null;

  @Getter private final List<GeminiCameraSuggestion> cachedCameraSuggestions = new ArrayList<>();

  @Getter private static final AppCache instance = new AppCache();

  private AppCache() {}

  public synchronized boolean isWeatherPresent() {
    return !cachedWeather.isEmpty();
  }

  public synchronized boolean isHomePresent() {
    return cachedHome != null;
  }

  public synchronized boolean isCameraSuggestionPresent() {
    return !cachedCameraSuggestions.isEmpty();
  }

  public synchronized void addCameraSuggestion(GeminiCameraSuggestion cameraSuggestion) {
    cachedCameraSuggestions.add(cameraSuggestion);
  }

  public synchronized void addCachedData(WeatherCache data) {
    cachedWeather.add(data);
  }

  public synchronized void removeCachedData(WeatherCache data) {
    cachedWeather.remove(data);
  }

  public synchronized WeatherCache getCachedWeather(float lat, float lon) {
    Optional<WeatherCache> first =
        cachedWeather.stream().filter(cd -> cd.getLat() == lat && cd.getLon() == lon).findAny();
    return first.orElse(null);
  }

  public synchronized WeatherCache getCachedWeather(String locationName) {
    Optional<WeatherCache> first =
        cachedWeather.stream()
            .filter(cd -> cd.getLocationName().equalsIgnoreCase(locationName))
            .findAny();
    return first.orElse(null);
  }

  public synchronized void persistWeather(Context context) throws IOException {
    if (!cachedWeather.isEmpty()) {
      ObjectMapper mapper = new ObjectMapper();
      mapper.registerModule(new JavaTimeModule());
      String json = mapper.writeValueAsString(cachedWeather);
      saveJsonToFile(context, json, "WeatherGA.json");
    }
  }

  public void addHomeCache(HomeCache cache) {
    cachedHome = cache;
  }

  public HomeCache getHomeCache() {
    return cachedHome;
  }

  public synchronized void persistHome(Context context) throws IOException {
    if (cachedHome == null) return;
    ObjectMapper mapper = new ObjectMapper();
    String json = mapper.writeValueAsString(cachedHome);
    saveJsonToFile(context, json, "HomeGA.json");
  }

  private void saveJsonToFile(Context context, String json, String content) throws IOException {
    try (FileOutputStream fos = context.openFileOutput(content, Context.MODE_PRIVATE)) {
      fos.write(json.getBytes());
      fos.flush();
      loge("File written successfully JSON: " + json);
    }
  }

  public void restoreHome(Context context) throws IOException {
    String jsonString = readJsonContent(context, "HomeGA.json");
    loge("JSON Home: " + jsonString);

    cachedHome =
        new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .readValue(jsonString, HomeCache.class);
  }

  public synchronized void restoreWeather(Context context) throws IOException {
    String jsonString = readJsonContent(context, "WeatherGA.json");
    loge("JSON Weather: " + jsonString);

    WeatherCache[] deserialized =
        new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .readValue(jsonString, WeatherCache[].class);
    cachedWeather.addAll(Arrays.asList(deserialized));
  }

  private String readJsonContent(Context context, String path) throws IOException {
    StringBuilder stringBuilder = new StringBuilder();
    try (FileInputStream fis = context.openFileInput(path)) {
      InputStreamReader isr = new InputStreamReader(fis);
      BufferedReader bufferedReader = new BufferedReader(isr);
      String line;
      while ((line = bufferedReader.readLine()) != null) {
        stringBuilder.append(line);
      }
    }

    return stringBuilder.toString();
  }
}
