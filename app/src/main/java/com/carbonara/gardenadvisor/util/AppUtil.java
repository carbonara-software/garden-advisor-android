package com.carbonara.gardenadvisor.util;

import static com.carbonara.gardenadvisor.util.LogUtil.logd;
import static com.carbonara.gardenadvisor.util.LogUtil.loge;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import lombok.Getter;

public class AppUtil {

  @Getter private static final AppCache appCache = AppCache.getInstance();

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
