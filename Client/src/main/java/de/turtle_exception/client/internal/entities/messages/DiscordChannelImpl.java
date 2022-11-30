package de.turtle_exception.client.internal.entities.messages;

import com.google.gson.JsonObject;
import de.turtle_exception.client.api.TurtleClient;
import de.turtle_exception.client.api.entities.messages.DiscordChannel;
import de.turtle_exception.client.api.entities.messages.SyncChannel;
import de.turtle_exception.client.api.entities.messages.SyncMessage;
import de.turtle_exception.client.internal.entities.TurtleImpl;
import net.dv8tion.jda.api.entities.Message;
import org.jetbrains.annotations.NotNull;

public class DiscordChannelImpl extends ChannelImpl implements DiscordChannel {
    private long snowflake;

    protected DiscordChannelImpl(@NotNull TurtleClient client, long id, SyncChannel syncChannel, long snowflake) {
        super(client, id, syncChannel);

        this.snowflake   = snowflake;
    }

    @Override
    public @NotNull TurtleImpl handleUpdate(@NotNull JsonObject json) {
        // TODO
        return null;
    }

    /* - - - */

    @Override
    public void receive(@NotNull Message msg) {
        // TODO
    }

    @Override
    public void send(@NotNull SyncMessage msg) {
        // TODO
    }

    @Override
    public long getSnowflake() {
        return this.snowflake;
    }
}
