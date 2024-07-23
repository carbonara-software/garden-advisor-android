package com.carbonara.gardenadvisor.ai;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.carbonara.gardenadvisor.persistence.entity.Garden;
import com.carbonara.gardenadvisor.persistence.entity.GardenWithPlants;
import com.carbonara.gardenadvisor.persistence.entity.Location;
import com.carbonara.gardenadvisor.persistence.entity.Plant;
import com.carbonara.gardenadvisor.persistence.entity.PlantType;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import org.junit.Before;
import org.junit.Test;

public class GardenGeminiWrapperTest {

  private GardenWithPlants testGarden;

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
}
