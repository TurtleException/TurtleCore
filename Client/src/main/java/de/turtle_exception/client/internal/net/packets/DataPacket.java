package de.turtle_exception.client.internal.net.packets;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import de.turtle_exception.client.internal.net.Direction;
import de.turtle_exception.client.internal.net.message.Conversation;
import org.jetbrains.annotations.NotNull;

public class DataPacket extends Packet {
    public static final byte TYPE = 1;

    protected final @NotNull JsonObject json;

    public DataPacket(long id, @NotNull Conversation conversation, @NotNull Direction direction, @NotNull JsonObject data) {
        super(id, conversation, direction, TYPE);
        this.json = data;
    }

    public DataPacket(long id, @NotNull Conversation conversation, @NotNull Direction direction, byte[] bytes) {
        super(id, conversation, direction, TYPE);
        this.json = new Gson().fromJson(new String(bytes), JsonObject.class);
    }

    @Override
    public byte[] getBytes() {
        return new Gson().toJson(this.json).getBytes();
    }
}
