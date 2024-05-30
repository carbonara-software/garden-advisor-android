package com.alexinnocenzi.gardenadvisor.ai.funct;

import com.alexinnocenzi.gardenadvisor.ai.dto.GeminiWeather;

public interface OnGeminiWrapperWeatherSuccess {

    void getAnswer(GeminiWeather weather);

}
