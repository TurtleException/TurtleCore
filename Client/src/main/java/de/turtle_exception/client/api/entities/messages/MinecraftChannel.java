package de.turtle_exception.client.api.entities.messages;

import de.turtle_exception.client.api.request.Action;
import de.turtle_exception.client.internal.data.annotations.*;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

@Resource(path = "minecraft_channels", builder = "buildMinecraftChannel")
@SuppressWarnings("unused")
public interface MinecraftChannel extends IChannel {
    @Override
    default @NotNull Action<MinecraftChannel> update() {
        return this.getClient().retrieveMinecraftChannel(this.getId());
    }

    @Override
    @NotNull Action<MinecraftChannel> modifySyncChannel(long syncChannel);

    @Override
    @NotNull
    default Action<MinecraftChannel> modifySyncChannel(@NotNull SyncChannel channel) {
        return this.modifySyncChannel(channel.getId());
    }

    enum Type {
        USER((byte) 0),
        WORLD((byte) 1),
        SERVER((byte) 2);

        public final byte code;
        Type(byte code) {
            this.code = code;
        }
    }

    @Key(name = Keys.Messages.MinecraftChannel.TYPE, sqlType = Types.Messages.MinecraftChannel.TYPE)
    default byte getTypeCode() {
        return this.getType().code;
    }

    @NotNull Type getType();

    @NotNull Action<MinecraftChannel> modifyType(@NotNull Type type);

    @Key(name = Keys.Messages.MinecraftChannel.IDENTIFIER, sqlType = Types.Messages.MinecraftChannel.IDENTIFIER)
    @NotNull String getIdentifier();

    @NotNull Action<MinecraftChannel> modifyIdentifier(@NotNull String identifier);

    default @NotNull UUID getUUID() throws IllegalStateException {
        if (this.getType() == Type.SERVER)
            throw new IllegalStateException("Illegal channel type: " + this.getType());
        return UUID.fromString(this.getIdentifier());
    }

    // TODO
    void receive(@NotNull Player player, @NotNull String msg);
}
