package com.carbonara.gardenadvisor.ai.funct;

import com.carbonara.gardenadvisor.ai.dto.GeminiWeather;

public interface OnGeminiWrapperWeatherSuccess {

  void getAnswer(GeminiWeather weather);
}
