package com.carbonara.gardenadvisor.openmeteo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.carbonara.gardenadvisor.openmeteo.request.OpenMeteoRequest;
import com.carbonara.gardenadvisor.openmeteo.response.OpenMeteoResponseException;

import org.junit.Test;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class OkHttpOpenMeteoClientTest {

    @Test
    public void getWeatherData() throws IOException {
        ResponseBody responseBody = ResponseBody.create("ok!", MediaType.get("text/plain"));
        Response response = new Response.Builder()
                .code(200)
                .protocol(Protocol.HTTP_1_1)
                .request(mock(Request.class))
                .message("test")
                .body(responseBody)
                .build();

        Call callMock = mock(Call.class);
        when(callMock.execute()).thenReturn(response);

        OkHttpClient clientMock = mock(OkHttpClient.class);
        when(clientMock.newCall(any())).thenReturn(callMock);


        OkHttpOpenMeteoClient client = new OkHttpOpenMeteoClient(clientMock);
        OpenMeteoRequest openMeteoRequest = OpenMeteoRequest.builder()
                .lat(12.3F)
                .lon(4.5F)
                .build();
        String responseString = client.getWeatherData(openMeteoRequest);

        assertNotNull(responseString);
        assertEquals("ok!", responseString);
    }

    @Test
    public void getWeatherData_responseError() throws IOException {
        Response response = new Response.Builder()
                .code(400)
                .protocol(Protocol.HTTP_1_1)
                .request(mock(Request.class))
                .message("error")
                .build();

        Call callMock = mock(Call.class);
        when(callMock.execute()).thenReturn(response);

        OkHttpClient clientMock = mock(OkHttpClient.class);
        when(clientMock.newCall(any())).thenReturn(callMock);


        OkHttpOpenMeteoClient client = new OkHttpOpenMeteoClient(clientMock);
        OpenMeteoRequest openMeteoRequest = OpenMeteoRequest.builder()
                .lat(12.3F)
                .lon(4.5F)
                .build();

        OpenMeteoResponseException thrownException = assertThrows(OpenMeteoResponseException.class,
                () -> client.getWeatherData(openMeteoRequest));
        assertEquals("error", thrownException.getMessage());
    }

    @Test
    public void getWeatherData_emptyBody() throws IOException {
        Response response = new Response.Builder()
                .code(200)
                .protocol(Protocol.HTTP_1_1)
                .request(mock(Request.class))
                .message("")
                .build();

        Call callMock = mock(Call.class);
        when(callMock.execute()).thenReturn(response);

        OkHttpClient clientMock = mock(OkHttpClient.class);
        when(clientMock.newCall(any())).thenReturn(callMock);


        OkHttpOpenMeteoClient client = new OkHttpOpenMeteoClient(clientMock);
        OpenMeteoRequest openMeteoRequest = OpenMeteoRequest.builder()
                .lat(12.3F)
                .lon(4.5F)
                .build();

        OpenMeteoResponseException thrownException = assertThrows(OpenMeteoResponseException.class,
                () -> client.getWeatherData(openMeteoRequest));
        assertEquals("Empty Response.", thrownException.getMessage());
    }

    @Test
    public void toOkHttpRequest(){
        OkHttpOpenMeteoClient client = new OkHttpOpenMeteoClient(new OkHttpClient());

        OpenMeteoRequest openMeteoRequest = OpenMeteoRequest.builder()
                .lat(12.3F)
                .lon(4.5F)
                .build();

        Request okHttpRequest = client.toOkHttpRequest(openMeteoRequest);
        assertNotNull(okHttpRequest);

        assertEquals("https://api.open-meteo.com/v1/forecast?latitude=12.3&longitude=4.5&format=csv&hourly=wind_speed_10m%2Csoil_moisture_0_to_1cm%2Crain%2Csoil_moisture_1_to_3cm%2Csurface_pressure%2Csoil_moisture_3_to_9cm%2Crelative_humidity_2m%2Csnowfall%2Csoil_temperature_0cm%2Csoil_temperature_6cm%2Ctemperature_2m%2Cshowers%2Cprecipitation_probability%2Ccloud_cover%2Cwind_speed_80m%2Cwind_speed_120m%2Csnow_depth%2Csoil_temperature_18cm&past_days=7&forecast_days=7", okHttpRequest.url().toString());
    }

}
