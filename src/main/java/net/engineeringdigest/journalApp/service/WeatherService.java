package net.engineeringdigest.journalApp.service;

import net.engineeringdigest.journalApp.ApiResponse.WeatherRespose;
import net.engineeringdigest.journalApp.Constants.Placeholders;
import net.engineeringdigest.journalApp.cache.AppCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class WeatherService {

    @Value("${weather.api.key}")
    private String apikey;

 //   private static final String API = "http://api.weatherstack.com/current?access_key=API_KEY&query=CITY";

    @Autowired
    public AppCache appCache;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private RedisService redisService;

    public WeatherRespose getWeather (String city){
        WeatherRespose weatherRespose = redisService.get("weather_of_" + city, WeatherRespose.class);
        if(weatherRespose != null){
            return weatherRespose;
        }
        else {
            String finalapi = appCache.appCache.get(AppCache.keys.WEATHER_API.toString()).replace(Placeholders.CITY, city).replace(Placeholders.API_KEY, apikey);

//        HttpHeaders httpHeaders = new HttpHeaders();
//        httpHeaders.set("key", "value");
//
//        String requestBody = "{\n" +
//                "    \"userName\": \"Ram\",\n" +
//                "    \"passWord\": \"Ram\"\n" +
//                "}";
//
//        UserDetails user = User.builder().username("vipul").password("vipul").build();
//
//        HttpEntity<UserDetails> httpentity = new HttpEntity<> (user,httpHeaders);

//        ResponseEntity<WeatherRespose> response = restTemplate.exchange(finalapi, HttpMethod.POST, httpentity, WeatherRespose.class);

            ResponseEntity<WeatherRespose> response = restTemplate.exchange(finalapi, HttpMethod.GET, null, WeatherRespose.class);
            WeatherRespose body = response.getBody();
            if (body != null){
                redisService.set("weather_of_" + city, body, 300l);
            }
            return body;
        }
    }
}
