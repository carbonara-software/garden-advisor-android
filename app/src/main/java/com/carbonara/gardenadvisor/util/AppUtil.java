package com.carbonara.gardenadvisor.util;

import static com.carbonara.gardenadvisor.util.LogUtil.logd;
import static com.carbonara.gardenadvisor.util.LogUtil.loge;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import com.carbonara.gardenadvisor.ai.cache.HomeCache;
import com.carbonara.gardenadvisor.ai.cache.WeatherCache;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.common.collect.Lists;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;

public class AppUtil {

  private static Set<WeatherCache> cachedData = new TreeSet<>();
  private static HomeCache cacheHome = null;

  public static void addCachedData(WeatherCache data) {
    cachedData.add(data);
  }

  public static void removeCachedData(WeatherCache data) {
    cachedData.remove(data);
  }

  public static WeatherCache getCachedData(float lat, float lon) {
    Optional<WeatherCache> first =
        cachedData.stream()
            .filter(cd -> cd.getLat() == lat && cd.getLon() == lon)
            .findAny();
    return first.orElse(null);
  }

  public static WeatherCache getCachedData(String locationName) {
    Optional<WeatherCache> first =
        cachedData.stream()
            .filter(cd -> cd.getLocationName().equalsIgnoreCase(locationName))
            .findAny();
    return first.orElse(null);
  }

  public static void persistWeather(Context context) throws IOException {
    if(cachedData != null && !cachedData.isEmpty()) {
      ObjectMapper mapper = new ObjectMapper();
      mapper.registerModule(new JavaTimeModule());
      String json = mapper.writeValueAsString(cachedData);
      saveJsonToFile(context, json, "WeatherGA.json");
    }
  }

  public static void addHomeCache(HomeCache cache) {
    cacheHome = cache;
  }

  public static HomeCache getHomeCache() {
    return cacheHome;
  }

  public static void persistHome(Context context) throws IOException {
    if(cacheHome == null) return;
    ObjectMapper mapper = new ObjectMapper();
    String json = mapper.writeValueAsString(cacheHome);
    saveJsonToFile(context,json,"HomeGA.json");
  }

  private static void saveJsonToFile(Context context, String json, String s)
      throws IOException {
    try (FileOutputStream fos = context.openFileOutput(s, Context.MODE_PRIVATE)) {
      fos.write(json.getBytes());
      fos.flush();
      loge("File written successfully: " + s);
      loge("File written successfully JSON: " + json);
    }catch (Exception ex){
      loge("Error writing file: ",ex);
    }
  }

  public static void restoreHome(Context context) throws IOException {
    StringBuilder stringBuilder = new StringBuilder();
    try(FileInputStream fis = context.openFileInput("HomeGA.json")) {
      InputStreamReader isr = new InputStreamReader(fis);
      BufferedReader bufferedReader = new BufferedReader(isr);
      String line;
      while ((line = bufferedReader.readLine()) != null) {
        stringBuilder.append(line);
      }
    }catch (IOException ex){
      throw ex;
    }
    String jsonString = stringBuilder.toString();
    loge("JSON Home: " + jsonString);
    cacheHome = new ObjectMapper().registerModule(new JavaTimeModule())
        .readValue(jsonString, HomeCache.class);

  }

  public static void restoreWeather(Context context) throws IOException {
    StringBuilder stringBuilder = new StringBuilder();
    try(FileInputStream fis = context.openFileInput("WeatherGA.json")) {
      InputStreamReader isr = new InputStreamReader(fis);
      BufferedReader bufferedReader = new BufferedReader(isr);
      String line;
      while ((line = bufferedReader.readLine()) != null) {
        stringBuilder.append(line);
      }
    }
    String jsonString = stringBuilder.toString();
    loge("JSON Weather: " + jsonString);
    WeatherCache[] cachedDataTemp = new ObjectMapper().registerModule(new JavaTimeModule()).readValue(jsonString, WeatherCache[].class);
    cachedData = new TreeSet<>(Arrays.asList(cachedDataTemp));
  }

  public static boolean isNetworkAvailable(Context context) {
    ConnectivityManager connectivityManager =
        (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
    return activeNetworkInfo != null && activeNetworkInfo.isConnected();
  }

  public static Address getCurrentLocationName(Context context, double latitude, double longitude) {
    Geocoder geocoder = new Geocoder(context, Locale.getDefault());
    try {
      List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
      if (addresses != null && !addresses.isEmpty()) {
        return addresses.get(0);
      }
    } catch (IOException e) {
      loge("getCurrentLocationName Geocoder failed: ", e);
    }
    return null;
  }

  public static Address getCurrentLocationLatLon(Context context, String cityName) {
    Geocoder geocoder = new Geocoder(context, Locale.getDefault());
    try {
      List<Address> addresses = geocoder.getFromLocationName(cityName, 1);
      if (addresses != null && !addresses.isEmpty()) {
        return addresses.get(0);
      } else {
        logd("No location found for the provided city name.");
      }
    } catch (IOException e) {
      loge("getCurrentLocationLatLon Geocoder failed: ", e);
    }
    return null;
  }
}
