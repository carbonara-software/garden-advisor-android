package com.carbonara.gardenadvisor.ai.dto;

import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

@Getter
@Builder
@Jacksonized
public class GeminiCameraSuggestion {

  private final String name, scientificName, statusDescription, status;
  private final List<String> suggestions;
}
