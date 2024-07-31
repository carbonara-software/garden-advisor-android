package com.carbonara.gardenadvisor.util;

import static com.carbonara.gardenadvisor.util.LogUtil.loge;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import com.carbonara.gardenadvisor.ai.dto.CachedData;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

public class AppUtil {

  private static List<CachedData> cachedData = new ArrayList<>();

  public static void addCachedData(CachedData data) {
    cachedData.add(data);
  }

  public static void removeCachedData(CachedData data) {
    cachedData.remove(data);
  }

  public static CachedData getCachedData(float lat, float lon) {
    Optional<CachedData> first =
        cachedData.stream()
            .filter(cd -> cd.getLat() == lat && cd.getLon() == lon)
            .max(CachedData::compareTo);
    return first.orElse(null);
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
      loge("Geocoder failed: " + e.getMessage());
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
        loge("No location found for the provided city name.");
      }
    } catch (IOException e) {
      loge("Geocoder failed: " + e.getMessage());
    }
    return null;
  }
}
