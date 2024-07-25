package com.carbonara.gardenadvisor.ai.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.List;
import lombok.Getter;

@Getter
public class GeminiCameraSuggestion {

  private final String name, scientificName, statusDescription, status;
  private final List<String> suggestions;

  @JsonCreator
  public GeminiCameraSuggestion(
      String name,
      String scientificName,
      String statusDescription,
      String status,
      List<String> suggestions) {
    this.name = name;
    this.scientificName = scientificName;
    this.statusDescription = statusDescription;
    this.status = status;
    this.suggestions = suggestions;
  }
}
