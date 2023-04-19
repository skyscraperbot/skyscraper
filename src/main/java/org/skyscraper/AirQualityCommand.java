package org.skyscraper;

import com.google.gson.JsonObject;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class AirQualityCommand extends ListenerAdapter {
    private final AirPollutionFetcher airPollutionFetcher = new AirPollutionFetcher();
    private final String apiKey = "25d7755b47deebeab26894827a6843f6";

    public CommandData getCommandData() {
        return new CommandData("airquality", "Get the air quality in a city")
                .addOption(OptionType.STRING, "city", "The city you want air quality for", true);
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

            try {
                JsonObject data = airPollutionFetcher.fetchAirPollutionData(city, apiKey);
                
                LocationConstructor locationConstructor = new LocationConstructor();
                String location = locationConstructor.getLocation(city, data.getAsJsonObject("coord").get("lat").getAsFloat(), data.getAsJsonObject("coord").get("lon").getAsFloat());

                JsonObject main = data.getAsJsonArray("list").get(0).getAsJsonObject().get("main").getAsJsonObject();
				JsonObject components = data.getAsJsonArray("list").get(0).getAsJsonObject().get("components").getAsJsonObject();
				int aqi = main.get("aqi").getAsInt();
				double pm25 = components.get("pm2_5").getAsDouble();
				double pm10 = components.get("pm10").getAsDouble();
				
				String muSym = "\u00B5";
				String cubeSym = "\u00B3";

				MessageEmbed embed = new EmbedBuilder()
				        .setTitle("Air Quality in " + location)
				        .addField("AQI", String.valueOf(aqi), true)
				        .addField("Concentration (" + muSym + "g/m" + cubeSym + ")", "PM2.5: " + pm25 + "\n" + "PM10: " + pm10, true)
				        .setFooter("Requested by " + event.getMember().getEffectiveName(), user.getEffectiveAvatarUrl())
				        .build();

				event.replyEmbeds(embed).queue();
            } catch (Exception e) {
                event.reply("An error occurred while fetching the air quality data.").queue();
            }
        } else {
            event.reply("Please provide a city name.").queue();
        }
    }
}
