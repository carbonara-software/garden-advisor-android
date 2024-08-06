package com.carbonara.gardenadvisor.ai.dto;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.carbonara.gardenadvisor.persistence.entity.Plant;
import com.carbonara.gardenadvisor.persistence.entity.PlantType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

public class GardeningItemTest {
  @Test
  public void recommendationJson() throws JsonProcessingException {
    ObjectMapper mapper = new ObjectMapper();
    String recommendationJson =
        "{\n"
            + "      \"type\": \"fruit\",\n"
            + "      \"recommended\": false,\n"
            + "      \"recommendedScore\": 1,\n"
            + "      \"maintenanceScore\": 4,\n"
            + "      \"positives\": [\n"
            + "        \"The soil temperature is consistently warm, which is great for apple trees.\",\n"
            + "        \"The wind speeds are generally mild, which will help protect the trees from damage\"\n"
            + "      ],\n"
            + "      \"cautions\": [\n"
            + "        \"Apple trees are best planted in the fall or early spring for optimal growth. Planting in summer is not ideal due to high temperatures and potential stress.\",\n"
            + "        \"The dry conditions with minimal rain could make it difficult to maintain adequate moisture for a newly planted apple tree\"\n"
            + "      ],\n"
            + "      \"suggestions\": [\n"
            + "        \"Consider waiting to plant the apple tree in the fall or early spring. It would have a much higher chance of success then.\"\n"
            + "      ]\n"
            + "    }";
    GardeningItem gardeningItem = mapper.readValue(recommendationJson, GardeningItem.class);

    assertNotNull(gardeningItem);

    assertEquals(GardeningItemType.FRUIT, gardeningItem.getType());
  }

  @Test
  public void toDO() throws JsonProcessingException {
    ObjectMapper mapper = new ObjectMapper();
    String gardenJson =
        "{\n"
            + "      \"type\": \"fruit\",\n"
            + "      \"recommended\": false,\n"
            + "      \"recommendedScore\": 1,\n"
            + "      \"maintenanceScore\": 4,\n"
            + "      \"positives\": [\n"
            + "        \"The soil temperature is consistently warm, which is great for apple trees.\",\n"
            + "        \"The wind speeds are generally mild, which will help protect the trees from damage\"\n"
            + "      ],\n"
            + "      \"cautions\": [\n"
            + "        \"Apple trees are best planted in the fall or early spring for optimal growth. Planting in summer is not ideal due to high temperatures and potential stress.\",\n"
            + "        \"The dry conditions with minimal rain could make it difficult to maintain adequate moisture for a newly planted apple tree\"\n"
            + "      ],\n"
            + "      \"suggestions\": [\n"
            + "        \"Consider waiting to plant the apple tree in the fall or early spring. It would have a much higher chance of success then.\"\n"
            + "      ]\n"
            + "    }";
    GardeningItem gardeningItem = mapper.readValue(gardenJson, GardeningItem.class);

    assertNotNull(gardeningItem);

    assertEquals(GardeningItemType.FRUIT, gardeningItem.getType());

    Plant plantDo = GardeningItem.toDO(gardeningItem);

    assertNotNull(plantDo);

    assertTrue(plantDo.getCautions().startsWith("[\"Apple trees are best planted in"));
    assertEquals(PlantType.FRUIT, plantDo.getType());
  }

  @Test
  public void toDTO() {
    Plant testPlant =
        Plant.builder()
            .cautions("[\"s1\", \"s2\", \"s3\"]")
            .positives("[]")
            .recommendedScore(10)
            .recommended(true)
            .plantName("test plant")
            .type(PlantType.VEGETABLE)
            .maintenanceScore(10)
            .build();

    GardeningItem gardeningItem = GardeningItem.toDTO(testPlant);

    assertNotNull(gardeningItem);

    assertEquals("test plant", gardeningItem.getName());
    assertEquals(GardeningItemType.VEGETABLE, gardeningItem.getType());
    assertEquals(3, gardeningItem.getCautions().size());
    assertEquals("s1", gardeningItem.getCautions().get(0));
    assertEquals(0, gardeningItem.getPositives().size());
    assertEquals(0, gardeningItem.getSuggestions().size());
  }
}
