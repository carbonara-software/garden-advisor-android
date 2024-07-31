package com.carbonara.gardenadvisor.ai.task.impl;

import static com.carbonara.gardenadvisor.ai.prompt.ConstPrompt.WEATHER_PROMPT;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class GeminiWeatherTaskTest {

  @Test
  public void testPrompt() {
    GeminiWeatherTask task =
        new GeminiWeatherTask(1.2f, 2.3f, "Test Location", "open meteo result");

    assertNotNull(task.getPrompt());
    assertTrue(task.getPrompt().contains("open meteo result"));
    assertTrue(task.getPrompt().contains("Location Name: Test Location"));
    assertTrue(task.getPrompt().contains(WEATHER_PROMPT));
  }
}
