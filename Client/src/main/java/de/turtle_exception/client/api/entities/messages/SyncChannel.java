package de.turtle_exception.client.api.entities.messages;

import de.turtle_exception.client.api.entities.Turtle;
import de.turtle_exception.client.api.entities.containers.messages.IDiscordChannelContainer;
import de.turtle_exception.client.api.entities.containers.messages.IMinecraftChannelContainer;
import de.turtle_exception.client.api.request.Action;
import de.turtle_exception.client.internal.data.annotations.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Resource(path = "channels", builder = "buildSyncChannel")
@SuppressWarnings("unused")
public interface SyncChannel extends Turtle, IDiscordChannelContainer, IMinecraftChannelContainer {
    @Override
    default @NotNull Action<SyncChannel> update() {
        return this.getClient().retrieveChannel(this.getId());
    }

    @Override
    default @NotNull List<Turtle> getTurtles() {
        ArrayList<Turtle> list = new ArrayList<>();
        list.addAll(this.getDiscordChannels());
        list.addAll(this.getMinecraftChannels());
        return List.copyOf(list);
    }

    @Override
    @Nullable Turtle getTurtleById(long id);

    @Override
    @Key(name = Keys.Messages.SyncChannel.DISCORD, relation = Relation.ONE_TO_MANY, sqlType = Types.Messages.SyncChannel.DISCORD)
    @Relational(table = "channel_discord", self = "channel", foreign = "discord", type = DiscordChannel.class)
    @NotNull List<DiscordChannel> getDiscordChannels();

    @Override
    @Key(name = Keys.Messages.SyncChannel.MINECRAFT, relation = Relation.ONE_TO_MANY, sqlType = Types.Messages.SyncChannel.MINECRAFT)
    @Relational(table = "channel_minecraft", self = "channel", foreign = "minecraft", type = MinecraftChannel.class)
    @NotNull List<MinecraftChannel> getMinecraftChannels();

    default @NotNull List<IChannel> getChannels() {
        return Stream.concat(
                this.getDiscordChannels().stream(),
                this.getMinecraftChannels().stream()
        ).toList();
    }

    // TODO: send & receive
}
