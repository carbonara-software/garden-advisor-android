package com.alexinnocenzi.gardenadvisor.openmeteo.response;

import java.io.IOException;

public class OpenMeteoResponseException extends IOException {

    public OpenMeteoResponseException(String message) {
        super(message);
    }
}
