package com.skyscraper.bot.command.defaults.impl;

import java.lang.reflect.Method;
import com.skyscraper.bot.command.defaults.Command;
import com.skyscraper.bot.command.defaults.CommandExecutor;
import com.skyscraper.bot.command.defaults.CommandInfo;
import com.skyscraper.bot.service.DiscordService;

public class CommandRegistry {

  private final DiscordService botService;

  public CommandRegistry(DiscordService botService) {
    this.botService = botService;
  }

  public void registerByExecutors(CommandExecutor... commandExecutors) {
    for (CommandExecutor commandExecutor : commandExecutors) {
      Method[] methods = commandExecutor.getClass().getMethods();

      for (Method method : methods) {
        if (method.isAnnotationPresent(CommandInfo.class)) {
          CommandInfo commandInfo = method.getAnnotation(CommandInfo.class);

          Command command = new CommandBuilder()
              .withName(commandInfo.value())
              .withUsage(commandInfo.usage())
              .withMinArguments(commandInfo.minArguments())
              .withMaxArguments(commandInfo.maxArguments())
              .withCommandExecutor(commandExecutor)
              .build();

          this.botService.registerCommand(command);
        }
      }
    }
  }

}
