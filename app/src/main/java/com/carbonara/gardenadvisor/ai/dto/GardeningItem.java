package com.carbonara.gardenadvisor.ai.dto;

import static com.carbonara.gardenadvisor.util.AppUtil.extractStringList;

import com.carbonara.gardenadvisor.persistence.entity.Plant;
import com.carbonara.gardenadvisor.persistence.entity.PlantType;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.jackson.Jacksonized;

@Getter
@Setter
@ToString
@Builder
@Jacksonized
public class GardeningItem implements GeminiResult {

  //  @JsonInclude(Include.NON_NULL)
  //  private Long id;
  //
  //  @JsonInclude(Include.NON_NULL)
  //  private Long gardenId;

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
        .cautions(extractStringList(gardeningItem.cautions))
        .positives(extractStringList(gardeningItem.positives))
        .suggestions(extractStringList(gardeningItem.suggestions))
        .recommended(gardeningItem.recommended)
        .recommendedScore(gardeningItem.recommendedScore)
        .maintenanceScore(gardeningItem.maintenanceScore)
        .build();
  }

  public static GardeningItem toDTO(Plant plant) {
    return GardeningItem.builder()
        //        .id(plant.getId())
        //        .gardenId(plant.getGardenId())
        .name(plant.getPlantName())
        .type(GardeningItemType.valueOf(plant.getType().toString()))
        .suggestions(extractStringList(plant.getSuggestions()))
        .cautions(extractStringList(plant.getCautions()))
        .positives(extractStringList(plant.getPositives()))
        .recommended(plant.getRecommended())
        .recommendedScore(plant.getRecommendedScore())
        .maintenanceScore(plant.getMaintenanceScore())
        .build();
  }
}
