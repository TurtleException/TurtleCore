package de.turtle_exception.client.api.entities.messages;

import de.turtle_exception.client.api.entities.Turtle;
import de.turtle_exception.client.api.entities.containers.TurtleContainer;
import de.turtle_exception.client.api.request.Action;
import de.turtle_exception.client.internal.data.annotations.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * A message channel that is synchronized across multiple third-party channels. SyncChannels contain
 * {@link SyncMessage SyncMessages}, which are created by the responsible listener applications and contain any
 * information that may be necessary to process the message in another application.
 * @see DiscordChannel
 * @see MinecraftChannel
 */
@Resource(path = "channels", builder = "buildSyncChannel")
@SuppressWarnings("unused")
public interface SyncChannel extends Turtle, TurtleContainer<IChannel> {
    @Override
    default @NotNull Action<SyncChannel> update() {
        return this.getClient().retrieveTurtle(this.getId(), SyncChannel.class);
    }

    /* - CHANNELS - */

    @Override
    default @NotNull List<IChannel> getTurtles() {
        ArrayList<IChannel> list = new ArrayList<>();
        list.addAll(this.getDiscordChannels());
        list.addAll(this.getMinecraftChannels());
        return List.copyOf(list);
    }

    @Override
    @Nullable IChannel getTurtleById(long id);

    /* - DISCORD - */

    /**
     * Provides a List of all {@link DiscordChannel DiscordChannels} that are coupled to this SyncChannel.
     * @return List of coupled DiscordChannels.
     */
    @NotNull List<DiscordChannel> getDiscordChannels();

    /**
     * Creates an Action with the instruction to add the provided id to the list of coupled DiscordChannels.
     * <p> The provided {@code long} should be a representation of a {@link DiscordChannel} id.
     * @param discordChannel Turtle ID of a DiscordChannel.
     * @return Action that provides the modified {@link SyncChannel} on completion.
     */
    @NotNull Action<SyncChannel> addDiscordChannel(long discordChannel);

    /**
     * Creates an Action with the instruction to add the provided {@link DiscordChannel} to the list of coupled
     * DiscordChannels.
     * <p> This is a shortcut for {@code SyncChannel.addDiscordChanel(discordChannel.getId())}.
     * @param discordChannel A DiscordChannel.
     * @return Action that provides the modified {@link SyncChannel} on completion.
     */
    default @NotNull Action<SyncChannel> addDiscordChanel(@NotNull DiscordChannel discordChannel) {
        return this.addDiscordChannel(discordChannel.getId());
    }

    /**
     * Creates an Action with the instruction to remove the provided id from the list of Group members.
     * <p> The provided {@code long} should be a representation of a {@link DiscordChannel} id.
     * @param discordChannel Turtle ID of a DiscordChannel.
     * @return Action that provides the modified {@link SyncChannel} on completion.
     */
    @NotNull Action<SyncChannel> removeDiscordChannel(long discordChannel);

    /**
     * Creates an Action with the instruction to remove the provided {@link DiscordChannel} from the list of coupled
     * DiscordChannels.
     * <p> This is a shortcut for {@code SyncChannel.removeDiscordChanel(discordChannel.getId())}.
     * @param discordChannel A DiscordChannel.
     * @return Action that provides the modified {@link SyncChannel} on completion.
     */
    default @NotNull Action<SyncChannel> removeDiscordChannel(@NotNull DiscordChannel discordChannel) {
        return this.removeDiscordChannel(discordChannel.getId());
    }

    /* - MINECRAFT - */

    /**
     * Provides a List of all {@link MinecraftChannel MinecraftChannels} that are coupled to this SyncChannel.
     * @return List of coupled MinecraftChannels.
     */
    @NotNull List<MinecraftChannel> getMinecraftChannels();

    /**
     * Creates an Action with the instruction to add the provided id to the list of coupled MinecraftChannels.
     * <p> The provided {@code long} should be a representation of a {@link MinecraftChannel} id.
     * @param minecraftChannel Turtle ID of a MinecraftChannel.
     * @return Action that provides the modified {@link SyncChannel} on completion.
     */
    @NotNull Action<SyncChannel> addMinecraftChannel(long minecraftChannel);

    /**
     * Creates an Action with the instruction to add the provided {@link MinecraftChannel} to the list of coupled
     * MinecraftChannels.
     * <p> This is a shortcut for {@code SyncChannel.addMinecraftChanel(minecraftChannel.getId())}.
     * @param minecraftChannel A MinecraftChannel.
     * @return Action that provides the modified {@link SyncChannel} on completion.
     */
    default @NotNull Action<SyncChannel> addMinecraftChanel(@NotNull MinecraftChannel minecraftChannel) {
        return this.addMinecraftChannel(minecraftChannel.getId());
    }

    /**
     * Creates an Action with the instruction to remove the provided id from the list of Group members.
     * <p> The provided {@code long} should be a representation of a {@link MinecraftChannel} id.
     * @param minecraftChannel Turtle ID of a MinecraftChannel.
     * @return Action that provides the modified {@link SyncChannel} on completion.
     */
    @NotNull Action<SyncChannel> removeMinecraftChannel(long minecraftChannel);

    /**
     * Creates an Action with the instruction to remove the provided {@link MinecraftChannel} from the list of coupled
     * MinecraftChannels.
     * <p> This is a shortcut for {@code SyncChannel.removeMinecraftChanel(minecraftChannel.getId())}.
     * @param minecraftChannel A MinecraftChannel.
     * @return Action that provides the modified {@link SyncChannel} on completion.
     */
    default @NotNull Action<SyncChannel> removeMinecraftChannel(@NotNull MinecraftChannel minecraftChannel) {
        return this.removeMinecraftChannel(minecraftChannel.getId());
    }

    /* - LOGIC - */

    // TODO: send & receive
}
