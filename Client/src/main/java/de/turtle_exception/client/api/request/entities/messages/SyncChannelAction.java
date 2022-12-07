package de.turtle_exception.client.api.request.entities.messages;

import de.turtle_exception.client.api.TurtleClient;
import de.turtle_exception.client.api.entities.messages.DiscordChannel;
import de.turtle_exception.client.api.entities.messages.MinecraftChannel;
import de.turtle_exception.client.api.entities.messages.SyncChannel;
import de.turtle_exception.client.api.request.Action;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collection;

/**
 * A SyncChannelAction is an Action that requests the creation of a new {@link SyncChannel Channel}, according to the
 * arguments this Action holds. If any required field is missing the server will reject the request and respond with an
 * error. Required fields are all attributes that are not a subclass of {@link Collection}, as these are set to an empty
 * Collection by default.
 * @see TurtleClient#createChannel()
 */
@SuppressWarnings("unused")
public interface SyncChannelAction extends Action<SyncChannel> {
    /* - DISCORD CHANNELS - */

    /**
     * Sets the List of ids that each represent a {@link DiscordChannel} as a child of this Channel.
     * The existing List will be overwritten.
     * @param discordChannels Collection of DiscordChannel ids.
     * @return This SyncChannelAction for chaining convenience.
     */
    SyncChannelAction setDiscordChannelIds(@NotNull Collection<Long> discordChannels);

    /**
     * Sets the List of {@link DiscordChannel DiscordChannels} as children of this Channel.
     * The existing List will be overwritten.
     * @param discordChannels Collection of DiscordChannel ids.
     * @return This SyncChannelAction for chaining convenience.
     */
    default SyncChannelAction setDiscordChannels(@NotNull Collection<DiscordChannel> discordChannels) {
        return this.setDiscordChannelIds(discordChannels.stream().map(DiscordChannel::getId).toList());
    }

    /**
     * Sets the List of ids that each represent a {@link DiscordChannel} as a child of this Channel.
     * The existing List will be overwritten.
     * @param discordChannels Collection of DiscordChannel ids.
     * @return This SyncChannelAction for chaining convenience.
     */
    default SyncChannelAction setDiscordChannelIds(@NotNull Long... discordChannels) {
        return this.setDiscordChannelIds(Arrays.asList(discordChannels));
    }

    /**
     * Sets the List of {@link DiscordChannel DiscordChannels} as children of this Channel.
     * The existing List will be overwritten.
     * @param discordChannels Collection of DiscordChannel ids.
     * @return This SyncChannelAction for chaining convenience.
     */
    default SyncChannelAction setDiscordChannels(@NotNull DiscordChannel... discordChannels) {
        return this.setDiscordChannels(Arrays.asList(discordChannels));
    }

    /**
     * Adds the provided {@code long} to the List of ids that each represent a {@link DiscordChannel} as a child of this Channel.
     * @param discordChannel Channel id.
     * @return This SyncChannelAction for chaining convenience.
     */
    SyncChannelAction addDiscordChannel(long discordChannel);

    /**
     * Adds the provided {@link DiscordChannel} to the List of Channels that are children of this Channel.
     * @param discordChannel Some DiscordChannel.
     * @return This SyncChannelAction for chaining convenience.
     */
    default SyncChannelAction addDiscordChannel(@NotNull DiscordChannel discordChannel) {
        return this.addDiscordChannel(discordChannel.getId());
    }

    /**
     * Removes the provided {@code long} from the List of ids that each represent a {@link DiscordChannel} as a child of
     * this Channel.
     * @param discordChannel Channel id.
     * @return This SyncChannelAction for chaining convenience.
     */
    SyncChannelAction removeDiscordChannel(long discordChannel);

    /**
     * Removes the provided {@link DiscordChannel} from the List of Channels that are children of this Channel.
     * @param discordChannel Some DiscordChannel.
     * @return This SyncChannelAction for chaining convenience.
     */
    default SyncChannelAction removeDiscordChannel(@NotNull DiscordChannel discordChannel) {
        return this.removeDiscordChannel(discordChannel.getId());
    }

    /* - MINECRAFT CHANNELS - */

    /**
     * Sets the List of ids that each represent a {@link MinecraftChannel} as a child of this Channel.
     * The existing List will be overwritten.
     * @param minecraftChannels Collection of MinecraftChannel ids.
     * @return This SyncChannelAction for chaining convenience.
     */
    SyncChannelAction setMinecraftChannelIds(@NotNull Collection<Long> minecraftChannels);

    /**
     * Sets the List of {@link MinecraftChannel MinecraftChannels} as children of this Channel.
     * The existing List will be overwritten.
     * @param minecraftChannels Collection of MinecraftChannel ids.
     * @return This SyncChannelAction for chaining convenience.
     */
    default SyncChannelAction setMinecraftChannels(@NotNull Collection<MinecraftChannel> minecraftChannels) {
        return this.setMinecraftChannelIds(minecraftChannels.stream().map(MinecraftChannel::getId).toList());
    }

    /**
     * Sets the List of ids that each represent a {@link MinecraftChannel} as a child of this Channel.
     * The existing List will be overwritten.
     * @param minecraftChannels Collection of MinecraftChannel ids.
     * @return This SyncChannelAction for chaining convenience.
     */
    default SyncChannelAction setMinecraftChannelIds(@NotNull Long... minecraftChannels) {
        return this.setMinecraftChannelIds(Arrays.asList(minecraftChannels));
    }

    /**
     * Sets the List of {@link MinecraftChannel MinecraftChannels} as children of this Channel.
     * The existing List will be overwritten.
     * @param minecraftChannels Collection of MinecraftChannel ids.
     * @return This SyncChannelAction for chaining convenience.
     */
    default SyncChannelAction setMinecraftChannels(@NotNull MinecraftChannel... minecraftChannels) {
        return this.setMinecraftChannels(Arrays.asList(minecraftChannels));
    }

    /**
     * Adds the provided {@code long} to the List of ids that each represent a {@link MinecraftChannel} as a child of this Channel.
     * @param minecraftChannel Channel id.
     * @return This SyncChannelAction for chaining convenience.
     */
    SyncChannelAction addMinecraftChannel(long minecraftChannel);

    /**
     * Adds the provided {@link MinecraftChannel} to the List of Channels that are children of this Channel.
     * @param minecraftChannel Some MinecraftChannel.
     * @return This SyncChannelAction for chaining convenience.
     */
    default SyncChannelAction addMinecraftChannel(@NotNull MinecraftChannel minecraftChannel) {
        return this.addMinecraftChannel(minecraftChannel.getId());
    }

    /**
     * Removes the provided {@code long} from the List of ids that each represent a {@link MinecraftChannel} as a child of
     * this Channel.
     * @param minecraftChannel Channel id.
     * @return This SyncChannelAction for chaining convenience.
     */
    SyncChannelAction removeMinecraftChannel(long minecraftChannel);

    /**
     * Removes the provided {@link MinecraftChannel} from the List of Channels that are children of this Channel.
     * @param minecraftChannel Some MinecraftChannel.
     * @return This SyncChannelAction for chaining convenience.
     */
    default SyncChannelAction removeMinecraftChannel(@NotNull MinecraftChannel minecraftChannel) {
        return this.removeMinecraftChannel(minecraftChannel.getId());
    }
}
