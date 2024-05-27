package com.alexinnocenzi.gardenadvisor.persistence.entity;

import androidx.room.ColumnInfo;

import lombok.Data;

@Data
public class Location {

    @ColumnInfo(name = "location_id")
    private Long id;

    private Float lat, lon;
    private String locationName;

}
