package de.turtle_exception.client.api.entities.messages;

import de.turtle_exception.client.api.entities.Turtle;
import de.turtle_exception.client.internal.data.annotations.*;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

@Resource(path = "minecraft_channel", builder = "buildMinecraftChannel")
public interface MinecraftChannel extends Turtle {
    @Key(name = Keys.Messages.MinecraftChannel.SYNC_CHANNEL, relation = Relation.MANY_TO_ONE, sqlType = Types.Messages.MinecraftChannel.SYNC_CHANNEL)
    @NotNull SyncChannel getSyncChannel();

    enum Type {
        USER((byte) 0),
        WORLD((byte) 1),
        SERVER((byte) 2);

        final byte code;
        Type(byte code) {
            this.code = code;
        }
    }

    @Key(name = Keys.Messages.MinecraftChannel.TYPE, sqlType = Types.Messages.MinecraftChannel.TYPE)
    default byte getTypeCode() {
        return this.getType().code;
    }

    @NotNull Type getType();

    @Key(name = Keys.Messages.MinecraftChannel.IDENTIFIER, sqlType = Types.Messages.MinecraftChannel.IDENTIFIER)
    @NotNull String getIdentifier();

    default @NotNull UUID getUUID() throws IllegalStateException {
        if (this.getType() == Type.SERVER)
            throw new IllegalStateException("Illegal channel type: " + this.getType());
        return UUID.fromString(this.getIdentifier());
    }

    // TODO
    void send(@NotNull SyncMessage msg);

    // TODO
    void receive(@NotNull Player player, @NotNull String msg);
}
