package com.carbonara.gardenadvisor.openmeteo;

import com.carbonara.gardenadvisor.openmeteo.request.OpenMeteoRequest;
import java.io.IOException;

public interface OpenMeteoClient {

  String getWeatherData(OpenMeteoRequest openMeteoRequest) throws IOException;
}
