package de.turtle_exception.client.internal.net.packets;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import de.turtle_exception.client.internal.data.Data;
import de.turtle_exception.client.internal.net.Connection;
import de.turtle_exception.client.internal.net.Direction;
import de.turtle_exception.client.internal.util.time.TurtleType;
import de.turtle_exception.client.internal.util.time.TurtleUtil;
import org.jetbrains.annotations.NotNull;

public class DataPacket extends Packet {
    public static final byte TYPE = 1;

    protected final @NotNull Data data;

    public DataPacket(long id, long deadline, @NotNull Connection connection, long responseCode, @NotNull Direction direction, @NotNull Data data) {
        super(id, deadline, connection, responseCode, direction, TYPE);
        this.data = data;
    }

    public DataPacket(long deadline, @NotNull Connection connection, long responseCode, @NotNull Data data) {
        this(TurtleUtil.newId(TurtleType.PACKET), deadline, connection, responseCode, Direction.OUTBOUND, data);
    }

    public DataPacket(long id, long deadline, @NotNull Connection connection, long responseCode, @NotNull Direction direction, byte[] bytes) {
        super(id, deadline, connection, responseCode, direction, TYPE);
        this.data = Data.of(new Gson().fromJson(new String(bytes), JsonObject.class));
    }

    public @NotNull Data getData() {
        return data;
    }

    @Override
    public byte[] getBytes() {
        return new Gson().toJson(data.asJson()).getBytes();
    }
}
