package com.carbonara.gardenadvisor.openmeteo;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import okhttp3.HttpUrl;

public class OpenMeteoConstantsTest {

    @Test
    public void validUrlConstant() {
        assertNotNull(HttpUrl.parse(OpenMeteoConstants.Request.API_URL));
    }
}
