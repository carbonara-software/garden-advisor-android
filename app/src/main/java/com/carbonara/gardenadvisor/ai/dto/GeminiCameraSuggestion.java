package com.carbonara.gardenadvisor.ai.dto;

import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

@Getter
@Builder
@Jacksonized
public class GeminiCameraSuggestion implements GeminiResult {

  private final String name, scientificName, statusDescription, status;
  private final List<String> suggestions;

  public String getPrintableStatus() {
    String printableStatus = "None";

    switch (status) {
      case "healthy":
        printableStatus = "Looking good!";
        break;
      case "needs_care":
        printableStatus = "Might need a little care.\nTake a look at these suggestions:";
    }

    return printableStatus;
  }

  public String getStatusEmoji() {
    String statusEmoji = "\uD83E\uDD14";
    switch (status) {
      case "healthy":
        statusEmoji = "\uD83E\uDD29";
        break;
      case "needs_care":
        statusEmoji = "\uD83E\uDD12";
    }
    return statusEmoji;
  }
}
