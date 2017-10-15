package net.schlaubi.spigot.discordcraft.commands;

import net.dv8tion.jda.core.entities.TextChannel;
import net.schlaubi.spigot.discordcraft.Main;
import net.schlaubi.spigot.discordcraft.util.JsonMessage;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class CommandReport implements CommandExecutor {

    private JsonMessage report(Player target, String reason){
        return new JsonMessage()
                .append("§7[§4Report§7]§c The player §e")
                .save()
                .append(target.getName()).setHoverAsTooltip("Click to teleport")
                .setClickAsExecuteCmd("/tp " + target.getName()).save()
                .append("§c got reported for §e" + reason).save();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command name, String lable, String[] args) {
        if(sender instanceof Player){
            FileConfiguration cfg = Main.getConfiguration();
            Player player = (Player) sender;
            if(args.length > 1){
                Player target = Bukkit.getPlayer(args[0]);
                if(target != null){
                    if(!target.equals(player)) {
                        StringBuilder reason = new StringBuilder();
                        for (int i = 1; i < args.length; i++) {
                            reason.append(args[i]).append(" ");
                        }
                        player.sendMessage(cfg.getString("Messages.reported").replace("&", "§").replace("%player%", target.getName()).replace("%reason%", reason.toString()));
                        TextChannel channel = Main.jda.getGuilds().get(0).getTextChannelById(cfg.getString("Channels.reportchannel"));
                        channel.sendMessage("**" + sender.getName() + "** reported **" + target.getName() + "** for `" + reason.toString() + "`!").queue();
                        for (Player all : Bukkit.getOnlinePlayers()) {
                            if (all.hasPermission("system.report")) {
                                String playername = target.getName();
                                report(target, reason.toString()).send(all);
                            }
                        }
                    } else{
                        player.sendMessage(cfg.getString("Messages.cantreportself").replace("&", "§"));
                    }
                } else{
                    player.sendMessage(cfg.getString("Messages.reportplayernotfound").replace("&", "§"));
                }
            } else {
                player.sendMessage(cfg.getString("Messages.reporthelp").replace("&", "§"));
            }
        }

        return false;
    }
}
