package net.engineeringdigest.journalApp.ApiResponse;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
public class WeatherRespose {

    public Current current;

    @Getter
    @Setter
    public class Current {

        public int temperature;

        @JsonProperty("weather_descriptions")
        public ArrayList<String> weatherDescriptions;

        public int feelslike;

    }

}

