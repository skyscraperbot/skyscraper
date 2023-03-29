package org.skyscraper;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.awt.*;
import java.io.IOException;

public class AirQualityCommand extends ListenerAdapter {

    private final OkHttpClient httpClient = new OkHttpClient();
    private final Gson gson = new Gson();

    public CommandData getCommandData() {
        return new CommandData("airquality", "Get the air quality in a city")
                .addOption(OptionType.STRING, "city", "The city you want air quality for");
    }

    @Override
    public void onSlashCommand(SlashCommandEvent event) {
        if (!event.getName().equals("airquality")) {
            return;
        }

        User user = event.getUser();

        String cityOptionName = "city";
        if (!event.getOptions().isEmpty() && event.getOption(cityOptionName) != null) {
            String city = event.getOption(cityOptionName).getAsString();
            String apiKey = "25d7755b47deebeab26894827a6843f6";

            try {
                Request request = new Request.Builder()
                        .url("http://api.openweathermap.org/data/2.5/air_pollution?lat={latitude}&lon={longitude}&appid=" + apiKey)
                        .build();

                // Replace "{latitude}" and "{longitude}" with the appropriate values for the city.
                // You may need to use a separate API or library to get the latitude and longitude for the city name provided.

                Response response = httpClient.newCall(request).execute();

                if (response.isSuccessful()) {
                    JsonObject data = gson.fromJson(response.body().string(), JsonObject.class);

                    MessageEmbed embed = new EmbedBuilder()
                            .setColor(new Color(0x2F3136))
                            .setTitle("Air Quality in " + city)
                            .addField("AQI", String.valueOf(data.getAsJsonArray("list").get(0).getAsJsonObject().getAsJsonObject("main").get("aqi").getAsInt()), true)
                            .addField("Concentration (µg/m³)", "PM2.5: " + data.getAsJsonArray("list").get(0).getAsJsonObject().getAsJsonObject("components").get("pm2_5").getAsString() + "\n" + "PM10: " + data.getAsJsonArray("list").get(0).getAsJsonObject().getAsJsonObject("components").get("pm10").getAsString(), true)
                            .setFooter("Requested by " + user.getAsTag(), user.getEffectiveAvatarUrl())
                            .build();

                    event.replyEmbeds(embed).queue();
                } else {
                    event.reply("An error occurred while fetching the air quality data.").queue();
                }
            } catch (IOException e) {
                event.reply("An error occurred while fetching the air quality data.").queue();
            }
        } else {
            event.reply("Please provide a city name.").queue();
        }
    }
}
