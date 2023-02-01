package com.skyscraper.bot;

import com.skyscraper.bot.command.*;
import com.skyscraper.bot.command.defaults.impl.CommandRegistry;
import com.skyscraper.bot.listener.GuildMemberJoinListener;
import com.skyscraper.bot.repository.UserRepository;
import com.skyscraper.bot.service.DiscordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties
@SpringBootApplication
public class ApplicationBootstrap implements CommandLineRunner {

  private final DiscordService botService;
  private final CommandRegistry commandRegistry;

  public ApplicationBootstrap(DiscordService botService, CommandRegistry commandRegistry) {
    this.botService = botService;
    this.commandRegistry = commandRegistry;
  }

  public static void main(String[] args) {
    SpringApplication.run(ApplicationBootstrap.class, args);
  }

  @Autowired
  private UserRepository userRepository;

  @Override
  public void run(String... args) throws Exception {
    this.botService.startBot();

    this.botService.registerListeners(
        new GuildMemberJoinListener(userRepository)
    );

    this.commandRegistry.registerByExecutors(
        new GitHubCommand(),
        new SayCommand(),
        new FactCommand(),
        new EightBallCommand(),
        new EchoCommand()
    );
  }

}
