package com.carbonara.gardenadvisor.util;

import static com.carbonara.gardenadvisor.util.LogUtil.logd;
import static com.carbonara.gardenadvisor.util.LogUtil.loge;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Collections;
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

  public static String extractStringList(List<String> stringList) {
    try {
      return new ObjectMapper().writeValueAsString(stringList);
    } catch (Exception e) {
      return "[]";
    }
  }

  public static List<String> extractStringList(String string) {
    try {
      return new ObjectMapper().readValue(string, new TypeReference<List<String>>() {});
    } catch (Exception e) {
      return Collections.emptyList();
    }
  }

  public static void writeToFile(Context context, byte[] content, String path) throws IOException {
    try (FileOutputStream fos = context.openFileOutput(path, Context.MODE_PRIVATE)) {
      fos.write(content);
      fos.flush();
      logd(
          String.format(
              "File %s written successfully contents: %s", path, Arrays.toString(content)));
    }
  }

  public static byte[] bitmapToByteArray(Bitmap bitmap) {
    int size = bitmap.getRowBytes() * bitmap.getHeight();
    ByteBuffer byteBuffer = ByteBuffer.allocate(size);
    bitmap.copyPixelsToBuffer(byteBuffer);
    return byteBuffer.array();
  }

  public static Bitmap byteArrayToBitmap(byte[] bitmapArray) {
    return BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.length);
  }
}
