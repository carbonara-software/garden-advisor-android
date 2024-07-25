package com.carbonara.gardenadvisor.ai.dto;

import java.util.List;
import lombok.Data;

@Data
public class GeminiCameraSuggestion {

  private String name, scientificName, statusDescription, status;
  private List<String> suggestions;
}
