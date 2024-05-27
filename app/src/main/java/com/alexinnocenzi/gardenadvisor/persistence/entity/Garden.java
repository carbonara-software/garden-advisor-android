package com.alexinnocenzi.gardenadvisor.persistence.entity;

import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(tableName = "garden")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Garden {
    @PrimaryKey
    private Long id;

    private String description;

    @Embedded
    private Location location;
}
