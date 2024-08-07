package com.carbonara.gardenadvisor.ai.prompt;

public class ConstPrompt {

  public static final String PLAIN_TEXT_NOTICE =
      "Important: Return the response exclusively in plain text, with no Markdown formatting";

  public static final String WEATHER_PROMPT =
      "Given the weather and location data provide the following information in JSON format:\n"
          + " - a \"location\" object, containing the location data, such as name, region and country\n"
          + " - a \"weather\" object, containing the weather forecast (min and max temperatures and weather conditions for today and the next 6 days)\n"
          + " - for every weather object calculate the best icon to show between this 10 icons:\n"
          + "    - bad_clouds (1)\n"
          + "    - clouds (2)\n"
          + "    - clouds_thunder (3)\n"
          + "    - fog (4)\n"
          + "    - rain (5)\n"
          + "    - snow (6)\n"
          + "    - sun (7)\n"
          + "    - sun_cloud (8)\n"
          + "    - sun_cloud_rain (9)\n"
          + "    - thunder (10)\n"
          + " - the output json must respect the following JSON schema:\n"
          + "{\n"
          + "  \"location\": {\n"
          + "    \"name\": \"nameOfLocation\",\n"
          + "    \"region\": \"regionOfLocationIfFoundElseNull\",\n"
          + "    \"country\": \"countryOfLocationIfFoundElseNull\"\n"
          + "  },\n"
          + "  \"weather\": {\n"
          + "    \"todayForecast\": {\n"
          + "        \"date\": \"YYYY-MM-DD\",\n"
          + "        \"current_temp\": 0.0,\n"
          + "        \"min_temp\": 0.0,\n"
          + "        \"max_temp\": 0.0,\n"
          + "        \"icon\": number of icon to choose in ex: 2\n"
          + "        \"conditions\": \"WeatherConsditionsToParseFromDataPassed\"\n"
          + "      }\n"
          + "    \"forecast\": [\n"
          + "      {\n"
          + "        \"date\": \"YYYY-MM-DD\",\n"
          + "        \"current_temp\": 0.0,\n"
          + "        \"min_temp\": 0.0,\n"
          + "        \"max_temp\": 0.0,\n"
          + "        \"icon\": number of icon to choose in ex: 4\n"
          + "        \"conditions\": \"WeatherConsditionsToParseFromDataPassed\"\n"
          + "      }, ...\n"
          + "    ]\n"
          + "  }\n"
          + "}\n"
          + PLAIN_TEXT_NOTICE
          + "\nNotice that today is:";

  protected static final String BASE_SUGGESTION_PROMPT =
      "type: lowercase string value representing the type: fruit, vegetable, flower\n"
          + "recommended: boolean value if can be planted now\n"
          + "recommendedScore: an integer value score from 1 to 5 representing how recommended it is to plant given the weather conditions, the time of the year, the location and the kind of soil in the area"
          + "maintenanceScore: an integer value score from 1 to 5 representing how easy it is to maintain, for example how much water is needed, if pest control is necessary or if constant watch is required\n"
          + "positives: a list of the positive factors of the weather data\n"
          + "cautions: a list of what i need to be careful when planting\n"
          + "suggestions: a list of possible improvements to get a better result given some suggestions\n"
          + " the output json must respect the following JSON schema:\n"
          + "{\n"
          + "  \"fruits\": [\n"
          + "    {\n"
          + "      \"name\": \"NameOfFruit\",\n"
          + "      \"type\": \"fruit\",\n"
          + "      \"recommended\": true if is recommended else false,\n"
          + "      \"recommendedScore\": integer value from 1 to 5 of how recommended it is to plant,\n"
          + "      \"maintenanceScore\": integer value from 1 to 5 of how easy it is to maintain,\n"
          + "      \"positives\": [\n"
          + "        \"APositiveSuggestionBasedOnCurrentPlantAndCurrentOrFutureWeather\",\n..."
          + "      ],\n"
          + "      \"cautions\": [\n"
          + "        \"ACautionSuggestionBasedOnCurrentPlantAndCurrentOrFutureWeather\",\n..."
          + "      ],\n"
          + "      \"suggestions\": [\n"
          + "        \"AGeneralSuggestionBasedOnCurrentPlantAndCurrentOrFutureWeather\",\n..."
          + "      ]\n"
          + "    },\n...]"
          + "  \"vegetables\": [\n"
          + "    {\n"
          + "      \"name\": \"NameOfVegetable\",\n"
          + "      \"type\": \"vegetable\",\n"
          + "      \"recommended\": true if is recommended else false,\n"
          + "      \"recommendedScore\": integer value from 1 to 5 of how recommended it is to plant,\n"
          + "      \"maintenanceScore\": integer value from 1 to 5 of how easy it is to maintain,\n"
          + "      \"positives\": [\n"
          + "        \"APositiveSuggestionBasedOnCurrentPlantAndCurrentOrFutureWeather\",\n..."
          + "      ],\n"
          + "      \"cautions\": [\n"
          + "        \"ACautionSuggestionBasedOnCurrentPlantAndCurrentOrFutureWeather\",\n..."
          + "      ],\n"
          + "      \"suggestions\": [\n"
          + "        \"AGeneralSuggestionBasedOnCurrentPlantAndCurrentOrFutureWeather\",\n..."
          + "      ]\n"
          + "    },\n...]"
          + "  \"flowers\": [\n"
          + "    {\n"
          + "      \"name\": \"NameOfFlower\",\n"
          + "      \"type\": \"flower\",\n"
          + "      \"recommended\": true if is recommended else false,\n"
          + "      \"recommendedScore\": integer value from 1 to 5 of how recommended it is to plant,\n"
          + "      \"maintenanceScore\": integer value from 1 to 5 of how easy it is to maintain,\n"
          + "      \"positives\": [\n"
          + "        \"APositiveSuggestionBasedOnCurrentPlantAndCurrentOrFutureWeather\",\n..."
          + "      ],\n"
          + "      \"cautions\": [\n"
          + "        \"ACautionSuggestionBasedOnCurrentPlantAndCurrentOrFutureWeather\",\n..."
          + "      ],\n"
          + "      \"suggestions\": [\n"
          + "        \"AGeneralSuggestionBasedOnCurrentPlantAndCurrentOrFutureWeather\",\n..."
          + "      ]\n"
          + "    },\n...]"
          + "}\n"
          + PLAIN_TEXT_NOTICE;

  public static final String HOME_SUGGESTION_PROMPT =
      "Given this location and weather data, provide an answer in Json format with a list of recommended fruits, vegetables and flowers that can be planted at this time, with each value having:\n"
          + BASE_SUGGESTION_PROMPT;

  public static final String GARDEN_SUGGESTION_PROMPT =
      "Given this location, weather data and list of plants, evaluate each of the plants provided, with each value having:\n"
          + "name: the name of the plant provided in the json above\n"
          + BASE_SUGGESTION_PROMPT;

  public static final String CAMERA_SUGGESTION_PROMPT =
      "Help me identify this plant and its health.\n"
          + "if you can recognize it, provide the result in the following json format:\n"
          + "  {\n"
          + "  \"name\" : string containing the common name of the plant\n"
          + "  \"scientificName\" : string containing the scientific name of the plant,\n"
          + "  \"status\": \"healthy\" if the plant is well or \"needs_care\" if the plant happens to require extra care (for example if it is infected by a parasite,\n"
          + "  \"suggestions\": a list of suggestions to take care of the plant\n"
          + "}\n"
          + "\n"
          + "if you can't recognize the plant or the picture does not contain a plant, return an empty string.\n"
          + PLAIN_TEXT_NOTICE;

  public static final String CAMERA_GARDEN_PROMPT =
      "Given this location and weather data\n"
          + "if you can recognize the plant in the picture, provide the result in the following json format:\n"
          + "    {\n"
          + "      \"name\" : string containing the common name of the plant\n"
          + "      \"type\": string containing the values flower, fruit or vegetable depending on the type of plant,\n"
          + "      \"recommended\": true if is recommended to plant in this location else false,\n"
          + "      \"recommendedScore\": integer value from 1 to 5 of how recommended it is to plant,\n"
          + "      \"maintenanceScore\": integer value from 1 to 5 of how easy it is to maintain,\n"
          + "      \"positives\": a list of the positive factors of the weather data\n"
          + "      \"cautions\": a list of what i need to be careful when planting\n"
          + "      \"suggestions\": a list of possible improvements to get a better result given some suggestions\n"
          + "}"
          + "\n"
          + "if you can't recognize the plant or the picture does not contain a plant, return an empty string.\n"
          + PLAIN_TEXT_NOTICE;
}
