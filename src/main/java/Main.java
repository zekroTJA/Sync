import commands.Debug;
import commands.Test;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.exceptions.RateLimitedException;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import javax.security.auth.login.LoginException;
import java.io.File;
import java.util.HashMap;

/**
 * Created by zekro on 07.10.2017 / 11:47
 * MCCore.PACKAGE_NAME
 * dev.zekro.de - github.zekro.de
 * © zekro 2017
 */

public class Main extends JavaPlugin {

    public static JDA jda;
    public static Permission perms;

    public static HashMap<String, String> roles = new HashMap<>();



    @Override
    public void onEnable() {
        loadConfig();
        startBot();
        MySQL.connect();
        MySQL.createDatabase();
        Bukkit.getPluginManager().registerEvents(new JoinListener(),this);
        setupPermissions();

        // COMMANDS
        this.getCommand("debug").setExecutor(new Debug());
    }

    private void setupPermissions() {
        RegisteredServiceProvider<Permission> rsp = Bukkit.getServicesManager().getRegistration(Permission.class);
        perms = rsp.getProvider();
    }

    public static Permission getPermissions(){
        return  perms;
    }

    private void startBot() {
        FileConfiguration cfg = getConfiguration();
        JDABuilder bot = new JDABuilder(AccountType.BOT);
        bot.setToken(cfg.getString("Discord.token"));
        bot.setGame(Game.of(cfg.getString("Discord.game")));
        bot.addEventListener(new MessageListener());

        try {
            jda = bot.buildBlocking();
        } catch (LoginException | InterruptedException | RateLimitedException e) {
            Bukkit.getConsoleSender().sendMessage("§4§l[DiscordSync] Unable to connect to Discord check your config.yml");
            e.printStackTrace();
        }
    }

    private void loadConfig() {
        File f = new File("plugins/DiscordCraft", "config.yml");
        FileConfiguration cfg = YamlConfiguration.loadConfiguration(f);
        saveDefaultConfig();

        cfg.getConfigurationSection("Roles").getKeys(false).forEach(i -> roles.put(cfg.getString("Roles." + i), i));
    }

    public static FileConfiguration getConfiguration(){
        File f = new File("plugins/DiscordCraft", "config.yml");
        return YamlConfiguration.loadConfiguration(f);
    }

    @Override
    public void onDisable() {
        MySQL.disconnect();
        jda.shutdown();
    }
}
