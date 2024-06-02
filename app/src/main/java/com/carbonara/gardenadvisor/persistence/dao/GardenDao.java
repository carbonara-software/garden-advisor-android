package com.carbonara.gardenadvisor.persistence.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;
import com.carbonara.gardenadvisor.persistence.entity.Garden;
import com.carbonara.gardenadvisor.persistence.entity.GardenWithPlants;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import java.util.List;

@Dao
public interface GardenDao {

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  Completable insertGarden(Garden garden);

  @Query("SELECT * FROM garden where id = :id")
  Flowable<Garden> getGarden(Long id);

  @Query("SELECT * FROM garden")
  Flowable<List<Garden>> getGardens();

  @Update
  Completable updateGarden(Garden garden);

  @Delete
  Completable deleteGarden(Garden garden);

  @Transaction
  @Query("SELECT * FROM garden")
  Flowable<List<GardenWithPlants>> getGardensWithPlants();
}
