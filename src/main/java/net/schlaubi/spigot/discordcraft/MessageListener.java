package net.schlaubi.spigot.discordcraft;

import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.json.JSONObject;

import java.awt.*;
import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
public class MessageListener extends ListenerAdapter{
    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }


    private String getNameFromUUID(String uuid) throws IOException {
        InputStream is = new URL("https://use.gameapis.net/mc/player/profile/" + uuid).openStream();

        try{
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsontext = readAll(rd);
            JSONObject json = new JSONObject(jsontext);
            return json.getString("name");
        } finally {
            is.close();
        }

    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        FileConfiguration cfg = Main.getConfiguration();
        if(event.getAuthor().isBot())
            return;

        if(event.isFromType(ChannelType.TEXT)){
            String msg = event.getMessage().getContent();
            String[] args = msg.split(" ");
            if(args[0].equalsIgnoreCase("+minecraft")){
                if(args.length > 1){
                    Message message = event.getMessage();
                    if(args[1].equalsIgnoreCase("info")){
                        if(message.getMentionedUsers().size() > 0){
                            if(!MySQL.userExists(message.getMentionedUsers().get(0).getId())){
                                EmbedSender.sendEmbed(cfg.getString("Messages.notfound"), event.getTextChannel(), Color.red);
                                return;
                            }
                            String uuid = MySQL.getValue(message.getMentionedUsers().get(0), "uuid");
                            String username = null;
                            try {
                                username = getNameFromUUID(uuid);
                                EmbedSender.sendEmbed(cfg.getString("Messages.info").replace("%user%", username), event.getTextChannel(), Color.cyan);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            if(!MySQL.userExists(event.getAuthor().getId())){
                                EmbedSender.sendEmbed(cfg.getString("Messages.notfound"), event.getTextChannel(), Color.red);
                                return;
                            }
                            String uuid = MySQL.getValue(event.getAuthor(), "uuid");
                            try {
                                String username = getNameFromUUID(uuid);
                                EmbedSender.sendEmbed(cfg.getString("Messages.info").replace("%user%", username), event.getTextChannel(), Color.cyan);
                            } catch (IOException e){
                                e.printStackTrace();
                            }
                        }
                    } else if(args[1].equalsIgnoreCase("link")){
                        if (args.length > 2){
                            if(MySQL.userExists(event.getAuthor().getId())){
                                EmbedSender.sendEmbed(cfg.getString("Messages.alreadylinked"), event.getTextChannel(), Color.red);
                                return;
                            }
                            event.getTextChannel().sendTyping().queue();
                            Message mymsg = event.getTextChannel().sendMessage(cfg.getString("Messages.fetching")).complete();
                            OfflinePlayer player = Bukkit.getOfflinePlayer(args[2]);
                            new Timer().schedule(new TimerTask() {

                                @Override
                                public void run() {
                                    if(player == null){
                                        mymsg.editMessage(cfg.getString("Messages.cantfind").replace("%user%", args[0])).queue();
                                    }
                                    UUID uuid = player.getUniqueId();
                                    if(MySQL.uuidExists(uuid)){
                                        mymsg.editMessage(cfg.getString("Messages.accountlinked").replace("%user%", player.getName())).queue();
                                        return;
                                    }
                                    MySQL.createUser(uuid, event.getAuthor().getId());
                                    mymsg.editMessage(cfg.getString("Messages.success").replace("%user%", player.getName())).queue();

                                }
                            }, 1000);

                        } else {
                            EmbedSender.sendEmbed(cfg.getString("Messages.usage"), event.getTextChannel(), Color.red);
                        }
                    } else if(args[1].equalsIgnoreCase("unlink")){
                        if(!MySQL.userExists(event.getAuthor().getId())){
                            event.getTextChannel().sendMessage(cfg.getString("Messages.notlinked")).queue();
                            return;
                        }
                        MySQL.deleteUser(event.getAuthor());
                        event.getTextChannel().sendMessage(cfg.getString("Messages.unlinked")).queue();
                    }
                } else {
                    EmbedSender.sendEmbed(cfg.getString("Messages.help").replace("%nl", "\n"), event.getTextChannel(), Color.cyan);
                }
            }
        }
    }

}
