package de.turtle_exception.client.internal.entities.messages;

import com.google.gson.JsonObject;
import de.turtle_exception.client.api.TurtleClient;
import de.turtle_exception.client.api.entities.Turtle;
import de.turtle_exception.client.api.entities.messages.DiscordChannel;
import de.turtle_exception.client.api.entities.messages.MinecraftChannel;
import de.turtle_exception.client.api.entities.messages.SyncChannel;
import de.turtle_exception.client.internal.entities.TurtleImpl;
import de.turtle_exception.client.internal.util.TurtleSet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SyncChannelImpl extends TurtleImpl implements SyncChannel {
    private final TurtleSet<DiscordChannel>   discordChannels;
    private final TurtleSet<MinecraftChannel> minecraftChannels;

    protected SyncChannelImpl(@NotNull TurtleClient client, long id, TurtleSet<DiscordChannel> discordChannels, TurtleSet<MinecraftChannel> minecraftChannels) {
        super(client, id);

        this.discordChannels   = discordChannels;
        this.minecraftChannels = minecraftChannels;
    }

    @Override
    public @NotNull TurtleImpl handleUpdate(@NotNull JsonObject json) {
        // TODO
        return null;
    }

    /* - - - */

    @Override
    public @Nullable DiscordChannel getDiscordChannelById(long id) {
        return this.discordChannels.get(id);
    }

    @Override
    public @Nullable MinecraftChannel getMinecraftChannelById(long id) {
        return this.minecraftChannels.get(id);
    }

    @SuppressWarnings("RedundantIfStatement")
    @Override
    public @Nullable Turtle getTurtleById(long id) {
        DiscordChannel discordChannel = discordChannels.get(id);
        if (discordChannel != null) return discordChannel;
        MinecraftChannel minecraftChannel = minecraftChannels.get(id);
        if (minecraftChannel != null) return minecraftChannel;
        return null;
    }

    @Override
    public @NotNull List<DiscordChannel> getDiscordChannels() {
        return List.copyOf(this.discordChannels);
    }

    @Override
    public @NotNull List<MinecraftChannel> getMinecraftChannels() {
        return List.copyOf(this.minecraftChannels);
    }
}
