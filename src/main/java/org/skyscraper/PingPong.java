package main.java.org.skyscraper; //Specify the package, which will almost always be this

import net.dv8tion.jda.api.entities.channel.concrete.TextChannel; //Remember to import libraries that you're using
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class PingPong extends ListenerAdapter {
    @Override
    public void onMessageReceived(MessageRecievedEvent event) {
        TextChannel channel = event.getTextChannel();

        String text = event.getMessage().getContentDisplay();
        text = text.toLowerCase();

        if (text == "ping") {
            channel.sendMessage("Pong");
        }
    }
}
