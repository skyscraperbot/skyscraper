# Skyscraper Discord Weather Bot
Are you ever curious about what the outside world feels like while you're inside gaming? 
Do you ever want to see into the future with a simple command? 
Then look no further! Made using [JDA][1] and the Gradle build tool, the Skyscraper discord bot
is a super simple tool, that allows you to look at the realtime weather and forecast.


## Where's the code?
The code can be found in [`src/main/java/org/skyscraper`][2].


## What do I need to provide?
Nothing more than a discord bot token.


## Building and Running
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

_Having issues using `gradlew`? Read [this][3]._

## Commands
- /weather(city)
    Provides the current weather of the inputted city
- /forecast(city)
    Provides 12 hour tri-hourly forecast for inputted city
- /spc
    Provides nationwide weather radar for the entire country
- /aqi(city)
    Provides the Air Quality Index of the designated city
- /bookmark(city)
    
  


## What does this template do?
This template does the following:
- Displays a "ready for takeoff" message on the `ready` event.
- Prints out a chatter's message every time they speak in a channel visible to the bot.
- Prints out a chatter's message every time they Direct Message (DM) the bot


For more information on how to use JDA, [check out their github repository][1].

[1]: https://github.com/DV8FromTheWorld/JDA "The JDA github Repository"
[2]: https://github.com/skyscraperbot/skyscraper/tree/main/src/main/java/org/botexample "Template Source Code"
[3]: https://gist.github.com/lucasstarsz/9bbc306f8655b916367d557043e498ad "Terminals Access Files Differently"


## The Team
- Team Lead
    xcesiv
- Tech Lead
    Cyton
- The Grunts
    Animelord69
    TomatoGuard
    Matt D.