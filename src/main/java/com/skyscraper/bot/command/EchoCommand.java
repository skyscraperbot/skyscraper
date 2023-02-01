package com.skyscraper.bot.command;

import com.skyscraper.bot.command.defaults.CommandExecutor;
import com.skyscraper.bot.command.defaults.CommandInfo;
import com.skyscraper.bot.command.defaults.CommandSender;
import com.skyscraper.bot.component.DiscordMessage;
import org.springframework.stereotype.Component;
import java.awt.Color;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

@Component
public class EchoCommand implements CommandExecutor {

  @CommandInfo(value = "echo", minArguments = 1, maxArguments = 1, usage = "<message>")
  @Override
  public void execute(DiscordMessage dMessage, CommandSender commandSender) {

    MessageEmbed messageEmbed = new EmbedBuilder()
          .setColor(Color.decode("#ffffff"))
          .setTitle(dMessage.getSentBy() + " said..")
          .setDescription(dMessage.getData())
          .build();

    commandSender.sendEmbedMessage(messageEmbed);
  }
}
