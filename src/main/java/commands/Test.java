package commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by zekro on 07.10.2017 / 12:05
 * MCCore.commands
 * dev.zekro.de - github.zekro.de
 * © zekro 2017
 */

public class Test implements CommandExecutor {


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (sender instanceof Player) {

            Player p = (Player) sender;

            p.sendMessage("Test!");

            return true;
        }

        return false;
    }
}
