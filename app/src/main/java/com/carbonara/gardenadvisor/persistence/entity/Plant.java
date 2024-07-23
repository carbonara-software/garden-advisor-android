package com.carbonara.gardenadvisor.persistence.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Entity(tableName = "plant")
@Data
@Builder
@AllArgsConstructor
@JsonInclude(Include.NON_NULL)
public class Plant implements Serializable {

  @PrimaryKey private Long id;

  private Long gardenId;

  private String plantName;

  private PlantType type;

  private Boolean recommended;

  private String cautions;

  private String positives;

  private String suggestions;

  private Integer recommendedScore;

  private Integer maintenanceScore;
}
