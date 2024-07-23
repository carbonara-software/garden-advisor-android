package com.carbonara.gardenadvisor.persistence.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(tableName = "plant")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Plant implements Serializable {

  @PrimaryKey private Long id;

  private Long gardenId;

  private String plantName;

  private PlantType type;
}
