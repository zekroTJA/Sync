package net.schlaubi.spigot.discordcraft.listener;

import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Role;
import net.milkbowl.vault.permission.Permission;
import net.schlaubi.spigot.discordcraft.Main;
import net.schlaubi.spigot.discordcraft.util.MySQL;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.HashMap;

public class JoinListener implements Listener{

    private static HashMap<String, String> roles = Main.roles;

    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        FileConfiguration cfg = Main.getConfiguration();
        Player player = e.getPlayer();
        if(!MySQL.uuidExists(player.getUniqueId())){
            player.kickPlayer(cfg.getString("Messages.notregistred").replace("&", "§"));
            return;
        }
        Permission perms = Main.getPermissions();
        Member member = Main.jda.getGuilds().get(0).getMemberById(MySQL.getValue(player, "discordid"));
        member.getRoles().forEach(i ->{
            if(roles.containsKey(i.getId())) {
                if (!perms.playerInGroup(player, roles.get(i.getId()))) {
                    perms.playerAddGroup(player, roles.get(i.getId()));
                    player.sendMessage(cfg.getString("Messages.gotrole").replace("%role%", roles.get(i.getId())).replace("&", "§"));
                }
            }
        });

        for(String role : perms.getPlayerGroups(e.getPlayer())){
            Role playerrole = Main.jda.getGuilds().get(0).getRoleById(cfg.getString("Roles." + role));
            if(!member.getRoles().contains(playerrole)){
                perms.playerRemoveGroup(e.getPlayer(), role);
            }
        }

    }
}
