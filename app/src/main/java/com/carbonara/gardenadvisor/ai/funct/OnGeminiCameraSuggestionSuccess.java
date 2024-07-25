package com.carbonara.gardenadvisor.ai.funct;

import com.carbonara.gardenadvisor.ai.dto.GeminiCameraSuggestion;

public interface OnGeminiCameraSuggestionSuccess {

  void getAnswer(GeminiCameraSuggestion geminiCameraSuggestion);
}
