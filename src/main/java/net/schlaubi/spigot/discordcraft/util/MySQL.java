package net.schlaubi.spigot.discordcraft.util;

import net.dv8tion.jda.core.entities.User;
import net.schlaubi.spigot.discordcraft.Main;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.sql.*;
import java.util.UUID;

public class MySQL {
    private static Connection connection;
    public static void connect(){
        FileConfiguration cfg = Main.getConfiguration();
        String host = cfg.getString("MySQL.host");
        Integer port = cfg.getInt("MySQL.port");
        String user = cfg.getString("MySQL.user");
        String database = cfg.getString("MySQL.database");
        String password = cfg.getString("MySQL.password");

        try {
            connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database + "?autoReconnect=true&autoReconnectForPools=true&interactiveClient=true&characterEncoding=UTF-8", user, password);
            Bukkit.getConsoleSender().sendMessage("§a§l[DiscordCraft]MySQL connection success");
        } catch (SQLException e) {
            Bukkit.getConsoleSender().sendMessage("§4§l[DiscordCraft]MySQL connection failed");
            e.printStackTrace();
        }
    }
    private static boolean isConnected() {
        return connection != null;
    }

    public static void disconnect(){
        if(isConnected())
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
    }

    public static void createDatabase()
    {
        try
        {
            if (connection.isClosed()) {
                connect();
            }
            PreparedStatement ps = connection.prepareStatement("CREATE TABLE IF NOT EXISTS discordcraft( `id` INT NOT NULL AUTO_INCREMENT , `uuid` TEXT NOT NULL , `discordid` TEXT NOT NULL , PRIMARY KEY (`id`)) ENGINE = InnoDB;");
            ps.execute();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    public static boolean uuidExists(UUID uuid)
    {
        try
        {
            if (connection.isClosed()) {
                connect();
            }
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM discordcraft WHERE uuid =?");
            ps.setString(1, uuid.toString());
            System.out.println("UUDID!!!!!!!" + uuid.toString());
            ResultSet rs = ps.executeQuery();
            return rs.next();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean userExists(User user)
    {
        try
        {
            if (connection.isClosed()) {
                connect();
            }
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM discordcraft WHERE discordid =?");
            ps.setString(1, user.getId());
            ResultSet rs = ps.executeQuery();
            return rs.next();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return false;
    }
    public static void createUser(UUID uuid, String identity)
    {
        try
        {
            if (connection.isClosed()) {
                connect();
            }
            PreparedStatement ps = connection.prepareStatement("INSERT INTO discordcraft(`uuid`,`discordid`) VALUES (?, ?)");
            ps.setString(1, uuid.toString());
            ps.setString(2, identity);
            ps.execute();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    public static String getValue(Player player, String type)
    {
        try
        {
            if (connection.isClosed()) {
                connect();
            }
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM discordcraft WHERE uuid = ?");
            ps.setString(1, player.getUniqueId().toString());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                return rs.getString(type);
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return null;
    }
    public static String getValue(User user, String type)
    {
        try
        {
            if (connection.isClosed()) {
                connect();
            }
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM discordcraft WHERE discordid = ?");
            ps.setString(1, user.getId());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString(type);
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return null;
    }


    public static void deleteUser(User user)
    {
        try
        {
            if (connection.isClosed()) {
                connect();
            }
            PreparedStatement ps = connection.prepareStatement("DELETE FROM discordcraft WHERE discordid=?");
            ps.setString(1, user.getId());
            ps.execute();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

}
