package org.skyscraper;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

public class AirPollutionFetcher {
    private final OkHttpClient httpClient = new OkHttpClient();
    private final Gson gson = new Gson();

    public JsonObject fetchAirPollutionData(String city, String apiKey) {
        try {
            String url = "http://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + apiKey;
            Request request = new Request.Builder().url(url).build();
            Response response = httpClient.newCall(request).execute();

            if (response.isSuccessful()) {
                JsonObject weatherData = gson.fromJson(response.body().string(), JsonObject.class);
                double lat = weatherData.getAsJsonObject("coord").get("lat").getAsDouble();
                double lon = weatherData.getAsJsonObject("coord").get("lon").getAsDouble();

                String airPollutionUrl = "http://api.openweathermap.org/data/2.5/air_pollution?lat=" + lat + "&lon=" + lon + "&appid=" + apiKey;
                Request airPollutionRequest = new Request.Builder().url(airPollutionUrl).build();
                Response airPollutionResponse = httpClient.newCall(airPollutionRequest).execute();

                if (airPollutionResponse.isSuccessful()) {
                    return gson.fromJson(airPollutionResponse.body().string(), JsonObject.class);
                } else {
                    return null;
                }
            } else {
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
