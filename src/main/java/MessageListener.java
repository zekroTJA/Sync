import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.json.JSONObject;

import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;
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

    private void link(TextChannel chan, Member sender, String name) {

        if (MySQL.userExists(sender.getUser())) {
            Msg.error(chan, "You are still linked in the database!\nUse `!mc unlink` to unlink your profile.");
            return;
        }

        Msg.msg(chan, "Fetching username...");
        OfflinePlayer player = Bukkit.getOfflinePlayer(name);

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {

                if (player == null) {
                    Msg.error(chan, String.format("Player `%s` can not be found!", name));
                    return;
                }

                UUID uuid = player.getUniqueId();

                MySQL.createUser(uuid, sender.getUser().getId());
                try {
                    Msg.success(chan, String.format("Successfully linked account `%s` to your discord ID!", getNameFromUUID(uuid.toString())));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }, 1000);

    }

    private void unlink(TextChannel chan, Member sender) {

        if (!MySQL.userExists(sender.getUser())) {
            Msg.error(chan, "You don't have a linked account to unlink!");
            return;
        }
        MySQL.deleteUser(sender.getUser());
        Msg.success(chan, "Successfully unlinked your minecraft account!");
    }

    private void info(TextChannel chan) {
        Msg.msg(chan, "**USAGE:**\n" +
                "`!mc link <Minecraft Username>`  -  Links your minecraft profile to your discord ID\n" +
                "`!mc unlink`  -  Unlink your minecraft account");
    }


    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        FileConfiguration cfg = Main.getConfiguration();
        if(event.getAuthor().isBot())
            return;

        String content = event.getMessage().getContent();
        Member sender = event.getMember();
        TextChannel chan = event.getTextChannel();
        List<String> args = Arrays.asList(content.split(" ")).subList(1, content.split(" ").length);

        if (content.toLowerCase().startsWith("!mc")) {

            if (args.size() > 0) {

                switch (args.get(0)) {

                    case "set":
                    case "link":
                        link(chan, sender, args.get(1));
                        break;

                    case "unset":
                    case "unlink":
                        unlink(chan, sender);
                        break;

                    default:
                        info(chan);
                }
            }
            else
                info(chan);

        }

    }

}
