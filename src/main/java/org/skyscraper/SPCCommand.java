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
import java.time.Instant;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

public class SPCCommand extends ListenerAdapter {	
	private void showOutlook(SlashCommandEvent event,boolean isPrivate) {
		String refURL = "https://www.spc.noaa.gov/products/activity_loop.gif?";
		String actor = event.getMember().getEffectiveName();
		
		MessageEmbed newEmbed = buildEmbed(actor, "Current Radar Outlook", "https://www.spc.noaa.gov/", refURL);
		
		event.replyEmbeds(newEmbed)
			.addActionRow(
					Button.secondary("day1otlk", "Day 1"),
					Button.secondary("day2otlk", "Day 2"),
					Button.secondary("day3otlk", "Day 3"),
					Button.secondary("day48otlk", "Day 4-8")
					)
		.setEphemeral(isPrivate).queue();
	}
	
	public CommandData getCommandData() {
		
		return new CommandData("spc","Get data from the NOAA Storm Prediction Center")
			.addOption(OptionType.BOOLEAN,"private","Whether or not the message is private (default: true)");
	}
	
	@Override
	public void onSlashCommand(SlashCommandEvent event) {
		
		if (event.getName().equals("spc")) {

			try {
				boolean isPrivate = event.getOption("private").getAsBoolean();
				showOutlook(event, isPrivate);
				
			} catch (NullPointerException err) {
				//event.reply("Missing arguments: Please refer to /help for more information.").setEphemeral(true).queue();
				showOutlook(event, false);
			}
		} else {
			return;
		}
	}
	
	/////// Embed builders
	private MessageEmbed extractEmbed(ButtonClickEvent event, String title, String day, String refURL) {
		MessageEmbed newEmbed = new EmbedBuilder()
				//Dynamic reference to resource name pointed within table
				.setColor(0x327DA8)
				.setTitle(title)
				.setDescription("Assembled by Skyscraper, data provided by NOAA\nOfficial Page: " + refURL)
				.setImage(extractImageURL(event, refURL, day) + Long.toString(Math.round(Math.random() * 100000))) //Append meaningless query to escape previously cached image
				.setFooter("Click below for projected storm development. Last updated by " + event.getMember().getEffectiveName())
				.setTimestamp(Instant.now())
				.build();
		
		return newEmbed;
	}
	
	public String extractImageURL(ButtonClickEvent event, String webpage, String day) {
		try {
			//Give me the HTML and it's resources
			Document document = Jsoup.connect(webpage).get();
			
			//Image is loaded based on website interaction, so get the reference to the image evoked within the interaction
			Element pointer = null;
			Elements buttons = document.select("a[onclick],td[onclick]");
			
			String[] eventData = event.getComponentId().split("\\?");
			String eventName = eventData[0];
			
			String PREFIX = "prob";
			String SUFFIX = "";
			String REGEX_DIRECTOR = "\'otlk_";
			
			switch (eventName) {
			case "tornado" :
				PREFIX = "prob";
				SUFFIX = "_torn";
				break;
			case "wind" :
				PREFIX = "prob";
				SUFFIX = "_wind";
				break;
			case "hail" :
				PREFIX = "prob";
				SUFFIX = "_hail";
				break;
			default: //Categorical
				PREFIX = "";
				SUFFIX = "";
				break;
			}
			
			for (int i = 0; i < buttons.size() - 1; i++) {
				if (buttons.get(i).attr("onclick").contains(REGEX_DIRECTOR)) { // Get default convective outlook
					pointer = buttons.get(i);
					break;
				}
			}
			//Build the URL
			String attribute = pointer.attr("onclick");
			String[] funcComponents = attribute.split("\'");
			String assembledURL = "https://www.spc.noaa.gov/products/outlook/" + day + PREFIX + funcComponents[1] + SUFFIX + ".gif?";
			
			return assembledURL;
			
		} catch (IOException err) {
			err.printStackTrace();
		}
		
        return "";
	}
	
	// Basic embed builder
	private MessageEmbed buildEmbed(String actor, String title, String descURL, String refURL) {
		MessageEmbed newEmbed = new EmbedBuilder()
				//Simple reference to their resource file
				.setColor(0x327DA8)
				.setTitle(title)
				.setDescription("Assembled by Skyscraper, data provided by NOAA\nOfficial Page: " + descURL)
				.setImage(refURL + Long.toString(Math.round(Math.random() * 100000))) //Append meaningless query to escape previously cached image
				.setFooter("Click below for projected storm development. Last updated by " + actor)
				.setTimestamp(Instant.now())
				.build();
		
		return newEmbed;
	}
	
	private void resetDisplay(ButtonClickEvent event) {
		String refURL = "https://www.spc.noaa.gov/products/activity_loop.gif?";
		String actor = event.getMember().getEffectiveName();
		
		MessageEmbed newEmbed = buildEmbed(actor, "Current Radar Outlook", "https://www.spc.noaa.gov/", refURL);
		
		rebuildMessage(event, event.editMessageEmbeds(newEmbed)).queue(); // Refer to current (last) event type, highlight button of last selection on return to main
	}
	
	private UpdateInteractionAction rebuildMessage(ButtonClickEvent event, UpdateInteractionAction action) {
		String eventName = event.getComponentId();
		String[] eventData = null;
		boolean isPrimaryMenu = true;
		
		if (eventName.contains("?")) {
			isPrimaryMenu = false;
			eventData = eventName.split("\\?");
			eventName = eventData[0];
		}
		if (isPrimaryMenu) {
			// Main menu
			return action.setActionRow(
					(eventName.equals("day1otlk") ? Button.primary("day1otlk", "Day 1") : Button.secondary("day1otlk", "Day 1")), // Redundant atm
					(eventName.equals("day2otlk") ? Button.primary("day2otlk", "Day 2") : Button.secondary("day2otlk", "Day 2")), // Redundant atm
					(eventName.equals("day3otlk") ? Button.primary("day3otlk", "Day 3") : Button.secondary("day3otlk", "Day 3")), // Redundant atm
					(eventName.equals("day48otlk") ? Button.primary("day48otlk", "Day 4-8") : Button.secondary("day48otlk", "Day 4-8")) // No categorical content; summarized only. Not as redundant! - B.
				);
		} else {
			// Selection menu
			String param = "";
			Document document = null;
			Elements buttons = null;
			
			if (eventData.length > 1) {
				param = eventData[1];
			}
			
			try {
				document = Jsoup.connect(param).get();
				buttons = document.select("a[onclick],td[onclick]");
			} catch (IOException err) {
				err.printStackTrace();
				resetDisplay(event);
			}
			
			String[] parsedURL = param.split("/");
			String pageName = parsedURL[parsedURL.length - 1];
			String day = pageName.split("otlk")[0];
			
			return action.setActionRow(
					Button.secondary("goBack","<< Back"),
					(eventName.equals("categorical") ? Button.success("categorical?" + param + "?" + day, "Categorical") : Button.secondary("categorical?" + param + "?" + day, "Categorical")),
					(eventName.equals("tornado") ? Button.success("tornado?" + param + "?" + day, "Tornado").withDisabled(!validIndex(buttons,"Tornado",1)) :Button.secondary("tornado?" + param + "?" + day, "Tornado").withDisabled(!validIndex(buttons,"Tornado",1))),
					(eventName.equals("wind") ? Button.success("wind?" + param + "?" + day, "Wind").withDisabled(!validIndex(buttons,"Wind",2)) : Button.secondary("wind?" + param + "?" + day, "Wind").withDisabled(!validIndex(buttons,"Wind",2))),
					(eventName.equals("hail") ? Button.success("hail?" + param + "?" + day, "Hail").withDisabled(!validIndex(buttons,"Hail",3)) : Button.secondary("hail?" + param + "?" + day, "Hail").withDisabled(!validIndex(buttons,"Hail",3)))
				);
		}
	}
	/////////// Secondary menu code
	boolean validIndex(Elements elements, String checkString, int index) {
		try {
			elements.get(index);
			if (checkString.equals(elements.get(index).text())) {
				return true;
			}
			return false;
			
		} catch (IndexOutOfBoundsException err) {
			return false;
		}
	}
	private UpdateInteractionAction focusOptions(ButtonClickEvent event, String day, String webpage, UpdateInteractionAction action) {
		try {
			Document document = Jsoup.connect(webpage).get();
			Elements buttons = document.select("a[onclick],td[onclick]");
			
			return action.setActionRow(
					Button.secondary("goBack","<< Back"),
					Button.success("categorical?" + webpage + "?" + day,"Categorical"),
					(Button.secondary("tornado?" + webpage + "?" + day,"Tornado").withDisabled(!validIndex(buttons,"Tornado",1))),
					(Button.secondary("wind?" + webpage + "?" + day,"Wind").withDisabled(!validIndex(buttons,"Wind",2))),
					(Button.secondary("hail?" + webpage + "?" + day,"Hail").withDisabled(!validIndex(buttons,"Hail",3)))
					);
			
		} catch (IOException err) {
			resetDisplay(event);
			err.printStackTrace();
		}
		return action.setActionRow(
				Button.danger("error", "Error!")
				);
	}
	/////// Main event handler
	@Override
	public void onButtonClick(ButtonClickEvent event) {
		String eventType = event.getComponentId();
		String actor = event.getMember().getEffectiveName();
		
		if (eventType.equals("day48otlk")) {
			String refURL = "https://www.spc.noaa.gov/products/exper/day4-8/day48prob.gif?";
			MessageEmbed newEmbed = buildEmbed(actor, "Day 4-8 Convective Outlook","https://www.spc.noaa.gov/products/exper/day4-8/",refURL);
			
			rebuildMessage(event, event.editMessageEmbeds(newEmbed)).queue();
			
		} else if (eventType.equals("day1otlk")){
			String refURL = "https://www.spc.noaa.gov/products/outlook/day1otlk.html";
			MessageEmbed newEmbed = extractEmbed(event, "Day 1 Convective Outlook (Today)", "day1", refURL);
			
			//rebuildMessage(event, event.editMessageEmbeds(newEmbed)).queue();
			focusOptions(event, "day1", refURL, event.editMessageEmbeds(newEmbed)).queue();
			
		}else if (eventType.equals("day2otlk")) {
			String refURL = "https://www.spc.noaa.gov/products/outlook/day2otlk.html";
			MessageEmbed newEmbed = extractEmbed(event, "Day 2 Convective Outlook (Tomorrow)", "day2", refURL);
			
			//rebuildMessage(event, event.editMessageEmbeds(newEmbed)).queue();
			focusOptions(event, "day2", refURL, event.editMessageEmbeds(newEmbed)).queue();
			
		}else if (eventType.equals("day3otlk")) {
			String refURL = "https://www.spc.noaa.gov/products/outlook/day3otlk.html";
			MessageEmbed newEmbed = extractEmbed(event, "Day 3 Convective Outlook", "day3", refURL);
			
			//rebuildMessage(event, event.editMessageEmbeds(newEmbed), null).queue();
			focusOptions(event, "day3", refURL, event.editMessageEmbeds(newEmbed)).queue();
		
		// Secondary Menu
		}else if (event.getComponentId().contains("?")){ // Using '?' as a delimiter for parameters
			String[] eventData = event.getComponentId().split("\\?");
			String eventName = eventData[0];
			String param = null;
			String day = null;
			
			if (eventData.length > 1) {
				param = eventData[1];
				day = eventData[2];
				String refURL = param;
				
				String title = day.substring(0,1).toUpperCase() + day.substring(1,3) + " " + day.substring(3,day.length()) + " " + eventName.substring(0,1).toUpperCase() + eventName.substring(1) + " Outlook";
				
				MessageEmbed newEmbed = extractEmbed(event, title, day, refURL);
				
				rebuildMessage(event, event.editMessageEmbeds(newEmbed)).queue();
			} else {
				resetDisplay(event);
			}
		// Fallback for testing purposes	
		} else {
			resetDisplay(event);
		}
	}
}
