package net.schlaubi.spigot.discordcraft;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.TextChannel;

import java.awt.*;

public class EmbedSender {
    public static void sendEmbed(String message, TextChannel channel, Color color){
        channel.sendTyping().queue();
        channel.sendMessage(new EmbedBuilder().setDescription(message).setColor(color).build()).queue();
    }
}
