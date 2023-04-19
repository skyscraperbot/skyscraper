package org.skyscraper;

import java.io.IOException;
import java.util.Locale;

import com.google.gson.Gson;
import com.google.gson.JsonArray;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class LocationConstructor {
    private final OkHttpClient httpClient = new OkHttpClient();
    private final Gson gson = new Gson();
	
	public String getLocation(String place, Float latitude, Float longitude) {
		
		String location = "??";
		String apiKey = "25d7755b47deebeab26894827a6843f6";
		
		try { // Pulling the latitude and longitude from our other command :)
			Request request = new Request.Builder()
					.url("http://api.openweathermap.org/geo/1.0/reverse?lat=" + latitude + "&lon=" + longitude + "&limit=" + 3 + "&appid=" + apiKey)
                    .build();

            Response response = httpClient.newCall(request).execute();
            
            if (response.isSuccessful()) {
            	JsonArray data = gson.fromJson(response.body().string(), JsonArray.class);
            	String cityName = null;
            	String stateName = null;
            	String countryCode = null;
            	String countryName = null;
            	
            	for (int i = 0; i < data.size(); i++) {
            		cityName = data.get(i).getAsJsonObject().get("name").getAsString();
            		stateName = data.get(i).getAsJsonObject().get("state").getAsString();
            		countryCode = data.get(i).getAsJsonObject().get("country").getAsString();
            		
            		Locale l = new Locale("", countryCode);
            		countryName = l.getDisplayCountry(); // Get country name with Locale
            		
            		if (place.toLowerCase().trim().equals(cityName.toLowerCase().trim())) {
            			break;
            		}
            	}
            	if (cityName == null) {
            		return place;
            	} else {
            		location = cityName + ", " + (stateName != null ? stateName : countryName); // Tell us the country if no state is available in the data
            	}
            	
            } else {
            	return place; // If all else fails, just print back the user's input
            }
		} catch (IOException err) {
			return place;
		}
		
		return location;
	}
}
