import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;

import java.awt.Color;

/**
 * Created by zekro on 15.10.2017 / 12:24
 * MCCore.PACKAGE_NAME
 * dev.zekro.de - github.zekro.de
 * © zekro 2017
 */
public class Msg {

    public static Message error(TextChannel channel, String content) {
        return channel.sendMessage(new EmbedBuilder().setDescription(content).setColor(Color.red).build()).complete();
    }

    public static Message success(TextChannel channel, String content) {
        return channel.sendMessage(new EmbedBuilder().setDescription(content).setColor(Color.green).build()).complete();
    }

    public static Message msg(TextChannel channel, String content, Color color) {
        return channel.sendMessage(new EmbedBuilder().setDescription(content).setColor(color).build()).complete();
    }

    public static Message msg(TextChannel channel, String content) {
        return channel.sendMessage(new EmbedBuilder().setDescription(content).build()).complete();
    }
}
