package com.carbonara.gardenadvisor.persistence.repository;

import com.carbonara.gardenadvisor.persistence.dao.GardenDao;
import com.carbonara.gardenadvisor.persistence.dao.PlantDao;
import com.carbonara.gardenadvisor.persistence.entity.Garden;
import com.carbonara.gardenadvisor.persistence.entity.GardenWithPlants;
import com.carbonara.gardenadvisor.persistence.entity.Plant;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import java.util.List;
import java.util.stream.Collectors;

public class GardenRepository {

  private final GardenDao gardenDao;
  private final PlantDao plantDao;

  public GardenRepository(GardenDao gardenDao, PlantDao plantDao) {
    this.gardenDao = gardenDao;
    this.plantDao = plantDao;
  }

  public Flowable<List<GardenWithPlants>> getGardensWithPlants() {
    return gardenDao.getGardensWithPlants();
  }

  public Completable insertGarden(Garden garden) {
    return gardenDao.insertGarden(garden);
  }

  public Completable addPlantsToGarden(Long gardenId, List<Plant> plants) {
    return plantDao.insertPlants(
        plants.stream().peek(plant -> plant.setGardenId(gardenId)).collect(Collectors.toSet()));
  }

  public Completable deletePlant(Plant plant) {
    return plantDao.deletePlant(plant);
  }

  public Completable deleteGarden(Garden garden) {
    return gardenDao.deleteGarden(garden);
  }
}
