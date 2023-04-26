# Skyscraper Discord Weather Bot
![GitHub release (latest by date including pre-releases)](https://img.shields.io/github/v/release/skyscraperbot/skyscraper?include_prereleases)![GitHub Workflow Status](https://img.shields.io/github/actions/workflow/status/skyscraperbot/skyscraper/gradle.yml)![GitHub issues](https://img.shields.io/github/issues/skyscraperbot/skyscraper)![Discord](https://img.shields.io/discord/1065429350889037845?color=%235865F2)

Are you ever curious about what the outside world feels like while you're inside gaming?
Do you ever want to see into the future with a simple command?
Then look no further! Made using [JDA](https://github.com/DV8FromTheWorld/JDA) and the Gradle build tool, the Skyscraper discord bot
is a super simple tool, that allows you to look at the realtime weather and forecast.

## How can I add this to my Discord Server?
If you're interested in adding the Skyscraper Discord Weather Bot to your server, the most up-to-date version is always available to add by visiting our [website](https://skyscraperbot.github.io) and clicking **Add to Discord**.

## What do I need to develop?
A discord bot token. Check out the Discord [docs](https://discord.com/developers/docs/getting-started) on how to get started developing Discord bots.

## Getting Started
Getting started running the bot locally is very straight forward:
- Clone the repository from Github.
  ```bash
  git clone https://github.com/skyscraperbot/skyscraper
  ```
- Enter the project's directory.
  ```bash
  cd skyscraper/
  ```
- Build the project.
  ```bash
  ./gradlew clean build
  ```
- Run the project, using your discord bot token.
  ```bash
  ./gradlew run --args "your discord bot token here"
  ```

_Having issues using `gradlew`? Read [this](https://gist.github.com/lucasstarsz/9bbc306f8655b916367d557043e498ad)._

## Available Commands
Skyscraper provides four basic commands:
- /weather(city)
    Provides the current weather of the inputted city
- /forecast(city)
    Provides 12 hour tri-hourly forecast for inputted city
- /spc
    Provides nationwide weather radar for the entire country
- /aqi(city)
    Provides the Air Quality Index of the designated city


## Learn More

Checkout our [wiki](https://github.com/skyscraperbot/skyscraper/wiki) for more detail about each command and more information on how to get your development environment setup to contribute.