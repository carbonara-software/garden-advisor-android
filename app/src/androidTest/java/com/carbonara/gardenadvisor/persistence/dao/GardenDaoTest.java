package com.carbonara.gardenadvisor.persistence.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import android.content.Context;
import androidx.room.Room;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import com.carbonara.gardenadvisor.persistence.AppDatabase;
import com.carbonara.gardenadvisor.persistence.entity.Garden;
import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class GardenDaoTest {

  private AppDatabase inMemoryDb;
  private GardenDao dao;

  @Before
  public void setup() {
    Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
    inMemoryDb =
        Room.inMemoryDatabaseBuilder(context, AppDatabase.class).allowMainThreadQueries().build();
    dao = inMemoryDb.gardenDao();
  }

  @After
  public void tearDown() {
    inMemoryDb.close();
  }

  @Test
  public void crud() {
    Garden testGarden = new Garden();
    testGarden.setDescription("test garden");
    dao.insertGarden(testGarden).blockingAwait();

    List<Garden> savedList = dao.getGardens().blockingFirst();
    assertNotNull(savedList);
    assertEquals(1, savedList.size());

    Garden saved = savedList.get(0);
    assertEquals("test garden", saved.getDescription());

    saved.setDescription("new description");
    dao.updateGarden(saved).blockingAwait();

    savedList = dao.getGardens().blockingFirst();
    assertNotNull(savedList);
    assertEquals(1, savedList.size());

    saved = savedList.get(0);
    assertEquals("new description", saved.getDescription());

    dao.deleteGarden(saved).blockingAwait();
    assertEquals(0, dao.getGardens().blockingFirst().size());
  }
}
