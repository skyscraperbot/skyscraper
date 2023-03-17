package org.skyscraper;

import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.components.Button;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

public class SPCCommand extends ListenerAdapter {
	private void showHelp(SlashCommandEvent event,boolean isPrivate) {
		event.reply("Arguments:\n"
				+ "**type** - Return the type of requested information\n"
				+ "```\n"
				+ "help: Return a list of appropriate arguments\n"
				+ "outlook: Return the convective weather outlook\n"
				+ "```"
				+ "**private** - Make the destination message private\n"
				).queue();
	}
	
	private void showOutlook(SlashCommandEvent event,boolean isPrivate) {
		MessageEmbed newEmbed = new EmbedBuilder()
				//Simple reference to their resource file
				.setImage("https://www.spc.noaa.gov/products/activity_loop.gif?" + Long.toString(Math.round(Math.random() * 100000))) //Append meaningless query to escape previously cached image
				.build();
		
		event.replyEmbeds(newEmbed)
			.addActionRow(
					Button.secondary("day0otlk","Day 0"),
					Button.secondary("day1otlk", "Day 1"),
					Button.secondary("day2otlk", "Day 2"),
					Button.secondary("day3otlk", "Day 3"),
					Button.secondary("day48otlk", "Day 4-8")
					)
		.queue();
	}
	
	public CommandData getCommandData() {
		
		return new CommandData("spc","Get data from the NOAA Storm Prediction Center")
			.addOption(OptionType.STRING,"type","What type of info to fetch, e.g. \'help\'")
			.addOption(OptionType.BOOLEAN,"private","Whether or not the message should be private");
	}
	
	@Override
	public void onSlashCommand(SlashCommandEvent event) {
		System.out.println("Registered change 1");
		
		if (event.getName().equals("spc")) {
			String type = event.getOption("type").getAsString();
			boolean isPrivate = event.getOption("private").getAsBoolean();
			
			System.out.println(type);
			System.out.println(isPrivate);
			
			switch (type) {
			case "help":
				showHelp(event, isPrivate);
				break;
			case "outlook":
				showOutlook(event, isPrivate);
				break;
			default:
				event.reply("You must specify an information type! Refer to /help for information.").setEphemeral(true);
			}
		} else {
			return;
		}
	}
}
