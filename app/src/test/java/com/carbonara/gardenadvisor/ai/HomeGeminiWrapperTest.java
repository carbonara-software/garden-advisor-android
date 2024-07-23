package com.carbonara.gardenadvisor.ai;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class HomeGeminiWrapperTest {

  @Test
  public void testPrompt() {
    GeminiWrapper wrapper = new HomeGeminiWrapper(123.4F, 567.8F, "TEST LOCATION");
    wrapper.weatherString = "TEST WEATHER";

    assertNotNull(wrapper.getGardeningSuggestionPrompt());

    String expectedPromptBeginning =
        "TEST WEATHER\n"
            + "Location Name: TEST LOCATION\n"
            + "Given this location and weather data, provide an answer in Json format with a list of recommended fruits, vegetables and flowers that can be planted at this time, with each value having:\n";
    assertTrue(wrapper.getGardeningSuggestionPrompt().startsWith(expectedPromptBeginning));
  }
}
