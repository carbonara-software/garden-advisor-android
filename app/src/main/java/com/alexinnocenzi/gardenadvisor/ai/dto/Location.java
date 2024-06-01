package com.alexinnocenzi.gardenadvisor.ai.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Location{

	@JsonProperty("country")
	private String country;

	@JsonProperty("name")
	private String name;

	@JsonProperty("region")
	private String region;
}