package com.carbonara.gardenadvisor.ai.dto;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Weather{

	@JsonProperty("forecast")
	private List<Day> forecast;
}