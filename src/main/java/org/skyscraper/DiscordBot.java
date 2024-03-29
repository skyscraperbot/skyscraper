package org.skyscraper;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

import javax.security.auth.login.LoginException;

public class DiscordBot {
    public static void main(String[] args) {
        // A token must be provided.
        if (args.length < 1) {
            throw new IllegalStateException("You have to provide a token as the first argument!");
        }

        /* Start the JDA bot builder, letting you provide the token externally rather
         * than writing it in your program's code. args[0] is the token. */
        JDABuilder jdaBotBuilder = JDABuilder.createDefault(args[0]);

        // Disable parts of the cache
        jdaBotBuilder.disableCache(CacheFlag.MEMBER_OVERRIDES, CacheFlag.VOICE_STATE);

        // Enable the bulk delete event - this means you'll have to handle it yourself!
        jdaBotBuilder.setBulkDeleteSplittingEnabled(false);

        // Set activity (like "playing Something")
        jdaBotBuilder.setActivity(Activity.watching("the weather"));

        // Set event listeners. Attach your class to the bot here!
        jdaBotBuilder.addEventListeners(new ReadyListener(), new WeatherCommand(), new ForecastCommand(), new AirQualityCommand(), new SPCCommand());

        try {
            // create the instance of JDA
            JDA discordBot = jdaBotBuilder.build();

            // optionally block until JDA is ready
            discordBot.awaitReady();

            // Register new slash commands
            CommandListUpdateAction commands = discordBot.updateCommands();
          
            commands.addCommands(
                new AirQualityCommand().getCommandData(),
                new WeatherCommand().getCommandData(),
                new ForecastCommand().getCommandData(),
                new SPCCommand().getCommandData()
            ).queue();

        } catch (LoginException | InterruptedException e) {
            System.err.println("Couldn't login.");
            e.printStackTrace();
        }
    }
}