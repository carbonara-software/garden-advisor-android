package com.alexinnocenzi.gardenadvisor.openmeteo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.alexinnocenzi.gardenadvisor.openmeteo.request.OpenMeteoRequest;

import org.junit.Test;

import okhttp3.Request;

public class OkHttpOpenMeteoClientTest {

    OkHttpOpenMeteoClient client = new OkHttpOpenMeteoClient();

    @Test
    public void toOkHttpRequest() throws Exception {
        OpenMeteoRequest openMeteoRequest = OpenMeteoRequest.builder()
                .lat(12.3F)
                .lon(4.5F)
                .build();

        Request okHttpRequest = client.toOkHttpRequest(openMeteoRequest);
        assertNotNull(okHttpRequest);

        assertEquals("https://api.open-meteo.com/v1/forecast?latitude=12.3&longitude=4.5&format=csv&hourly=wind_speed_10m%2Csoil_moisture_0_to_1cm%2Crain%2Csoil_moisture_1_to_3cm%2Csurface_pressure%2Csoil_moisture_3_to_9cm%2Crelative_humidity_2m%2Csnowfall%2Csoil_temperature_0cm%2Csoil_temperature_6cm%2Ctemperature_2m%2Cshowers%2Cprecipitation_probability%2Ccloud_cover%2Cwind_speed_80m%2Cwind_speed_120m%2Csnow_depth%2Csoil_temperature_18cm&past_days=7&forecast_days=7", okHttpRequest.url().toString());
    }

}
