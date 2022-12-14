package de.turtle_exception.client.internal.entities.messages;

import com.google.gson.JsonObject;
import de.turtle_exception.client.api.TurtleClient;
import de.turtle_exception.client.api.entities.messages.DiscordChannel;
import de.turtle_exception.client.api.entities.messages.IChannel;
import de.turtle_exception.client.api.entities.messages.MinecraftChannel;
import de.turtle_exception.client.api.entities.messages.SyncChannel;
import de.turtle_exception.client.api.request.Action;
import de.turtle_exception.client.internal.data.annotations.Keys;
import de.turtle_exception.client.internal.entities.TurtleImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SyncChannelImpl extends TurtleImpl implements SyncChannel {
    public SyncChannelImpl(@NotNull TurtleClient client, long id) {
        super(client, id);
    }

    @Override
    public @NotNull TurtleImpl handleUpdate(@NotNull JsonObject json) {
        return this;
    }

    /* - - - */

    @Override
    public @Nullable IChannel getTurtleById(long id) {
        DiscordChannel discordChannel = client.getTurtleById(id, DiscordChannel.class);
        if (discordChannel != null && discordChannel.getSyncChannelId() == this.getId())
            return discordChannel;

        MinecraftChannel minecraftChannel = client.getTurtleById(id, MinecraftChannel.class);
        if (minecraftChannel != null && minecraftChannel.getSyncChannelId() == this.getId())
            return minecraftChannel;

        return null;
    }

    /* - DISCORD - */

    @Override
    public @NotNull List<DiscordChannel> getDiscordChannels() {
        return getClient().getTurtles(DiscordChannel.class).stream()
                .filter(discordChannel -> discordChannel.getSyncChannelId() == this.getId())
                .toList();
    }

    @Override
    public @NotNull Action<SyncChannel> addDiscordChannel(long discordChannel) {
        return getClient().getProvider().patchEntryAdd(this, Keys.Messages.SyncChannel.DISCORD, discordChannel).andThenParse(SyncChannel.class);
    }

    @Override
    public @NotNull Action<SyncChannel> removeDiscordChannel(long discordChannel) {
        return getClient().getProvider().patchEntryDel(this, Keys.Messages.SyncChannel.DISCORD, discordChannel).andThenParse(SyncChannel.class);
    }

    /* - MINECRAFT - */

    @Override
    public @NotNull List<MinecraftChannel> getMinecraftChannels() {
        return getClient().getTurtles(MinecraftChannel.class).stream()
                .filter(minecraftChannel -> minecraftChannel.getSyncChannelId() == this.getId())
                .toList();
    }

    @Override
    public @NotNull Action<SyncChannel> addMinecraftChannel(long minecraftChannel) {
        return getClient().getProvider().patchEntryAdd(this, Keys.Messages.SyncChannel.MINECRAFT, minecraftChannel).andThenParse(SyncChannel.class);
    }

    @Override
    public @NotNull Action<SyncChannel> removeMinecraftChannel(long minecraftChannel) {
        return getClient().getProvider().patchEntryDel(this, Keys.Messages.SyncChannel.MINECRAFT, minecraftChannel).andThenParse(SyncChannel.class);
    }
}
