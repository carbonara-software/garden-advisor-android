package com.carbonara.gardenadvisor.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.carbonara.gardenadvisor.ai.dto.GeminiCameraSuggestion;
import java.util.Map;
import org.junit.Test;

public class AppCacheTest {

  @Test
  public void testCameraCache() {
    assertTrue(AppCache.getInstance().getCachedCameraSuggestions().isEmpty());

    GeminiCameraSuggestion cameraSuggestion = GeminiCameraSuggestion.builder().build();
    int hashCode = cameraSuggestion.hashCode();

    AppCache.getInstance().addCameraSuggestion(cameraSuggestion);

    Map<String, GeminiCameraSuggestion> cached =
        AppCache.getInstance().getCachedCameraSuggestions();

    assertEquals(1, cached.size());
    assertTrue(cached.containsKey("ai-cam-" + hashCode));
  }
}
