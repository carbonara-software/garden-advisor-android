package com.alexinnocenzi.gardenadvisor.persistence.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.alexinnocenzi.gardenadvisor.persistence.entity.FunFact;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;

@Dao
public interface FunFactDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    Completable insertFunFacts(List<FunFact> users);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertFunFact(FunFact users);

    @Query("SELECT * FROM fun_facts")
    Flowable<List<FunFact>> getFunFacts();

    @Query("SELECT * FROM fun_facts WHERE id = :id")
    Flowable<FunFact> getFunFact(Long id);

    @Query("SELECT fact FROM fun_facts ORDER BY RANDOM() LIMIT 1;")
    Flowable<FunFact> getRandomFunFact();
}
