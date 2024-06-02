package com.carbonara.gardenadvisor.openmeteo.response;

import java.io.IOException;

public class OpenMeteoResponseException extends IOException {

  public OpenMeteoResponseException(String message) {
    super(message);
  }
}
