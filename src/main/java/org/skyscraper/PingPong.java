package org.skyscraper;

import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class PingPong extends ListenerAdapter {
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        //TextChannel channel = event.getTextChannel();

        String text = event.getMessage().getContentDisplay();
        text = text.toLowerCase();

        System.out.println("text");

        if (text == "ping") {
            //channel.sendMessage("Pong");
            event.getTextChannel().sendMessage("Pong");
        }
    }
}