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

public class ForecastCommand extends ListenerAdapter {

    private final OkHttpClient httpClient = new OkHttpClient();
    private final Gson gson = new Gson();

    public CommandData getCommandData() {
        return new CommandData("forecast", "Get the forecast in a city").addOption(OptionType.STRING,"city","The city you want weather for", true);
    }

    public String getDateFormat(String date){
        String[] split = date.split("\\s");
        String[] dateSplit = split[0].split("-");
        String time;
        switch(split[1]){
            case "00:00:00": time = "12 am";
                            break;
            case "03:00:00": time = "3 am";
                            break;
            case "06:00:00": time = "6 am";
                            break;
            case "09:00:00": time = "9 am";
                            break;
            case "12:00:00": time = "12 pm";
                            break;
            case "15:00:00": time = "3 pm";
                            break;
            case "18:00:00": time = "6 pm";
                            break;
            case "21:00:00": time = "9 pm";
                            break;
            default: time = "fail";
                            break;
        }
        return dateSplit[1] +"-"+ dateSplit[2] +", "+ time ;

    }


    @Override
    public void onSlashCommand(SlashCommandEvent event) {
        if (!event.getName().equals("forecast")) {
            return;
        }

        User user = event.getUser();

        String cityOptionName = "city";
        if (!event.getOptions().isEmpty() && event.getOption(cityOptionName) != null) {
            String city = event.getOption(cityOptionName).getAsString();
            String apiKey = "25d7755b47deebeab26894827a6843f6";

            try {
                Request request = new Request.Builder()
                        .url("https://api.openweathermap.org/data/2.5/forecast?q="+ city +"&units=imperial&appid=" + apiKey)
                        .build();

                Response response = httpClient.newCall(request).execute();

                if (response.isSuccessful()) {
                    JsonObject data = gson.fromJson(response.body().string(), JsonObject.class);
                     MessageEmbed embed = new EmbedBuilder()
                            .setColor(new Color(0x2F3136))
                            .setTitle("forecast " + city)
                            .addField("24 hour forecast", "", true)
                            .addField(getDateFormat(data.getAsJsonArray("list").get(0).getAsJsonObject().get("dt_txt").getAsString()), data.getAsJsonArray("list").get(0).getAsJsonObject().get("main").getAsJsonObject().get("temp").getAsString() + "°F" + " " + data.getAsJsonArray("list").get(0).getAsJsonObject().get("weather").getAsJsonArray().get(0).getAsJsonObject().get("description").getAsString(), true)
                            .addField(getDateFormat(data.getAsJsonArray("list").get(1).getAsJsonObject().get("dt_txt").getAsString()), data.getAsJsonArray("list").get(1).getAsJsonObject().get("main").getAsJsonObject().get("temp").getAsString() + "°F" + " " + data.getAsJsonArray("list").get(1).getAsJsonObject().get("weather").getAsJsonArray().get(0).getAsJsonObject().get("description").getAsString(), true)
                            .addField(getDateFormat(data.getAsJsonArray("list").get(2).getAsJsonObject().get("dt_txt").getAsString()), data.getAsJsonArray("list").get(2).getAsJsonObject().get("main").getAsJsonObject().get("temp").getAsString() + "°F" + " " + data.getAsJsonArray("list").get(2).getAsJsonObject().get("weather").getAsJsonArray().get(0).getAsJsonObject().get("description").getAsString(), true)
                            .addField(getDateFormat(data.getAsJsonArray("list").get(3).getAsJsonObject().get("dt_txt").getAsString()), data.getAsJsonArray("list").get(3).getAsJsonObject().get("main").getAsJsonObject().get("temp").getAsString() + "°F" + " " + data.getAsJsonArray("list").get(3).getAsJsonObject().get("weather").getAsJsonArray().get(0).getAsJsonObject().get("description").getAsString(), true)
                            .addField(getDateFormat(data.getAsJsonArray("list").get(4).getAsJsonObject().get("dt_txt").getAsString()), data.getAsJsonArray("list").get(4).getAsJsonObject().get("main").getAsJsonObject().get("temp").getAsString() + "°F" + " " + data.getAsJsonArray("list").get(4).getAsJsonObject().get("weather").getAsJsonArray().get(0).getAsJsonObject().get("description").getAsString(), true)
                            .addField(getDateFormat(data.getAsJsonArray("list").get(5).getAsJsonObject().get("dt_txt").getAsString()), data.getAsJsonArray("list").get(5).getAsJsonObject().get("main").getAsJsonObject().get("temp").getAsString() + "°F" + " " + data.getAsJsonArray("list").get(5).getAsJsonObject().get("weather").getAsJsonArray().get(0).getAsJsonObject().get("description").getAsString(), true)
                            .addField(getDateFormat(data.getAsJsonArray("list").get(6).getAsJsonObject().get("dt_txt").getAsString()), data.getAsJsonArray("list").get(6).getAsJsonObject().get("main").getAsJsonObject().get("temp").getAsString() + "°F" + " " + data.getAsJsonArray("list").get(6).getAsJsonObject().get("weather").getAsJsonArray().get(0).getAsJsonObject().get("description").getAsString(), true)
                            .addField(getDateFormat(data.getAsJsonArray("list").get(7).getAsJsonObject().get("dt_txt").getAsString()), data.getAsJsonArray("list").get(7).getAsJsonObject().get("main").getAsJsonObject().get("temp").getAsString() + "°F" + " " + data.getAsJsonArray("list").get(7).getAsJsonObject().get("weather").getAsJsonArray().get(0).getAsJsonObject().get("description").getAsString(), true)
                            .setFooter("Requested by " + user.getAsTag(), user.getEffectiveAvatarUrl())
                            .build();

                    event.replyEmbeds(embed).queue();
                } else {
                    event.reply("An error occurred while fetching the weather data.").queue();
                }
            } catch (IOException e) {
                event.reply("An error occurred while fetching the weather data.").queue();
            }
        } else {
            event.reply("Please provide a city name.").queue();
        }
    }
}