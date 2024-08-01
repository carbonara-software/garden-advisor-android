package com.carbonara.gardenadvisor.persistence.entity;

import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(tableName = "garden")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Garden implements Serializable {
  @PrimaryKey private Long id;

  private String description;

  @Embedded private Location location;
}
