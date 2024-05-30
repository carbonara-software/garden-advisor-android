package com.alexinnocenzi.gardenadvisor.ai.prompt;

public class ConstPrompt {

    public static final String RETURN_WEATHER = "Given the weather and location data provide the following information in JSON format:\n" +
            " - a \"location\" object, containing the location data, such as name, region and country\n" +
            " - a \"weather\" object, containing the weather forecast (min and max temperatures and weather conditions for today and the next 6 days)\n"+
            " - the output json must respect the following schema or example:\n"+
            "{\n" +
            "  \"location\": {\n" +
            "    \"name\": \"Craco\",\n" +
            "    \"region\": \"Basilicata\",\n" +
            "    \"country\": \"Italy\"\n" +
            "  },\n" +
            "  \"weather\": {\n" +
            "    \"forecast\": [\n" +
            "      {\n" +
            "        \"date\": \"2024-05-17\",\n" +
            "        \"min_temp\": 12.4,\n" +
            "        \"max_temp\": 28.8,\n" +
            "        \"conditions\": \"Sunny\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"date\": \"2024-05-18\",\n" +
            "        \"min_temp\": 11.8,\n" +
            "        \"max_temp\": 25.8,\n" +
            "        \"conditions\": \"Partly cloudy with a chance of showers\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"date\": \"2024-05-19\",\n" +
            "        \"min_temp\": 13.6,\n" +
            "        \"max_temp\": 25.3,\n" +
            "        \"conditions\": \"Partly cloudy with a chance of showers\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"date\": \"2024-05-20\",\n" +
            "        \"min_temp\": 12.7,\n" +
            "        \"max_temp\": 24.6,\n" +
            "        \"conditions\": \"Mostly cloudy\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"date\": \"2024-05-21\",\n" +
            "        \"min_temp\": 14.8,\n" +
            "        \"max_temp\": 25.2,\n" +
            "        \"conditions\": \"Sunny and windy\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"date\": \"2024-05-22\",\n" +
            "        \"min_temp\": 12.7,\n" +
            "        \"max_temp\": 23.0,\n" +
            "        \"conditions\": \"Sunny and windy\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"date\": \"2024-05-23\",\n" +
            "        \"min_temp\": 12.1,\n" +
            "        \"max_temp\": 25.2,\n" +
            "        \"conditions\": \"Sunny\"\n" +
            "      }\n" +
            "    ]\n" +
            "  }\n" +
            "}" +
            "\n Important notice: DO NOT PRINT ANY THING ELSE BUT THE JSON ASKED as a plain text without any Markdown formatting please";
    public static final String RETURN_SUGGESTIONS = "Given this location and weather data, provide an answer in Json format with a list of recommended fruits, vegetables and flowers that can be planted at this time, with each value having:\n" +
            "\n" +
            "recommended: boolean value if can be planted now\n" +
            "positives: a list of the positive factors of the weather data\n" +
            "cautions: a list of what i need to be careful when planting\n" +
            "suggestions: a list of possible improvements to get a better result given some suggestions\n"+
            " the output json must respect the following schema or example:\n"+
            "{\n" +
            "  \"fruits\": [\n" +
            "    {\n" +
            "      \"name\": \"Tomatoes\",\n" +
            "      \"recommended\": true,\n" +
            "      \"positives\": [\n" +
            "        \"Warm soil temperatures consistently above 18°C\",\n" +
            "        \"Sunny days for most of the forecast period\",\n" +
            "        \"No frost expected\"\n" +
            "      ],\n" +
            "      \"cautions\": [\n" +
            "        \"Periods of high humidity and potential rain could increase disease risk (especially May 29th)\",\n" +
            "        \"Windy conditions on May 21st and 22nd could damage young plants\"\n" +
            "      ],\n" +
            "      \"suggestions\": [\n" +
            "        \"Consider using a cloche or protective cover for seedlings during windy days\",\n" +
            "        \"Monitor for signs of disease and apply appropriate treatments if needed\",\n" +
            "        \"Ensure good soil drainage to prevent root rot from excess moisture\"\n" +
            "      ]\n" +
            "    },\n" +
            "    {\n" +
            "      \"name\": \"Peppers\",\n" +
            "      \"recommended\": true,\n" +
            "      \"positives\": [\n" +
            "        \"Warm soil temperatures consistently above 18°C\",\n" +
            "        \"Sunny days for most of the forecast period\",\n" +
            "        \"No frost expected\"\n" +
            "      ],\n" +
            "      \"cautions\": [\n" +
            "        \"Similar to tomatoes, high humidity and rain can increase disease risk\",\n" +
            "        \"Windy conditions could damage young plants\" \n" +
            "      ],\n" +
            "      \"suggestions\": [\n" +
            "        \"Provide support (stakes or cages) for pepper plants as they grow\",\n" +
            "        \"Choose disease-resistant varieties if possible\",\n" +
            "        \"Space plants adequately for good air circulation\"\n" +
            "      ]\n" +
            "    },\n" +
            "    {\n" +
            "      \"name\": \"Strawberries\",\n" +
            "      \"recommended\": true,\n" +
            "      \"positives\": [\n" +
            "        \"Soil temperatures are warming up and consistently above 15°C\",\n" +
            "        \"Sunny periods provide good light for strawberry growth\"\n" +
            "      ],\n" +
            "      \"cautions\": [\n" +
            "        \"Strawberries prefer well-drained soil, ensure good drainage\",\n" +
            "        \"Periods of rain could lead to fruit rot if not managed\"\n" +
            "      ],\n" +
            "      \"suggestions\": [\n" +
            "        \"Plant in raised beds or mounds to improve drainage\",\n" +
            "        \"Use mulch around plants to suppress weeds and conserve moisture\",\n" +
            "        \"Consider netting to protect ripening fruit from birds\"\n" +
            "      ]\n" +
            "    }\n" +
            "  ],\n" +
            "  \"vegetables\": [\n" +
            "    {\n" +
            "      \"name\": \"Zucchini\",\n" +
            "      \"recommended\": true,\n" +
            "      \"positives\": [\n" +
            "        \"Warm soil temperatures ideal for zucchini\",\n" +
            "        \"Plenty of sunshine\"\n" +
            "      ],\n" +
            "      \"cautions\": [\n" +
            "        \"Zucchini needs consistent watering, especially during dry spells\",\n" +
            "        \"Can be susceptible to powdery mildew in humid conditions\"\n" +
            "      ],\n" +
            "      \"suggestions\": [\n" +
            "        \"Mulch to help retain soil moisture\",\n" +
            "        \"Space plants for good air circulation to prevent powdery mildew\"\n" +
            "      ]\n" +
            "    },\n" +
            "    {\n" +
            "      \"name\": \"Beans\",\n" +
            "      \"recommended\": true,\n" +
            "      \"positives\": [\n" +
            "        \"Warm soil temperatures are suitable for bean germination\",\n" +
            "        \"Plenty of sunshine will support bean growth\"\n" +
            "      ],\n" +
            "      \"cautions\": [\n" +
            "        \"Beans need well-drained soil\",\n" +
            "        \"Provide support for pole bean varieties\"\n" +
            "      ],\n" +
            "      \"suggestions\": [\n" +
            "        \"Direct sow beans as they dislike transplanting\",\n" +
            "        \"Water consistently, especially during flowering and pod development\"\n" +
            "      ]\n" +
            "    },\n" +
            "    {\n" +
            "      \"name\": \"Cucumbers\",\n" +
            "      \"recommended\": true,\n" +
            "      \"positives\": [\n" +
            "        \"Warm temperatures\",\n" +
            "        \"Plenty of sunshine\"\n" +
            "      ],\n" +
            "      \"cautions\": [\n" +
            "        \"Cucumbers need consistent watering\",\n" +
            "        \"Provide a trellis or support for climbing varieties\"\n" +
            "      ],\n" +
            "      \"suggestions\": [\n" +
            "        \"Mulch to retain soil moisture\",\n" +
            "        \"Train vines on a support for better air circulation and yield\"\n" +
            "      ]\n" +
            "    }\n" +
            "  ],\n" +
            "  \"flowers\": [\n" +
            "    {\n" +
            "      \"name\": \"Sunflowers\",\n" +
            "      \"recommended\": true,\n" +
            "      \"positives\": [\n" +
            "        \"Plenty of sunshine\",\n" +
            "        \"Warm soil temperature\"\n" +
            "      ],\n" +
            "      \"cautions\": [\n" +
            "        \"Sunflowers need well-drained soil\",\n" +
            "        \"Protect from strong winds, especially on May 21st and 22nd\"\n" +
            "      ],\n" +
            "      \"suggestions\": [\n" +
            "        \"Stake tall varieties for support\",\n" +
            "        \"Plant in a sheltered location\"\n" +
            "      ]\n" +
            "    },\n" +
            "    {\n" +
            "      \"name\": \"Cosmos\",\n" +
            "      \"recommended\": true,\n" +
            "      \"positives\": [\n" +
            "        \"Warm temperatures\",\n" +
            "        \"Plenty of sunshine\"\n" +
            "      ],\n" +
            "      \"cautions\": [\n" +
            "        \"Cosmos prefer well-drained soil\",\n" +
            "        \"Deadhead spent flowers to encourage continuous blooming\"\n" +
            "      ],\n" +
            "      \"suggestions\": [\n" +
            "        \"Direct sow cosmos seeds for best results\",\n" +
            "        \"Water regularly, especially during dry periods\"\n" +
            "      ]\n" +
            "    },\n" +
            "    {\n" +
            "      \"name\": \"Zinnias\",\n" +
            "      \"recommended\": true,\n" +
            "      \"positives\": [\n" +
            "        \"Warm temperatures\",\n" +
            "        \"Plenty of sunshine\"\n" +
            "      ],\n" +
            "      \"cautions\": [\n" +
            "        \"Zinnias are susceptible to powdery mildew, especially with the potential for rain and humidity\",\n" +
            "        \"Deadhead spent flowers to encourage more blooms\"\n" +
            "      ],\n" +
            "      \"suggestions\": [\n" +
            "        \"Space plants for good air circulation\",\n" +
            "        \"Water at the base of plants to avoid wetting leaves\",\n" +
            "        \"Consider using a preventative fungicide for powdery mildew\"\n" +
            "      ]\n" +
            "    }\n" +
            "  ]\n" +
            "}\n"+
            "\n Important notice: DO NOT PRINT ANY THING ELSE BUT THE JSON ASKED as a plain text without any Markdown formatting please";
}
