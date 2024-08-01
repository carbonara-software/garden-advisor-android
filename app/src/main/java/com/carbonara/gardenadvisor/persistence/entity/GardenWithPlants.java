package com.carbonara.gardenadvisor.persistence.entity;

import androidx.room.Embedded;
import androidx.room.Relation;
import java.io.Serializable;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GardenWithPlants implements Serializable {

  @Embedded private Garden garden;

  @Relation(parentColumn = "id", entityColumn = "gardenId")
  private Set<Plant> plants;
}
