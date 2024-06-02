package com.carbonara.gardenadvisor.openmeteo.request;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class OpenMeteoRequestTest {

  @Test(expected = NullPointerException.class)
  public void missingRequiredData() {
    OpenMeteoRequest request = OpenMeteoRequest.builder().build();
  }

  @Test
  public void defaultBuilder() {
    OpenMeteoRequest request = OpenMeteoRequest.builder().lat(1.2F).lon(3.4F).build();

    assertNotNull(request);
    assertEquals(Float.valueOf(1.2F), request.getLat());
    assertEquals(Float.valueOf(3.4F), request.getLon());

    assertEquals(Integer.valueOf(7), request.getForecastDays());
    assertEquals(Integer.valueOf(7), request.getPastDays());
  }

  @Test
  public void customRequest() {
    OpenMeteoRequest request =
        OpenMeteoRequest.builder().lat(1.2F).lon(3.4F).forecastDays(14).pastDays(1).build();

    assertNotNull(request);
    assertEquals(Float.valueOf(1.2F), request.getLat());
    assertEquals(Float.valueOf(3.4F), request.getLon());

    assertEquals(Integer.valueOf(14), request.getForecastDays());
    assertEquals(Integer.valueOf(1), request.getPastDays());
  }
}
