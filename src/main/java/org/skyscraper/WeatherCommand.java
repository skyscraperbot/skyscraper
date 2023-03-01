package org.skyscraper;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.awt.*;
import java.io.IOException;

public class WeatherCommand extends ListenerAdapter {

    private final OkHttpClient httpClient = new OkHttpClient();
    private final Gson gson = new Gson();

    public CommandData getCommandData() {
        return new CommandData("weather", "Get the weather in a city");
    }

    @Override
    public void onSlashCommand(SlashCommandEvent event) {
        if (!event.getName().equals("weather")) {
            return;
        }

        User user = event.getUser();

        String city = event.getOption("city").getAsString();
        String apiKey = "25d7755b47deebeab26894827a6843f6";

        try {
            Request request = new Request.Builder()
                    .url("https://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + apiKey)
                    .build();

            Response response = httpClient.newCall(request).execute();

            if (response.isSuccessful()) {
                JsonObject data = gson.fromJson(response.body().string(), JsonObject.class);

                MessageEmbed embed = new EmbedBuilder()
                        .setColor(new Color(0x2F3136))
                        .setTitle("Weather in " + city)
                        .addField("Temperature", data.getAsJsonObject("main").get("temp").getAsString() + "°F", true)
                        .addField("Feels like", data.getAsJsonObject("main").get("feels_like").getAsString() + "°F", true)
                        .addField("Description", data.getAsJsonArray("weather").get(0).getAsJsonObject().get("description").getAsString(), true)
                        .setFooter("Requested by " + user.getAsTag(), user.getEffectiveAvatarUrl())
                        .build();

                event.replyEmbeds(embed).queue();
            } else {
                event.reply("An error occurred while fetching the weather data.").queue();
            }
        } catch (IOException e) {
            event.reply("An error occurred while fetching the weather data.").queue();
        }
    }
}
