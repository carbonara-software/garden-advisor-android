package com.carbonara.gardenadvisor.ai;

import static org.junit.Assert.assertNotNull;
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

    Plant plant1 = new Plant(1L, 1L, "Sunflower", PlantType.FLOWER);
    Plant plant2 = new Plant(2L, 1L, "Apple", PlantType.FRUIT);

    Set<Plant> testPlants = new HashSet<>(Arrays.asList(plant1, plant2));

    when(testGarden.getGarden()).thenReturn(gardenMock);
    when(testGarden.getPlants()).thenReturn(testPlants);
  }

  @Test
  public void testPrompt() {
    GeminiWrapper wrapper = new GardenGeminiWrapper(testGarden);
    wrapper.weatherString = "TEST WEATHER";

    assertNotNull(wrapper.getGardeningSuggestionPrompt());

    String expectedPromptBeginning =
        "TEST WEATHER\n"
            + "Location Name: TEST LOCATION\n"
            + "My plants: [Apple, Sunflower]\n"
            + "Given this location, weather data and list of plants, evaluate each of the plants provided, with each value having:";

    assertTrue(wrapper.getGardeningSuggestionPrompt().startsWith(expectedPromptBeginning));
  }
}
