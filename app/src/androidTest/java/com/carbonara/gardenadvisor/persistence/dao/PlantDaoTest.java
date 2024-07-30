package com.carbonara.gardenadvisor.persistence.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import android.content.Context;
import androidx.room.Room;
import androidx.test.platform.app.InstrumentationRegistry;
import com.carbonara.gardenadvisor.persistence.AppDatabase;
import com.carbonara.gardenadvisor.persistence.entity.Plant;
import com.carbonara.gardenadvisor.persistence.entity.PlantType;
import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class PlantDaoTest {

  private AppDatabase inMemoryDb;
  private PlantDao dao;

  @Before
  public void setup() {
    Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
    inMemoryDb =
        Room.inMemoryDatabaseBuilder(context, AppDatabase.class).allowMainThreadQueries().build();
    dao = inMemoryDb.plantDao();
  }

  @After
  public void tearDown() {
    inMemoryDb.close();
  }

  @Test
  public void crud() {
    Plant testPlant = Plant.builder().plantName("Tomato").type(PlantType.VEGETABLE).build();

    dao.insertPlant(testPlant).blockingAwait();

    List<Plant> savedList = dao.getPlants().blockingFirst();
    assertNotNull(savedList);
    assertEquals(1, savedList.size());

    Plant saved = savedList.get(0);
    assertEquals("Tomato", saved.getPlantName());
    assertEquals(PlantType.VEGETABLE, saved.getType());

    // Tomatoes are actually fruit!
    saved.setType(PlantType.FRUIT);
    dao.updatePlant(saved).blockingAwait();

    savedList = dao.getPlants().blockingFirst();
    assertNotNull(savedList);
    assertEquals(1, savedList.size());

    saved = savedList.get(0);
    assertEquals("Tomato", saved.getPlantName());
    assertEquals(PlantType.FRUIT, saved.getType());

    dao.deletePlant(saved).blockingAwait();
    assertEquals(0, dao.getPlants().blockingFirst().size());
  }
}
