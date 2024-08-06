package com.carbonara.gardenadvisor.util;

import static com.carbonara.gardenadvisor.util.AppUtil.readFileContentAsString;
import static com.carbonara.gardenadvisor.util.AppUtil.writeToFile;
import static com.carbonara.gardenadvisor.util.LogUtil.logd;
import static com.carbonara.gardenadvisor.util.LogUtil.loge;

import android.content.Context;
import com.carbonara.gardenadvisor.ai.cache.HomeCache;
import com.carbonara.gardenadvisor.ai.cache.WeatherCache;
import com.carbonara.gardenadvisor.ai.dto.GeminiCameraSuggestion;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import lombok.Getter;

public class AppCache {

  private final Set<WeatherCache> cachedWeather = new TreeSet<>();
  private HomeCache cachedHome = null;

  @Getter
  private final Map<String, GeminiCameraSuggestion> cachedCameraSuggestions = new HashMap<>();

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
    cachedCameraSuggestions.put(cameraSuggestion.getCachedId(), cameraSuggestion);
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
      writeToFile(context, json.getBytes(), "WeatherGA.json");
    }
  }

  public void addHomeCache(HomeCache cache) {
    cachedHome = cache;
  }

  public HomeCache getHomeCache() {
    return cachedHome;
  }

  public synchronized void persistCameraCache(Context context) throws IOException {
    if (cachedCameraSuggestions.isEmpty()) return;
    ObjectMapper mapper = new ObjectMapper();
    String json = mapper.writeValueAsString(cachedCameraSuggestions);
    writeToFile(context, json.getBytes(), "Camera.json");
  }

  public void restoreCameraCache(Context context) throws IOException {
    String jsonString = readFileContentAsString(context, "Camera.json");
    logd("JSON CameraCache: " + jsonString);

    if (!cachedCameraSuggestions.isEmpty()) {
      cachedCameraSuggestions.clear();
    }

    Map<String, GeminiCameraSuggestion> retrieved =
        new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .readValue(jsonString, new TypeReference<Map<String, GeminiCameraSuggestion>>() {});

    cachedCameraSuggestions.putAll(retrieved);
  }

  public synchronized void persistHome(Context context) throws IOException {
    if (cachedHome == null) return;
    ObjectMapper mapper = new ObjectMapper();
    String json = mapper.writeValueAsString(cachedHome);
    writeToFile(context, json.getBytes(), "HomeGA.json");
  }

  public void restoreHome(Context context) throws IOException {
    String jsonString = readFileContentAsString(context, "HomeGA.json");
    loge("JSON Home: " + jsonString);

    cachedHome =
        new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .readValue(jsonString, HomeCache.class);
  }

  public synchronized void restoreWeather(Context context) throws IOException {
    String jsonString = readFileContentAsString(context, "WeatherGA.json");
    loge("JSON Weather: " + jsonString);

    WeatherCache[] deserialized =
        new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .readValue(jsonString, WeatherCache[].class);
    cachedWeather.addAll(Arrays.asList(deserialized));
  }
}
