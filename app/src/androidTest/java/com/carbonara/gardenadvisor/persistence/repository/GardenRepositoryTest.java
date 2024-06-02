package com.carbonara.gardenadvisor.persistence.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import android.content.Context;

import androidx.room.Room;
import androidx.test.platform.app.InstrumentationRegistry;

import com.carbonara.gardenadvisor.persistence.AppDatabase;
import com.carbonara.gardenadvisor.persistence.dao.GardenDao;
import com.carbonara.gardenadvisor.persistence.dao.PlantDao;
import com.carbonara.gardenadvisor.persistence.entity.Garden;
import com.carbonara.gardenadvisor.persistence.entity.GardenWithPlants;
import com.carbonara.gardenadvisor.persistence.entity.Plant;
import com.carbonara.gardenadvisor.persistence.entity.PlantType;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class GardenRepositoryTest {

    private AppDatabase inMemoryDb;

    private GardenRepository gardenRepository;

    @Before
    public void setup() {
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        inMemoryDb = Room.inMemoryDatabaseBuilder(context, AppDatabase.class)
                .allowMainThreadQueries()
                .build();
        GardenDao gardenDao = inMemoryDb.gardenDao();
        PlantDao plantDao = inMemoryDb.plantDao();
        gardenRepository = new GardenRepository(gardenDao, plantDao);
    }

    @After
    public void tearDown() {
        inMemoryDb.close();
    }


    @Test
    public void crud() {
        Garden testGarden = new Garden();
        testGarden.setDescription("test garden");

        // Add new garden
        gardenRepository.insertGarden(testGarden).blockingAwait();

        List<GardenWithPlants> savedList = gardenRepository.getGardensWithPlants().blockingFirst();
        assertNotNull(savedList);
        assertEquals(1, savedList.size());
        GardenWithPlants saved = savedList.get(0);
        assertEquals("test garden", saved.getGarden().getDescription());
        assertEquals(Long.valueOf(1), saved.getGarden().getId());
        assertEquals(0, saved.getPlants().size());

        // Add plants to garden
        gardenRepository.addPlantsToGarden(1L, getPlants()).blockingAwait();

        savedList = gardenRepository.getGardensWithPlants().blockingFirst();
        assertNotNull(savedList);
        assertEquals(1, savedList.size());
        saved = savedList.get(0);
        assertEquals("test garden", saved.getGarden().getDescription());
        assertEquals(Long.valueOf(1), saved.getGarden().getId());
        assertEquals(2, saved.getPlants().size());

        // Remove a plant
        Optional<Plant> toBeRemoved = saved.getPlants().stream().findAny();
        assertTrue(toBeRemoved.isPresent());
        gardenRepository.deletePlant(toBeRemoved.get()).blockingAwait();

        savedList = gardenRepository.getGardensWithPlants().blockingFirst();
        assertNotNull(savedList);
        assertEquals(1, savedList.size());
        saved = savedList.get(0);
        assertEquals("test garden", saved.getGarden().getDescription());
        assertEquals(Long.valueOf(1), saved.getGarden().getId());
        assertEquals(1, saved.getPlants().size());

        // Remove the garden
        gardenRepository.deleteGarden(saved.getGarden()).blockingAwait();

        savedList = gardenRepository.getGardensWithPlants().blockingFirst();
        assertNotNull(savedList);
        assertEquals(0, savedList.size());
    }

    private List<Plant> getPlants() {
        Plant tomato = new Plant();
        tomato.setPlantName("Tomato");
        tomato.setType(PlantType.FRUIT);

        Plant sunflower = new Plant();
        sunflower.setPlantName("Sunflower");
        sunflower.setType(PlantType.FLOWER);

        return Arrays.asList(tomato, sunflower);
    }
}
