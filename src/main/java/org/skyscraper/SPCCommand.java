package org.skyscraper;

import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.components.Button;
import net.dv8tion.jda.api.requests.restaction.interactions.UpdateInteractionAction;
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
				).setEphemeral(isPrivate).queue();
	}
	
	private void showOutlook(SlashCommandEvent event,boolean isPrivate) {
		
		MessageEmbed newEmbed = new EmbedBuilder()
				//Simple reference to their resource file
				.setImage("https://www.spc.noaa.gov/products/activity_loop.gif?" + Long.toString(Math.round(Math.random() * 100000))) //Append meaningless query to escape previously cached image
				.build();
		
		event.replyEmbeds(newEmbed)
			.addActionRow(
					Button.primary("day1otlk", "Day 1"),
					Button.secondary("day2otlk", "Day 2"),
					Button.secondary("day3otlk", "Day 3"),
					Button.secondary("day48otlk", "Day 4-8")
					)
		.setEphemeral(isPrivate).queue();
	}
	
	public CommandData getCommandData() {
		
		return new CommandData("spc","Get data from the NOAA Storm Prediction Center")
			.addOption(OptionType.STRING,"type","What type of info to fetch, e.g. \'help\'")
			.addOption(OptionType.BOOLEAN,"private","Whether or not the message should be private");
	}
	
	@Override
	public void onSlashCommand(SlashCommandEvent event) {
		
		if (event.getName().equals("spc")) {

			try {
				System.out.println("Attempting Code");
				String type = event.getOption("type").getAsString();
				boolean isPrivate = event.getOption("private").getAsBoolean();
				
				switch (type) {
				case "help":
					showHelp(event, isPrivate);
					break;
				case "outlook":
					showOutlook(event, isPrivate);
					break;
				default:
					event.reply("You must specify an information type! Refer to /help for information.").setEphemeral(true).queue();
				}
			} catch (NullPointerException err) {
				event.reply("Missing arguments: Please refer to /help for more information.").setEphemeral(true).queue();
			}
		} else {
			return;
		}
	}
	
	private UpdateInteractionAction rebuildMessage(ButtonClickEvent event, UpdateInteractionAction action) {
		return action.setActionRow(
				(event.getComponentId().equals("day1otlk") ? Button.primary("day1otlk", "Day 1") : Button.secondary("day1otlk", "Day 1")),
				(event.getComponentId().equals("day2otlk") ? Button.primary("day2otlk", "Day 2") : Button.secondary("day2otlk", "Day 2")),
				(event.getComponentId().equals("day3otlk") ? Button.primary("day3otlk", "Day 3") : Button.secondary("day3otlk", "Day 3")),
				(event.getComponentId().equals("day48otlk") ? Button.primary("day48otlk", "Day 4-8") : Button.secondary("day48otlk", "Day 4-8"))
			);
	}
	
	@Override
	public void onButtonClick(ButtonClickEvent event) {
		
		if (event.getComponentId().equals("day48otlk")) {
			MessageEmbed newEmbed = new EmbedBuilder()
					//Simple reference to their resource file
					.setImage("https://www.spc.noaa.gov/products/exper/day4-8/day48prob.gif?" + Long.toString(Math.round(Math.random() * 100000))) //Append meaningless query to escape previously cached image
					.build();
			
			rebuildMessage(event, event.editMessageEmbeds(newEmbed)).queue();
			
		} else if (event.getComponentId().equals("day1otlk")){
			MessageEmbed newEmbed = new EmbedBuilder()
					//Simple reference to their resource file
					.setImage("https://www.spc.noaa.gov/products/activity_loop.gif?" + Long.toString(Math.round(Math.random() * 100000))) //Append meaningless query to escape previously cached image
					.build();
			
			rebuildMessage(event, event.editMessageEmbeds(newEmbed)).queue();
		}else if (event.getComponentId().equals("day2otlk")) {
			MessageEmbed newEmbed = new EmbedBuilder()
					//Simple reference to their resource file
					.setImage("https://www.spc.noaa.gov/products/outlook/day2otlk_1730.gif?" + Long.toString(Math.round(Math.random() * 100000))) //Append meaningless query to escape previously cached image
					.build();
			
			rebuildMessage(event, event.editMessageEmbeds(newEmbed)).queue();
		}else if (event.getComponentId().equals("day3otlk")) {
			MessageEmbed newEmbed = new EmbedBuilder()
					//Simple reference to their resource file
					.setImage("https://www.spc.noaa.gov/products/outlook/day3otlk_0730.gif?" + Long.toString(Math.round(Math.random() * 100000))) //Append meaningless query to escape previously cached image
					.build();
			
			rebuildMessage(event, event.editMessageEmbeds(newEmbed)).queue();
		} else {
			System.out.println("Testing!");
			event.editComponents().setActionRow(
					Button.danger("waar", "whar")
					)
			.queue();
		}
	}
}
