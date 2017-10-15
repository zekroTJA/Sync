package commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import utils.Statics;


/**
 * Created by zekro on 15.10.2017 / 15:41
 * MCCore.commands
 * dev.zekro.de - github.zekro.de
 * © zekro 2017
 */

public class Debug implements CommandExecutor {

    public static boolean debug = false;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (sender instanceof Player) {

            Player p = (Player) sender;

            debug = !debug;

            // COLOR CODES: http://wiki.ess3.net/mc/
            p.sendMessage(Statics.chatPrefix + "Set debug mode to &l&2" + debug);
        }

        return false;
    }

}
