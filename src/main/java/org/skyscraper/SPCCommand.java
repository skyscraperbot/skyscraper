package org.skyscraper;

import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

public class SPCCommand extends ListenerAdapter {
	private void showOutlook(SlashCommandEvent event,boolean isPrivate) {
		
		System.out.println("Making embed");
		
		MessageEmbed newEmbed = new EmbedBuilder()
				//Simple reference to their resource file
				.setImage("https://www.spc.noaa.gov/products/activity_loop.gif?" + Long.toString(Math.round(Math.random() * 100000))) //Append meaningless query to escape previously cached image
				//.setImage("https://www.spc.noaa.gov/products/activity_loop.gif")
				.build();
		
		event.replyEmbeds(newEmbed).queue();
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
				event.reply("Arguments:\n"
						+ "**type** - Return the type of requested information\n"
						+ "```\n"
						+ "help: Return a list of appropriate arguments\n"
						+ "outlook: Return the convective weather outlook\n"
						+ "```"
						+ "**private** - Make the destination message private\n"
						);
			case "outlook":
				showOutlook(event, isPrivate);
			default:
				event.reply("You must specify an information type! Refer to /help for information.").setEphemeral(true);
			}
		} else {
			return;
		}
	}
}
