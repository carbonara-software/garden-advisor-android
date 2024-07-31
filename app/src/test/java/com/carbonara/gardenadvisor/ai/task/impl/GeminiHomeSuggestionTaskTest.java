package com.carbonara.gardenadvisor.ai.task.impl;

import static com.carbonara.gardenadvisor.ai.prompt.ConstPrompt.HOME_SUGGESTION_PROMPT;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class GeminiHomeSuggestionTaskTest {

  @Test
  public void testPrompt() {

    GeminiHomeSuggestionTask task =
        new GeminiHomeSuggestionTask(1.2f, 2.3f, "Test Location");

    assertNotNull(task.getPrompt("Open Meteo Result"));
    assertTrue(task.getPrompt("Open Meteo Result").contains("Open Meteo Result"));
    assertTrue(task.getPrompt("Open Meteo Result").contains("Location Name: Test Location"));
    assertTrue(task.getPrompt("Open Meteo Result").contains(HOME_SUGGESTION_PROMPT));
  }
}
