package com.carbonara.gardenadvisor.util;

import com.carbonara.gardenadvisor.BuildConfig;

public class ApiKeyUtility {

  public static String getGeminiApiKey() {
    return BuildConfig.GEMINI_API_KEY;
  }
}
