package org.skyscraper;

import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.components.Button;
import net.dv8tion.jda.api.requests.restaction.interactions.UpdateInteractionAction;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;

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
	
	public String extractImageURL(String webpage, String day) {
		try {
			//Give me the HTML and it's resources
			Document document = Jsoup.connect(webpage).get();
			
			//Image is loaded based on website interaction, so get the reference to the image evoked within the interaction
			Element pointer = null;
			
			Elements buttons = document.select("a[onclick],td[onclick]");
			for (int i = 0; i < buttons.size() - 1; i++) {
				if (buttons.get(i).attr("onclick").contains("\'otlk_")) {
					pointer = buttons.get(i);
					break;
				}
			}

			
			//Build the URL
			String attribute = pointer.attr("onclick");
			String[] funcComponents = attribute.split("\'");
			String assembledURL = "https://www.spc.noaa.gov/products/outlook/" + day + funcComponents[1] + ".gif?";
			
			return assembledURL;
			
		} catch (IOException err) {
			err.printStackTrace();
		}
		
        return "";
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
					//Dynamic reference to resource name pointed within table
					.setImage(extractImageURL("https://www.spc.noaa.gov/products/outlook/day2otlk.html", "day2") + Long.toString(Math.round(Math.random() * 100000))) //Append meaningless query to escape previously cached image
					.build();
			
			rebuildMessage(event, event.editMessageEmbeds(newEmbed)).queue();
		}else if (event.getComponentId().equals("day3otlk")) {
			MessageEmbed newEmbed = new EmbedBuilder()
					//Dynamic reference to resource name pointed within table
					.setImage(extractImageURL("https://www.spc.noaa.gov/products/outlook/day3otlk.html", "day3") + Long.toString(Math.round(Math.random() * 100000))) //Append meaningless query to escape previously cached image
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
