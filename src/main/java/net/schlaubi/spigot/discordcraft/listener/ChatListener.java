package net.schlaubi.spigot.discordcraft.listener;

import net.dv8tion.jda.core.entities.TextChannel;
import net.schlaubi.spigot.discordcraft.Main;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatListener implements Listener{

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e){
        FileConfiguration cfg = Main.getConfiguration();
        Player player = e.getPlayer();
        if(e.getMessage().startsWith("@")){
            if(player.hasPermission("system.staffchat")){
                e.setCancelled(true);
                String content = e.getMessage().replaceFirst("@", "");
                for(Player all : Bukkit.getOnlinePlayers()){
                    if(player.hasPermission("system.staffchat")){
                        all.sendMessage("§9" + player.getName() + "> §7" + content);
                    }
                }
                TextChannel staffchannel = Main.jda.getGuilds().get(0).getTextChannelById(cfg.getString("Channels.staffchannel"));
                staffchannel.sendMessage("**[" + player.getName() + "]** " + content).queue();
            }
        } else {
            TextChannel mcchannel = Main.jda.getGuilds().get(0).getTextChannelById(cfg.getString("Channels.minecraftchannel"));
            mcchannel.sendMessage("**[" + player.getName() + "]** > " + e.getMessage()).queue();
        }
    }
}
