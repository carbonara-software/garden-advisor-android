package com.carbonara.gardenadvisor.ai;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.carbonara.gardenadvisor.ai.dto.GeminiGardeningSugg;
import com.carbonara.gardenadvisor.persistence.entity.Garden;
import com.carbonara.gardenadvisor.persistence.entity.GardenWithPlants;
import com.carbonara.gardenadvisor.persistence.entity.Location;
import com.carbonara.gardenadvisor.persistence.entity.Plant;
import com.carbonara.gardenadvisor.persistence.entity.PlantType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import org.junit.Before;
import org.junit.Test;

public class GardenGeminiWrapperTest {

  private GardenWithPlants testGarden;

  private final String geminiResponse =
      "{\n"
          + "  \"fruits\": [\n"
          + "    {\n"
          + "      \"name\": \"Strawberries\",\n"
          + "      \"type\": \"fruit\",\n"
          + "      \"recommended\": true,\n"
          + "      \"recommendedScore\": 4,\n"
          + "      \"maintenanceScore\": 3,\n"
          + "      \"positives\": [\n"
          + "        \"Warm weather is ideal for strawberry growth\",\n"
          + "        \"Soil moisture levels are suitable for strawberries\",\n"
          + "        \"The location is suitable for strawberries\",\n"
          + "        \"The weather is generally dry and sunny, which is good for strawberries\"\n"
          + "      ],\n"
          + "      \"cautions\": [\n"
          + "        \"Ensure the soil is well-drained, as strawberries don't tolerate wet conditions\",\n"
          + "        \"Monitor for pests such as aphids and spider mites\",\n"
          + "        \"Provide adequate sunlight for optimal fruit production\"\n"
          + "      ],\n"
          + "      \"suggestions\": [\n"
          + "        \"Consider planting a strawberry variety that is suited to your specific region and climate\",\n"
          + "        \"Mulch around the plants to help retain moisture and suppress weeds\"\n"
          + "      ]\n"
          + "    },\n"
          + "    {\n"
          + "      \"name\": \"Blueberries\",\n"
          + "      \"type\": \"fruit\",\n"
          + "      \"recommended\": true,\n"
          + "      \"recommendedScore\": 3,\n"
          + "      \"maintenanceScore\": 4,\n"
          + "      \"positives\": [\n"
          + "        \"The soil moisture levels are favorable for blueberries\",\n"
          + "        \"The location is suitable for blueberries\"\n"
          + "      ],\n"
          + "      \"cautions\": [\n"
          + "        \"Blueberries thrive in acidic soil (pH 4.5-5.5), ensure your soil meets this requirement\",\n"
          + "        \"They need plenty of water, especially during hot weather\",\n"
          + "        \"Provide protection from strong winds\"\n"
          + "      ],\n"
          + "      \"suggestions\": [\n"
          + "        \"Amend the soil with sulfur or peat moss to lower the pH if necessary\",\n"
          + "        \"Water deeply and less frequently to encourage root development\"\n"
          + "      ]\n"
          + "    },\n"
          + "    {\n"
          + "      \"name\": \"Citrus\",\n"
          + "      \"type\": \"fruit\",\n"
          + "      \"recommended\": true,\n"
          + "      \"recommendedScore\": 2,\n"
          + "      \"maintenanceScore\": 2,\n"
          + "      \"positives\": [\n"
          + "        \"The location is suitable for citrus\"\n"
          + "      ],\n"
          + "      \"cautions\": [\n"
          + "        \"Citrus trees need full sun (at least 6 hours per day)\",\n"
          + "        \"The weather is warm, but citrus trees are susceptible to frost damage\",\n"
          + "        \"Monitor for pests and diseases, such as citrus greening\"\n"
          + "      ],\n"
          + "      \"suggestions\": [\n"
          + "        \"Choose a frost-tolerant citrus variety if you live in a region prone to frost\",\n"
          + "        \"Fertilize regularly with a citrus-specific fertilizer\"\n"
          + "      ]\n"
          + "    },\n"
          + "    {\n"
          + "      \"name\": \"Figs\",\n"
          + "      \"type\": \"fruit\",\n"
          + "      \"recommended\": true,\n"
          + "      \"recommendedScore\": 4,\n"
          + "      \"maintenanceScore\": 4,\n"
          + "      \"positives\": [\n"
          + "        \"Figs prefer warm, sunny locations\",\n"
          + "        \"The soil moisture levels are suitable for figs\",\n"
          + "        \"The location is suitable for figs\"\n"
          + "      ],\n"
          + "      \"cautions\": [\n"
          + "        \"Figs require good drainage and are sensitive to overwatering\",\n"
          + "        \"They are prone to certain diseases, such as fig rust\",\n"
          + "        \"Figs can be somewhat messy, as they drop fruit and leaves\"\n"
          + "      ],\n"
          + "      \"suggestions\": [\n"
          + "        \"Choose a fig variety that is well-suited to your climate\",\n"
          + "        \"Pruning can help manage size and shape\"\n"
          + "      ]\n"
          + "    }\n"
          + "  ],\n"
          + "  \"vegetables\": [\n"
          + "    {\n"
          + "      \"name\": \"Tomatoes\",\n"
          + "      \"type\": \"vegetable\",\n"
          + "      \"recommended\": true,\n"
          + "      \"recommendedScore\": 4,\n"
          + "      \"maintenanceScore\": 3,\n"
          + "      \"positives\": [\n"
          + "        \"Tomatoes prefer warm temperatures and full sun\",\n"
          + "        \"Soil moisture levels are suitable for tomatoes\",\n"
          + "        \"The location is suitable for tomatoes\"\n"
          + "      ],\n"
          + "      \"cautions\": [\n"
          + "        \"Tomatoes need adequate water, especially during hot weather\",\n"
          + "        \"They are prone to various diseases such as blight and wilt\",\n"
          + "        \"Consider using stakes or cages to support the plants\"\n"
          + "      ],\n"
          + "      \"suggestions\": [\n"
          + "        \"Choose a tomato variety that is disease-resistant\",\n"
          + "        \"Mulch around the plants to help retain moisture and suppress weeds\"\n"
          + "      ]\n"
          + "    },\n"
          + "    {\n"
          + "      \"name\": \"Peppers\",\n"
          + "      \"type\": \"vegetable\",\n"
          + "      \"recommended\": true,\n"
          + "      \"recommendedScore\": 4,\n"
          + "      \"maintenanceScore\": 3,\n"
          + "      \"positives\": [\n"
          + "        \"Peppers need warm weather and full sun\",\n"
          + "        \"Soil moisture levels are suitable for peppers\",\n"
          + "        \"The location is suitable for peppers\"\n"
          + "      ],\n"
          + "      \"cautions\": [\n"
          + "        \"Peppers are susceptible to pests and diseases such as aphids and blight\",\n"
          + "        \"They need consistent watering, especially during hot weather\",\n"
          + "        \"Consider using stakes or cages to support the plants\"\n"
          + "      ],\n"
          + "      \"suggestions\": [\n"
          + "        \"Choose a pepper variety that is disease-resistant\",\n"
          + "        \"Mulch around the plants to help retain moisture and suppress weeds\"\n"
          + "      ]\n"
          + "    },\n"
          + "    {\n"
          + "      \"name\": \"Cucumbers\",\n"
          + "      \"type\": \"vegetable\",\n"
          + "      \"recommended\": true,\n"
          + "      \"recommendedScore\": 3,\n"
          + "      \"maintenanceScore\": 2,\n"
          + "      \"positives\": [\n"
          + "        \"Cucumbers thrive in warm temperatures and need ample sunlight\",\n"
          + "        \"Soil moisture levels are suitable for cucumbers\",\n"
          + "        \"The location is suitable for cucumbers\"\n"
          + "      ],\n"
          + "      \"cautions\": [\n"
          + "        \"Cucumbers need consistent watering\",\n"
          + "        \"They are susceptible to pests such as aphids and squash bugs\",\n"
          + "        \"Consider using trellises or cages to support the vines\"\n"
          + "      ],\n"
          + "      \"suggestions\": [\n"
          + "        \"Plant cucumber seeds or seedlings after the last frost\",\n"
          + "        \"Mulch around the plants to help retain moisture and suppress weeds\"\n"
          + "      ]\n"
          + "    },\n"
          + "    {\n"
          + "      \"name\": \"Zucchini\",\n"
          + "      \"type\": \"vegetable\",\n"
          + "      \"recommended\": true,\n"
          + "      \"recommendedScore\": 4,\n"
          + "      \"maintenanceScore\": 3,\n"
          + "      \"positives\": [\n"
          + "        \"Zucchini needs warm weather and full sun\",\n"
          + "        \"Soil moisture levels are suitable for zucchini\",\n"
          + "        \"The location is suitable for zucchini\"\n"
          + "      ],\n"
          + "      \"cautions\": [\n"
          + "        \"Zucchini plants need consistent watering, especially during hot weather\",\n"
          + "        \"They are prone to pests such as squash bugs and cucumber beetles\",\n"
          + "        \"Harvest zucchini regularly to encourage continued production\"\n"
          + "      ],\n"
          + "      \"suggestions\": [\n"
          + "        \"Plant zucchini seeds or seedlings after the last frost\",\n"
          + "        \"Mulch around the plants to help retain moisture and suppress weeds\"\n"
          + "      ]\n"
          + "    }\n"
          + "  ],\n"
          + "  \"flowers\": [\n"
          + "    {\n"
          + "      \"name\": \"Zinnias\",\n"
          + "      \"type\": \"flower\",\n"
          + "      \"recommended\": true,\n"
          + "      \"recommendedScore\": 4,\n"
          + "      \"maintenanceScore\": 4,\n"
          + "      \"positives\": [\n"
          + "        \"Zinnias prefer full sun and warm temperatures\",\n"
          + "        \"Soil moisture levels are suitable for zinnias\",\n"
          + "        \"The location is suitable for zinnias\",\n"
          + "        \"The weather is generally dry and sunny, which is good for zinnias\"\n"
          + "      ],\n"
          + "      \"cautions\": [\n"
          + "        \"Zinnias need regular watering, especially during dry periods\",\n"
          + "        \"They are susceptible to pests such as aphids and spider mites\",\n"
          + "        \"Deadheading (removing spent flowers) can encourage continued blooming\"\n"
          + "      ],\n"
          + "      \"suggestions\": [\n"
          + "        \"Choose a zinnia variety that is well-suited to your climate\",\n"
          + "        \"Plant zinnia seeds or seedlings after the last frost\"\n"
          + "      ]\n"
          + "    },\n"
          + "    {\n"
          + "      \"name\": \"Sunflowers\",\n"
          + "      \"type\": \"flower\",\n"
          + "      \"recommended\": true,\n"
          + "      \"recommendedScore\": 5,\n"
          + "      \"maintenanceScore\": 5,\n"
          + "      \"positives\": [\n"
          + "        \"Sunflowers need full sun and warm temperatures\",\n"
          + "        \"Soil moisture levels are suitable for sunflowers\",\n"
          + "        \"The location is suitable for sunflowers\"\n"
          + "      ],\n"
          + "      \"cautions\": [\n"
          + "        \"Sunflowers need consistent watering, especially during dry periods\",\n"
          + "        \"They are prone to pests such as aphids and spider mites\",\n"
          + "        \"Sunflowers can grow very tall, so consider spacing accordingly\"\n"
          + "      ],\n"
          + "      \"suggestions\": [\n"
          + "        \"Choose a sunflower variety that is well-suited to your climate\",\n"
          + "        \"Plant sunflower seeds or seedlings after the last frost\"\n"
          + "      ]\n"
          + "    },\n"
          + "    {\n"
          + "      \"name\": \"Cosmos\",\n"
          + "      \"type\": \"flower\",\n"
          + "      \"recommended\": true,\n"
          + "      \"recommendedScore\": 4,\n"
          + "      \"maintenanceScore\": 4,\n"
          + "      \"positives\": [\n"
          + "        \"Cosmos need full sun and warm temperatures\",\n"
          + "        \"Soil moisture levels are suitable for cosmos\",\n"
          + "        \"The location is suitable for cosmos\",\n"
          + "        \"The weather is generally dry and sunny, which is good for cosmos\"\n"
          + "      ],\n"
          + "      \"cautions\": [\n"
          + "        \"Cosmos need regular watering, especially during dry periods\",\n"
          + "        \"They are susceptible to pests such as aphids and spider mites\",\n"
          + "        \"Deadheading (removing spent flowers) can encourage continued blooming\"\n"
          + "      ],\n"
          + "      \"suggestions\": [\n"
          + "        \"Choose a cosmos variety that is well-suited to your climate\",\n"
          + "        \"Plant cosmos seeds or seedlings after the last frost\"\n"
          + "      ]\n"
          + "    },\n"
          + "    {\n"
          + "      \"name\": \"Marigolds\",\n"
          + "      \"type\": \"flower\",\n"
          + "      \"recommended\": true,\n"
          + "      \"recommendedScore\": 4,\n"
          + "      \"maintenanceScore\": 5,\n"
          + "      \"positives\": [\n"
          + "        \"Marigolds prefer full sun and warm temperatures\",\n"
          + "        \"Soil moisture levels are suitable for marigolds\",\n"
          + "        \"The location is suitable for marigolds\",\n"
          + "        \"The weather is generally dry and sunny, which is good for marigolds\"\n"
          + "      ],\n"
          + "      \"cautions\": [\n"
          + "        \"Marigolds need regular watering, especially during dry periods\",\n"
          + "        \"They are susceptible to pests such as aphids and spider mites\",\n"
          + "        \"Deadheading (removing spent flowers) can encourage continued blooming\"\n"
          + "      ],\n"
          + "      \"suggestions\": [\n"
          + "        \"Choose a marigold variety that is well-suited to your climate\",\n"
          + "        \"Plant marigold seeds or seedlings after the last frost\"\n"
          + "      ]\n"
          + "    }\n"
          + "  ]\n"
          + "}\n";

  @Before
  public void setup() {
    testGarden = mock(GardenWithPlants.class);

    Location testLocation = new Location();
    testLocation.setLocationName("TEST LOCATION");
    testLocation.setLat(123.4F);
    testLocation.setLon(567.8F);

    Garden gardenMock = mock(Garden.class);
    when(gardenMock.getLocation()).thenReturn(testLocation);

    Plant plant1 =
        Plant.builder().id(1L).gardenId(1L).plantName("Sunflower").type(PlantType.FLOWER).build();
    Plant plant2 =
        Plant.builder().id(2L).gardenId(1L).plantName("Apple").type(PlantType.FRUIT).build();
    Set<Plant> testPlants = new HashSet<>(Arrays.asList(plant1, plant2));

    when(testGarden.getGarden()).thenReturn(gardenMock);
    when(testGarden.getPlants()).thenReturn(testPlants);
  }

  @Test
  public void testPrompt() {
    GeminiWrapper wrapper = new GardenGeminiWrapper(testGarden);
    wrapper.weatherString = "TEST WEATHER";

    String expectedPromptBeginning = "TEST WEATHER\nLocation Name: TEST LOCATION\nMy plants:";

    String prompt = wrapper.getGardeningSuggestionPrompt();

    assertTrue(prompt.startsWith(expectedPromptBeginning));
    assertTrue(
        prompt.contains(
            "{\"id\":1,\"gardenId\":1,\"plantName\":\"Sunflower\",\"type\":\"FLOWER\"}"));
    assertTrue(
        prompt.contains("{\"id\":2,\"gardenId\":1,\"plantName\":\"Apple\",\"type\":\"FRUIT\"}"));
  }

  @Test
  public void deserializeGeminiResponse() throws JsonProcessingException {
    ObjectMapper mapper = new ObjectMapper();
    GeminiGardeningSugg suggestion = mapper.readValue(geminiResponse, GeminiGardeningSugg.class);

    assertNotNull(suggestion);
    assertNotNull(suggestion.getFruits());
    assertEquals(4, suggestion.getFruits().size());
    assertNotNull(suggestion.getVegetables());
    assertEquals(4, suggestion.getVegetables().size());
    assertNotNull(suggestion.getFlowers());
    assertEquals(4, suggestion.getFlowers().size());
  }
}
