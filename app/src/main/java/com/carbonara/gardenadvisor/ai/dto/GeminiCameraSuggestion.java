package com.carbonara.gardenadvisor.ai.dto;

import static com.carbonara.gardenadvisor.util.AppUtil.Constants.PICTURE_PREFIX;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.List;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

@Getter
@Builder
@Jacksonized
@EqualsAndHashCode
public class GeminiCameraSuggestion implements GeminiResult {

  private final String name, scientificName, statusDescription, status;
  private final List<String> suggestions;

  @JsonIgnore
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

  @JsonIgnore
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

  @JsonIgnore
  public String getCachedId() {
    return PICTURE_PREFIX + hashCode();
  }
}
