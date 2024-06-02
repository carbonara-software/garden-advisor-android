package com.carbonara.gardenadvisor.persistence.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.carbonara.gardenadvisor.persistence.entity.Garden;
import com.carbonara.gardenadvisor.persistence.entity.Plant;

import java.util.List;
import java.util.Set;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;

@Dao
public interface PlantDao {

    @Insert
    Completable insertPlant(Plant plant);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertPlants(Set<Plant> plant);

    @Query("SELECT * FROM plant where id = :id")
    Flowable<Garden> getPlant(Long id);

    @Query("SELECT * FROM plant")
    Flowable<List<Plant>> getPlants();

    @Update
    Completable updatePlant(Plant plant);

    @Delete
    Completable deletePlant(Plant plant);

}
