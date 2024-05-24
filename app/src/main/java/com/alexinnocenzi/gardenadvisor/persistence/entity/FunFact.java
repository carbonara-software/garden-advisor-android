package com.alexinnocenzi.gardenadvisor.persistence.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(tableName = "fun_facts")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FunFact {

    @PrimaryKey
    private Long id;

    private String fact;
}
