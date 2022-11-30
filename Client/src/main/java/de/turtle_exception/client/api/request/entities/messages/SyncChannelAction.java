package de.turtle_exception.client.api.request.entities.messages;

import de.turtle_exception.client.api.entities.messages.DiscordChannel;
import de.turtle_exception.client.api.entities.messages.MinecraftChannel;
import de.turtle_exception.client.api.entities.messages.SyncChannel;
import de.turtle_exception.client.api.request.Action;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collection;

public interface SyncChannelAction extends Action<SyncChannel> {
    /* - DISCORD CHANNELS - */

    SyncChannelAction setDiscordChannelIds(@NotNull Collection<Long> discordChannels);

    default SyncChannelAction setDiscordChannels(@NotNull Collection<DiscordChannel> discordChannels) {
        return this.setDiscordChannelIds(discordChannels.stream().map(DiscordChannel::getId).toList());
    }

    default SyncChannelAction setDiscordChannelIds(@NotNull Long... discordChannels) {
        return this.setDiscordChannelIds(Arrays.asList(discordChannels));
    }

    default SyncChannelAction setDiscordChannels(@NotNull DiscordChannel... discordChannels) {
        return this.setDiscordChannels(Arrays.asList(discordChannels));
    }

    SyncChannelAction addDiscordChannel(long discordChannel);

    default SyncChannelAction addDiscordChannel(@NotNull DiscordChannel discordChannel) {
        return this.addDiscordChannel(discordChannel.getId());
    }

    SyncChannelAction removeDiscordChannel(long discordChannel);

    default SyncChannelAction removeDiscordChannel(@NotNull DiscordChannel discordChannel) {
        return this.removeDiscordChannel(discordChannel.getId());
    }

    /* - MINECRAFT CHANNELS - */

    SyncChannelAction setMinecraftChannelIds(@NotNull Collection<Long> minecraftChannels);

    default SyncChannelAction setMinecraftChannels(@NotNull Collection<MinecraftChannel> minecraftChannels) {
        return this.setMinecraftChannelIds(minecraftChannels.stream().map(MinecraftChannel::getId).toList());
    }

    default SyncChannelAction setMinecraftChannelIds(@NotNull Long... minecraftChannels) {
        return this.setMinecraftChannelIds(Arrays.asList(minecraftChannels));
    }

    default SyncChannelAction setMinecraftChannels(@NotNull MinecraftChannel... minecraftChannels) {
        return this.setMinecraftChannels(Arrays.asList(minecraftChannels));
    }

    SyncChannelAction addMinecraftChannel(long minecraftChannel);

    default SyncChannelAction addMinecraftChannel(@NotNull MinecraftChannel minecraftChannel) {
        return this.addMinecraftChannel(minecraftChannel.getId());
    }

    SyncChannelAction removeMinecraftChannel(long minecraftChannel);

    default SyncChannelAction removeMinecraftChannel(@NotNull MinecraftChannel minecraftChannel) {
        return this.removeMinecraftChannel(minecraftChannel.getId());
    }
}
