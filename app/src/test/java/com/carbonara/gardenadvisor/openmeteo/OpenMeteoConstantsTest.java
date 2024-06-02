package com.carbonara.gardenadvisor.openmeteo;

import static org.junit.Assert.assertNotNull;

import okhttp3.HttpUrl;
import org.junit.Test;

public class OpenMeteoConstantsTest {

  @Test
  public void validUrlConstant() {
    assertNotNull(HttpUrl.parse(OpenMeteoConstants.Request.API_URL));
  }
}
