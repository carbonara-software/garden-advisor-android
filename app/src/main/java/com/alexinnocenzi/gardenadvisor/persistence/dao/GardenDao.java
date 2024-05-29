package com.alexinnocenzi.gardenadvisor.persistence.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.alexinnocenzi.gardenadvisor.persistence.entity.Garden;
import com.alexinnocenzi.gardenadvisor.persistence.entity.GardenWithPlants;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;

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
