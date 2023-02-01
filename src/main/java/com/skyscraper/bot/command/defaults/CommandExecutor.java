package com.skyscraper.bot.command.defaults;

import com.skyscraper.bot.component.DiscordMessage;

public interface CommandExecutor {

  void execute(DiscordMessage dMessage, CommandSender commandSender);

}
