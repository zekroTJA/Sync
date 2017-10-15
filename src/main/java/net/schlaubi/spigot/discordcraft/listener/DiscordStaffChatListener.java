package net.schlaubi.spigot.discordcraft.listener;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import net.schlaubi.spigot.discordcraft.Main;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class DiscordStaffChatListener extends ListenerAdapter {

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if(event.getAuthor().isBot()){
            return;
        }
        FileConfiguration cfg = Main.getConfiguration();
        Guild guild = event.getGuild();
        if(event.getTextChannel().equals(guild.getTextChannelById(cfg.getString("Channels.staffchannel")))){
            for(Player all : Bukkit.getOnlinePlayers()){
                if(all.hasPermission("system.staffchat")){
                    all.sendMessage("§9" + event.getAuthor().getName() + "> §7 " + event.getMessage().getContent());
                }
            }
        }
    }
}
