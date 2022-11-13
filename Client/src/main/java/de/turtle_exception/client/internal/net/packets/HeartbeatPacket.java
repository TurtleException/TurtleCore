package de.turtle_exception.client.internal.net.packets;

import de.turtle_exception.client.internal.net.Direction;
import de.turtle_exception.client.internal.net.message.Conversation;
import de.turtle_exception.client.internal.util.MathUtil;
import org.jetbrains.annotations.NotNull;

public class HeartbeatPacket extends Packet {
    public static final byte TYPE = 2;

    public enum Stage { SEND, ACKNOWLEDGE, RECEIVE }
    protected final @NotNull Stage stage;

    protected long time1;
    protected long time2;
    protected long time3;

    /** Initiates a new heartbeat */
    public HeartbeatPacket(long id, @NotNull Conversation conversation, @NotNull Direction direction) {
        this(id, conversation, direction, new byte[Long.BYTES * 3]);
    }

    public HeartbeatPacket(long id, @NotNull Conversation conversation, @NotNull Direction direction, byte[] bytes) {
        super(id, conversation, direction, TYPE);

        if (bytes.length < Long.BYTES * 3)
            throw new IllegalArgumentException("HeartbeatPacket missing timings: " + bytes.length + " of " + (Long.BYTES * 3) + " bytes present.");

        final long time = System.currentTimeMillis();

        this.time1 = MathUtil.bytesToLong(bytes, 0);
        this.time2 = MathUtil.bytesToLong(bytes, 4);
        this.time3 = MathUtil.bytesToLong(bytes, 8);

        if (time3 == 0)
            stage = Stage.RECEIVE;
        else if (time2 == 0)
            stage = Stage.ACKNOWLEDGE;
        else if (time1 == 0)
            stage = Stage.SEND;
        else
            throw new IllegalStateException("Heartbeat may not go past state RECEIVE");

        // update timings
        if (stage == Stage.SEND)
            this.time1 = time;
        if (stage == Stage.ACKNOWLEDGE)
            this.time2 = time;
        if (stage == Stage.RECEIVE)
            this.time3 = time;
    }

    @Override
    public byte[] getBytes() {
        byte[] bytes = new byte[Long.BYTES * 3];
        System.arraycopy(MathUtil.longToBytes(time1), 0, bytes, 0, Long.BYTES);
        System.arraycopy(MathUtil.longToBytes(time2), 4, bytes, 0, Long.BYTES);
        System.arraycopy(MathUtil.longToBytes(time3), 8, bytes, 0, Long.BYTES);
        return bytes;
    }

    public @NotNull Stage getStage() {
        return stage;
    }

    public long getTime(@NotNull Stage stage) {
        return switch (stage) {
            case SEND        -> time1;
            case ACKNOWLEDGE -> time2;
            case RECEIVE     -> time3;
        };
    }

    public @NotNull HeartbeatPacket getResponse() throws IllegalStateException {
        if (stage == Stage.RECEIVE)
            throw new IllegalStateException("Heartbeat may not go past state RECEIVE");

        // TODO: rework constructor

        return new HeartbeatPacket(/* TODO */ 0, this.conversation, Direction.OUTBOUND);
    }
}
