package com.carbonara.gardenadvisor.ai.dto;

import com.carbonara.gardenadvisor.persistence.entity.Plant;
import com.carbonara.gardenadvisor.persistence.entity.PlantType;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
@Builder
public class GardeningItem {

  @JsonInclude(Include.NON_NULL)
  private Long id;

  @JsonInclude(Include.NON_NULL)
  private Long gardenId;

  @JsonProperty("name")
  private String name;

  @JsonProperty("type")
  private GardeningItemType type;

  @JsonProperty("cautions")
  private List<String> cautions;

  @JsonProperty("positives")
  private List<String> positives;

  @JsonProperty("suggestions")
  private List<String> suggestions;

  @JsonProperty("recommended")
  private boolean recommended;

  @JsonProperty("recommendedScore")
  private Integer recommendedScore;

  @JsonProperty("maintenanceScore")
  private Integer maintenanceScore;

  public static Plant toDO(GardeningItem gardeningItem) {
    return Plant.builder()
        .plantName(gardeningItem.name)
        .type(PlantType.valueOf(gardeningItem.type.toString()))
        .cautions(gardeningItem.cautions.toString())
        .positives(gardeningItem.positives.toString())
        .recommended(gardeningItem.recommended)
        .recommendedScore(gardeningItem.recommendedScore)
        .maintenanceScore(gardeningItem.maintenanceScore)
        .build();
  }

  public static GardeningItem toDO(Plant plant) {
    return GardeningItem.builder()
        .id(plant.getId())
        .gardenId(plant.getGardenId())
        .name(plant.getPlantName())
        .type(GardeningItemType.valueOf(plant.getType().toString()))
        .cautions(Arrays.stream(plant.getCautions().split(",")).collect(Collectors.toList()))
        .positives(Arrays.stream(plant.getPositives().split(",")).collect(Collectors.toList()))
        .recommended(plant.getRecommended())
        .recommendedScore(plant.getRecommendedScore())
        .maintenanceScore(plant.getMaintenanceScore())
        .build();
  }
}
