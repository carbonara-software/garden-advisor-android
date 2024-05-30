package com.alexinnocenzi.gardenadvisor.util;

import com.alexinnocenzi.gardenadvisor.BuildConfig;

public class ApiKeyUtility {

    public static String getGeminiApiKey() {
        return BuildConfig.GEMINI_API_KEY;
    }

}
