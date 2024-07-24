package com.carbonara.gardenadvisor.util.task;

import android.content.Context;
import com.carbonara.gardenadvisor.persistence.AppDatabase;
import com.carbonara.gardenadvisor.persistence.dao.GardenDao;
import com.carbonara.gardenadvisor.persistence.dao.PlantDao;
import com.carbonara.gardenadvisor.persistence.entity.GardenWithPlants;
import com.carbonara.gardenadvisor.persistence.repository.GardenRepository;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.ObservableEmitter;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import io.reactivex.rxjava3.disposables.Disposable;
import java.util.List;

public class GardensWithPlantEmitter implements ObservableOnSubscribe<List<GardenWithPlants>> {

  private final Context c;
  private Disposable d;

  public GardensWithPlantEmitter(Context c) {
    this.c = c;
  }

  @Override
  public void subscribe(@NonNull ObservableEmitter<List<GardenWithPlants>> emitter)
      throws Throwable {
    GardenDao gDao = AppDatabase.getDatabase(c).gardenDao();
    PlantDao pDao = AppDatabase.getDatabase(c).plantDao();
    GardenRepository repo = new GardenRepository(gDao, pDao);
    d = repo.getGardensWithPlants().subscribe(emitter::onNext, emitter::tryOnError);
  }
}
