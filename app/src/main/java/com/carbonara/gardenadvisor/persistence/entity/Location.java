package com.carbonara.gardenadvisor.persistence.entity;

import androidx.room.ColumnInfo;
import java.io.Serializable;
import lombok.Data;

@Data
public class Location implements Serializable {

  @ColumnInfo(name = "location_id")
  private Long id;

  private Float lat, lon;
  private String locationName;
}
