package de.turtle_exception.client.api.entities.messages;

import de.turtle_exception.client.api.entities.Turtle;
import de.turtle_exception.client.internal.data.annotations.*;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.channel.middleman.GuildMessageChannel;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@Resource(path = "channels", builder = "buildSyncChannel")
@SuppressWarnings("unused")
public interface SyncChannel extends Turtle {
    @Key(name = Keys.Messages.SyncChannel.DISCORD, relation = Relation.ONE_TO_MANY, type = Long.class, sqlType = Types.Messages.SyncChannel.DISCORD)
    @Relational(table = "channel_discord", self = "channel", foreign = "discord")
    @NotNull List<Long> getDiscordChannelIds();

    default List<GuildMessageChannel> getDiscordChannels() throws IllegalStateException {
        ArrayList<GuildMessageChannel> list = new ArrayList<>();
        JDA jda = getClient().getJDA();

        if (jda == null)
            throw new IllegalStateException("No JDA instance registered");

        for (Long discordChannelId : this.getDiscordChannelIds()) {
            GuildMessageChannel channel = jda.getChannelById(GuildMessageChannel.class, discordChannelId);
            if (channel == null) continue;
            list.add(channel);
        }

        return list;
    }

    @Key(name = Keys.Messages.SyncChannel.MINECRAFT, relation = Relation.ONE_TO_MANY, type = MinecraftChannel.class, sqlType = Types.Messages.SyncChannel.MINECRAFT)
    @Relational(table = "channel_minecraft", self = "channel", foreign = "minecraft")
    @NotNull List<MinecraftChannel> getMinecraftChannels();

    // TODO: send & receive
}
