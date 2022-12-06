package de.turtle_exception.client.api.entities.messages;

import de.turtle_exception.client.api.request.Action;
import de.turtle_exception.client.internal.data.annotations.Key;
import de.turtle_exception.client.internal.data.annotations.Keys;
import de.turtle_exception.client.internal.data.annotations.Resource;
import de.turtle_exception.client.internal.data.annotations.Types;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import org.jetbrains.annotations.NotNull;

/** A {@link IChannel Channel} that connects to a Discord {@link MessageChannel}. */
@Resource(path = "discord_channels", builder = "buildDiscordChannel")
@SuppressWarnings("unused")
public interface DiscordChannel extends IChannel {
    @Override
    default @NotNull Action<DiscordChannel> update() {
        return this.getClient().retrieveTurtle(this.getId(), DiscordChannel.class);
    }

    /* - SYNC CHANNEL - */

    @Override
    @NotNull Action<DiscordChannel> modifySyncChannel(long syncChannel);

    @Override
    @NotNull
    default Action<DiscordChannel> modifySyncChannel(@NotNull SyncChannel channel) {
        return this.modifySyncChannel(channel.getId());
    }

    /* - SNOWFLAKE - */

    /**
     * Provides the snowflake id of the connected {@link MessageChannel}.
     * <p> Be aware that the channel may be modified or deleted from outside the application. This snowflake may
     * represent a deleted channel!
     * @return The Discord channel snowflake id.
     */
    @Key(name = Keys.Messages.DiscordChannel.SNOWFLAKE, sqlType = Types.Messages.DiscordChannel.SNOWFLAKE)
    long getSnowflake();

    /**
     * Creates an Action with the instruction to modify this DiscordChannel's snowflake id and change it to the provided
     * {@code long}.
     * @param snowflake New DiscordChannel snowflake.
     * @return Action that provides the modified {@link DiscordChannel} on completion.
     */
    @NotNull Action<DiscordChannel> modifySnowflake(long snowflake);

    /* - LOGIC - */

    void receive(@NotNull Message msg);
}
