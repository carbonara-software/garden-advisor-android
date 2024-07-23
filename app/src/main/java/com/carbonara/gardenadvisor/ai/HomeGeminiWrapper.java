package com.carbonara.gardenadvisor.ai;

import static com.carbonara.gardenadvisor.ai.prompt.ConstPrompt.HOME_SUGGESTION_PROMPT;

public class HomeGeminiWrapper extends GeminiWrapper {

  public HomeGeminiWrapper(float lat, float lon, String locationName) {
    super(lat, lon, locationName);
  }

  @Override
  public String getGardeningSuggestionPrompt() {
    return weatherString + "\nLocation Name: " + locationName + "\n" + HOME_SUGGESTION_PROMPT;
  }
}
