package de.turtle_exception.client.internal.entities.messages;

import com.google.gson.JsonObject;
import de.turtle_exception.client.api.TurtleClient;
import de.turtle_exception.client.api.entities.messages.MinecraftChannel;
import de.turtle_exception.client.api.entities.messages.SyncChannel;
import de.turtle_exception.client.api.entities.messages.SyncMessage;
import de.turtle_exception.client.internal.entities.TurtleImpl;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class MinecraftChannelImpl extends ChannelImpl implements MinecraftChannel {
    private Type type;
    private String identifier;

    protected MinecraftChannelImpl(@NotNull TurtleClient client, long id, SyncChannel syncChannel, Type type, String identifier) {
        super(client, id, syncChannel);

        this.type        = type;
        this.identifier  = identifier;
    }

    @Override
    public @NotNull TurtleImpl handleUpdate(@NotNull JsonObject json) {
        // TODO
        return null;
    }

    /* - - - */

    @Override
    public void receive(@NotNull Player player, @NotNull String msg) {
        // TODO
    }

    @Override
    public void send(@NotNull SyncMessage msg) {
        // TODO
    }

    @Override
    public @NotNull Type getType() {
        return this.type;
    }

    @Override
    public @NotNull String getIdentifier() {
        return this.identifier;
    }
}
