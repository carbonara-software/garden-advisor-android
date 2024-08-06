package com.carbonara.gardenadvisor.ai.dto;

import android.graphics.Bitmap;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CachedCameraSuggestion implements GeminiResult {

  private final GeminiCameraSuggestion cameraSuggestion;
  private final Bitmap bitmap;
}
