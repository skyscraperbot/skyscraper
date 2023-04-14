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
	
	private MessageEmbed extractEmbed(String title, String descURL, String refURL) {
		MessageEmbed newEmbed = new EmbedBuilder()
				//Dynamic reference to resource name pointed within table
				.setColor(0x327DA8)
				.setTitle(title)
				.setDescription("Assembled by Skyscraper, data provided by NOAA\nOfficial Page: " + descURL)
				.setImage(extractImageURL("https://www.spc.noaa.gov/products/outlook/day3otlk.html", "day3") + Long.toString(Math.round(Math.random() * 100000))) //Append meaningless query to escape previously cached image
				.build();
		
		return newEmbed;
	}
	
	private MessageEmbed buildEmbed(String title, String descURL, String refURL) {
		MessageEmbed newEmbed = new EmbedBuilder()
				//Simple reference to their resource file
				.setColor(0x327DA8)
				.setTitle(title)
				.setDescription("Assembled by Skyscraper, data provided by NOAA\nOfficial Page: " + descURL)
				.setImage(refURL + Long.toString(Math.round(Math.random() * 100000))) //Append meaningless query to escape previously cached image
				.build();
		
		return newEmbed;
	}
	
	private void showOutlook(SlashCommandEvent event,boolean isPrivate) {
		String refURL = "https://www.spc.noaa.gov/products/activity_loop.gif?";
		
		MessageEmbed newEmbed = buildEmbed("Day 1 Convective Outlook", "https://www.spc.noaa.gov/", refURL);
		
//		MessageEmbed newEmbed = new EmbedBuilder()
//				//Simple reference to their resource file
//				.setColor(0x0099FF)
//				.setTitle("Day 1 Convective Outlook")
//				.setDescription("Assembled by Skyscraper, data provided by NOAA\nOfficial Page: " + URL)
//				.setImage("https://www.spc.noaa.gov/products/activity_loop.gif?" + Long.toString(Math.round(Math.random() * 100000))) //Append meaningless query to escape previously cached image
//				.build();
		
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
		String eventName = event.getComponentId();
		if (eventName.equals("goBack") || eventName.equals("error")) {
			return action.setActionRow(
					Button.primary("day1otlk", "Day 1"),
					Button.secondary("day2otlk", "Day 2"),
					Button.secondary("day3otlk", "Day 3"),
					Button.secondary("day48otlk", "Day 4-8")
				);
		}
		
		return action.setActionRow(
				(eventName.equals("day1otlk") ? Button.primary("day1otlk", "Day 1") : Button.secondary("day1otlk", "Day 1")),
				(eventName.equals("day2otlk") ? Button.primary("day2otlk", "Day 2") : Button.secondary("day2otlk", "Day 2")),
				(eventName.equals("day3otlk") ? Button.primary("day3otlk", "Day 3") : Button.secondary("day3otlk", "Day 3")),
				(eventName.equals("day48otlk") ? Button.primary("day48otlk", "Day 4-8") : Button.secondary("day48otlk", "Day 4-8"))
			);
	}
	
	private UpdateInteractionAction focusOptions(ButtonClickEvent event, String webpage, UpdateInteractionAction action) {
		try {
			Document document = Jsoup.connect(webpage).get();
			Elements buttons = document.select("a[onclick],td[onclick]");
			
			for (int i = 0; i < buttons.size(); i++) {
				System.out.println(buttons.get(i).text());
			}
			
			return action.setActionRow(
					Button.secondary("goBack","<< Back"),
					Button.success(buttons.get(0).text().toLowerCase(),buttons.get(0).text()),
					Button.secondary(buttons.get(1).text().toLowerCase(),buttons.get(1).text()),
					Button.secondary(buttons.get(2).text().toLowerCase(),buttons.get(2).text()),
					Button.secondary(buttons.get(3).text().toLowerCase(),buttons.get(3).text())
					);
			
		} catch (IOException err) {
			// TODO Auto-generated catch block
			err.printStackTrace();
		}
		
		return action.setActionRow(
				Button.danger("error", "Error!")
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
			String refURL = "https://www.spc.noaa.gov/products/exper/day4-8/day48prob.gif?";
			MessageEmbed newEmbed = buildEmbed("Day 4-8 Convective Outlook","https://www.spc.noaa.gov/products/exper/day4-8/",refURL);
			
			rebuildMessage(event, event.editMessageEmbeds(newEmbed)).queue();
			
		} else if (event.getComponentId().equals("day1otlk")){
			String refURL = "https://www.spc.noaa.gov/products/activity_loop.gif?";
			MessageEmbed newEmbed = buildEmbed("Day 1 Convective Outlook", "https://www.spc.noaa.gov/products/outlook/day1otlk.html", refURL);
			
			rebuildMessage(event, event.editMessageEmbeds(newEmbed)).queue();
			
		}else if (event.getComponentId().equals("day2otlk")) {
			String refURL = "https://www.spc.noaa.gov/products/outlook/day2otlk.html";
			MessageEmbed newEmbed = extractEmbed("Day 2 Convective Outlook", refURL, refURL);
			
			//rebuildMessage(event, event.editMessageEmbeds(newEmbed)).queue();
			focusOptions(event, refURL, event.editMessageEmbeds(newEmbed)).queue();
		}else if (event.getComponentId().equals("day3otlk")) {
			String refURL = "https://www.spc.noaa.gov/products/outlook/day3otlk.html";
			MessageEmbed newEmbed = extractEmbed("Day 3 Convective Outlook", refURL, refURL);
			
			rebuildMessage(event, event.editMessageEmbeds(newEmbed)).queue();
		} else {
			// Copy of day1otlk for reset purposes
			String refURL = "https://www.spc.noaa.gov/products/activity_loop.gif?";
			MessageEmbed newEmbed = buildEmbed("Day 1 Convective Outlook", "https://www.spc.noaa.gov/", refURL);
			
			rebuildMessage(event, event.editMessageEmbeds(newEmbed)).queue();
		}
	}
}
