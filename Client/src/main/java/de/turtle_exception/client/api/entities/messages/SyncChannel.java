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

    /* - DISCORD - */

    @NotNull Action<SyncChannel> addDiscordChannel(long discordChannel);

    default @NotNull Action<SyncChannel> addDiscordChanel(@NotNull DiscordChannel discordChannel) {
        return this.addDiscordChannel(discordChannel.getId());
    }

    @NotNull Action<SyncChannel> removeDiscordChannel(long discordChannel);

    default @NotNull Action<SyncChannel> removeDiscordChannel(@NotNull DiscordChannel discordChannel) {
        return this.removeDiscordChannel(discordChannel.getId());
    }

    /* - MINECRAFT - */

    @NotNull Action<SyncChannel> addMinecraftChannel(long minecraftChannel);

    default @NotNull Action<SyncChannel> addMinecraftChanel(@NotNull MinecraftChannel minecraftChannel) {
        return this.addMinecraftChannel(minecraftChannel.getId());
    }

    @NotNull Action<SyncChannel> removeMinecraftChannel(long minecraftChannel);

    default @NotNull Action<SyncChannel> removeMinecraftChannel(@NotNull MinecraftChannel minecraftChannel) {
        return this.removeMinecraftChannel(minecraftChannel.getId());
    }

    /* - - - */

    default @NotNull List<IChannel> getChannels() {
        return Stream.concat(
                this.getDiscordChannels().stream(),
                this.getMinecraftChannels().stream()
        ).toList();
    }

    // TODO: send & receive
}
