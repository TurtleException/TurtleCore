package de.turtle_exception.client.internal.request.actions.entities.messages;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import de.turtle_exception.client.api.entities.messages.SyncChannel;
import de.turtle_exception.client.api.request.entities.messages.SyncChannelAction;
import de.turtle_exception.client.internal.Provider;
import de.turtle_exception.client.internal.request.actions.EntityAction;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;

public class SyncChannelActionImpl extends EntityAction<SyncChannel> implements SyncChannelAction {
    protected ArrayList<Long> discordChannels = new ArrayList<>();
    protected ArrayList<Long> minecraftChannels = new ArrayList<>();

    public SyncChannelActionImpl(@NotNull Provider provider) {
        super(provider, SyncChannel.class);

        this.checks.add(json -> {
            JsonArray arr = json.get("discord_channels").getAsJsonArray();
            for (JsonElement entry : arr)
                entry.getAsLong();
        });
        this.checks.add(json -> {
            JsonArray arr = json.get("minecraft_channels").getAsJsonArray();
            for (JsonElement entry : arr)
                entry.getAsLong();
        });
    }

    @Override
    protected void updateContent() {
        this.content = new JsonObject();

        JsonArray discordArr = new JsonArray();
        for (Long discord : this.discordChannels)
            discordArr.add(discord);
        this.content.add("discord_channels", discordArr);
        JsonArray minecraftArr = new JsonArray();
        for (Long minecraft : this.minecraftChannels)
            minecraftArr.add(minecraft);
        this.content.add("minecraft_channels", minecraftArr);
    }

    /* - - - */

    @Override
    public SyncChannelAction setDiscordChannelIds(@NotNull Collection<Long> discordChannels) {
        this.discordChannels = new ArrayList<>(discordChannels);
        return this;
    }

    @Override
    public SyncChannelAction addDiscordChannel(long discordChannel) {
        this.discordChannels.add(discordChannel);
        return this;
    }

    @Override
    public SyncChannelAction removeDiscordChannel(long discordChannel) {
        this.discordChannels.remove(discordChannel);
        return this;
    }

    @Override
    public SyncChannelAction setMinecraftChannelIds(@NotNull Collection<Long> minecraftChannels) {
        this.minecraftChannels = new ArrayList<>(minecraftChannels);
        return this;
    }

    @Override
    public SyncChannelAction addMinecraftChannel(long minecraftChannel) {
        this.minecraftChannels.add(minecraftChannel);
        return this;
    }

    @Override
    public SyncChannelAction removeMinecraftChannel(long minecraftChannel) {
        this.minecraftChannels.remove(minecraftChannel);
        return this;
    }
}
