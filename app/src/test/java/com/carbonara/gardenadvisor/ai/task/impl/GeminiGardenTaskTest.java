package com.carbonara.gardenadvisor.ai.task.impl;

import static com.carbonara.gardenadvisor.ai.prompt.ConstPrompt.GARDEN_SUGGESTION_PROMPT;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.Test;

public class GeminiGardenTaskTest {

  @Test
  public void testPrompt() throws JsonProcessingException {
    Set<String> plants = Stream.of("t1", "t2", "t3").collect(Collectors.toSet());

    GeminiGardenTask task =
        new GeminiGardenTask(1.2f, 2.3f, "Test Location", plants);

    assertNotNull(task.getPrompt("Open Meteo Result"));
    assertTrue(task.getPrompt("Open Meteo Result").contains("Open Meteo Result"));
    assertTrue(task.getPrompt("Open Meteo Result").contains("Location Name: Test Location"));
    assertTrue(task.getPrompt("Open Meteo Result").contains("My plants: [\"t1\",\"t2\",\"t3\"]"));
    assertTrue(task.getPrompt("Open Meteo Result").contains(GARDEN_SUGGESTION_PROMPT));
  }
}
