package com.alexinnocenzi.gardenadvisor.ai.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Day {

	@JsonProperty("date")
	private String date;

	@JsonProperty("min_temp")
	private Object minTemp;

	@JsonProperty("max_temp")
	private Object maxTemp;

	@JsonProperty("conditions")
	private String conditions;
}