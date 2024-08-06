package com.carbonara.gardenadvisor.ai.dto;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class CameraSuggestionTest {

  @Test
  public void testPrintableStatus() {
    GeminiCameraSuggestion healthy = GeminiCameraSuggestion.builder().status("healthy").build();

    assertEquals("Looking good!", healthy.getPrintableStatus());

    GeminiCameraSuggestion notWell = GeminiCameraSuggestion.builder().status("needs_care").build();

    assertEquals(
        "Might need a little care.\nTake a look at these suggestions:",
        notWell.getPrintableStatus());
  }

  @Test
  public void testStatusEmoji() {
    GeminiCameraSuggestion healthy = GeminiCameraSuggestion.builder().status("healthy").build();

    assertEquals("\uD83E\uDD29", healthy.getStatusEmoji());

    GeminiCameraSuggestion notWell = GeminiCameraSuggestion.builder().status("needs_care").build();

    assertEquals("\uD83E\uDD12", notWell.getStatusEmoji());
  }

  @Test
  public void unknownStatus() {
    GeminiCameraSuggestion noStatus = GeminiCameraSuggestion.builder().status("none").build();

    assertEquals("None", noStatus.getPrintableStatus());
    assertEquals("\uD83E\uDD14", noStatus.getStatusEmoji());
  }
}
