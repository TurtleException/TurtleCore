package de.turtle_exception.client.api.entities.messages;

import de.turtle_exception.client.api.request.Action;
import de.turtle_exception.client.internal.data.annotations.*;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

/**
 * A {@link IChannel Channel} that connects to a Minecraft chat channel ({@link Server}, {@link World} or {@link Player}).
 */
@Resource(path = "minecraft_channels", builder = "buildMinecraftChannel")
@SuppressWarnings("unused")
public interface MinecraftChannel extends IChannel {
    @Override
    default @NotNull Action<MinecraftChannel> update() {
        return this.getClient().retrieveTurtle(this.getId(), MinecraftChannel.class);
    }

    /* - SYNC CHANNEL - */

    @Override
    @NotNull Action<MinecraftChannel> modifySyncChannel(long syncChannel);

    @Override
    @NotNull
    default Action<MinecraftChannel> modifySyncChannel(@NotNull SyncChannel channel) {
        return this.modifySyncChannel(channel.getId());
    }

    /* - TYPE - */

    /**
     * A MinecraftChannel can either be a direct chat with a specific {@link Player} (independent of which server
     * they're on), a {@link World} (All Players that are currently in that World) or a {@link Server} (All Players that
     * are currently on that Server).
     */
    enum Type {
        /** Identifies a {@link Player} by its {@link UUID}. */
        USER((byte) 0),
        /** Identifies a {@link World} by its {@link UUID}. */
        WORLD((byte) 1),
        // TODO
        /** Identifies a {@link Server} by ??? */
        SERVER((byte) 2),
        UNDEFINED(Byte.MAX_VALUE);

        public final byte code;
        Type(byte code) {
            this.code = code;
        }

        /**
         * Attempts to parse the provided {@code byte} into its corresponding {@link Type}. If no Type with that code
         * exists, {@link Type#UNDEFINED} is returned.
         * @param code Code of the Type.
         * @return The parsed Type, or {@link Type#UNDEFINED} as default.
         */
        public static @NotNull Type of(byte code) {
            for (Type value : Type.values())
                if (value.code == code)
                    return value;
            return Type.UNDEFINED;
        }
    }

    /**
     * Provides the type of this MinecraftChannel in its {@code byte} representation. This method exists mainly for
     * serialization purposes, as it is a rather inefficient shortcut for {@code MinecraftChannel.getType().code}.
     * @return The MinecraftChannel type as a {@code byte}.
     */
    @Key(name = Keys.Messages.MinecraftChannel.TYPE, sqlType = Types.Messages.MinecraftChannel.TYPE)
    default byte getTypeCode() {
        return this.getType().code;
    }

    /**
     * Provides the type of this MinecraftChannel. See {@link Type} documentation for more information.
     * @return The MinecraftChannel type.
     */
    @NotNull Type getType();

    /**
     * Creates an Action with the instruction to modify this MinecraftChannel's type and change it to the provided Type.
     * @param type New MinecraftChannel Type.
     * @return Action that provides the modified {@link MinecraftChannel} on completion.
     */
    @NotNull Action<MinecraftChannel> modifyType(@NotNull Type type);

    /* - IDENTIFIER - */

    /**
     * Provides the identifier of the connected Minecraft Chat channel.
     * <p> Be aware that the channel may be modified or deleted from outside the application. This identifier may
     * represent a deleted channel!
     * @return The Minecraft chat channel identifier.
     */
    @Key(name = Keys.Messages.MinecraftChannel.IDENTIFIER, sqlType = Types.Messages.MinecraftChannel.IDENTIFIER)
    @NotNull String getIdentifier();

    /**
     * Creates an Action with the instruction to modify this MinecraftChannel's identifier and change it to the provided
     * String.
     * @param identifier New MinecraftChannel identifier.
     * @return Action that provides the modified {@link MinecraftChannel} on completion.
     */
    @NotNull Action<MinecraftChannel> modifyIdentifier(@NotNull String identifier);

    /**
     * Provides the result of {@link MinecraftChannel#getIdentifier()} as a {@link UUID}.
     * @return UUID representation of the Minecraft chat channel identifier.
     * @throws IllegalStateException if the MinecraftChannel is of a type that does not support UUIDs.
     */
    default @NotNull UUID getUUID() throws IllegalStateException {
        if (this.getType() == Type.SERVER)
            throw new IllegalStateException("Illegal channel type: " + this.getType());
        return UUID.fromString(this.getIdentifier());
    }

    /* - LOGIC - */

    // TODO
    void receive(@NotNull Player player, @NotNull String msg);
}
