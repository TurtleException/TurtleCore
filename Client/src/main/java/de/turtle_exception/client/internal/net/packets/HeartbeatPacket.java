package de.turtle_exception.client.internal.net.packets;

import de.turtle_exception.client.internal.net.Connection;
import de.turtle_exception.client.internal.net.Direction;
import de.turtle_exception.client.internal.util.MathUtil;
import de.turtle_exception.client.internal.util.time.TurtleType;
import de.turtle_exception.client.internal.util.time.TurtleUtil;
import org.jetbrains.annotations.NotNull;

public class HeartbeatPacket extends Packet {
    public static final byte TYPE = 2;

    public enum Stage { SEND, ACKNOWLEDGE, RECEIVE }
    protected final @NotNull Stage stage;

    private static final int BYTES_LENGTH = Long.BYTES * 3;

    protected long time1;
    protected long time2;
    protected long time3;

    /** Initiates a new heartbeat */
    public HeartbeatPacket(long deadline, @NotNull Connection connection) {
        this(TurtleUtil.newId(TurtleType.PACKET), deadline, connection, TurtleUtil.newId(TurtleType.RESPONSE_CODE), Direction.OUTBOUND, new byte[BYTES_LENGTH]);
    }

    public HeartbeatPacket(long id, long deadline, @NotNull Connection connection, long responseCode, @NotNull Direction direction, byte[] receivedBytes) {
        super(id, deadline, connection, responseCode, direction, TYPE);

        if (receivedBytes.length < BYTES_LENGTH)
            throw new IllegalArgumentException("HeartbeatPacket missing timings: " + receivedBytes.length + " of " + BYTES_LENGTH + " bytes present.");

        final long time = System.currentTimeMillis();

        this.time1 = MathUtil.bytesToLong(receivedBytes, 0);
        this.time2 = MathUtil.bytesToLong(receivedBytes, 4);
        this.time3 = MathUtil.bytesToLong(receivedBytes, 8);

        this.stage = getState(time1, time2, time3);

        // update timings
        if (stage == Stage.SEND)
            this.time1 = time;
        if (stage == Stage.ACKNOWLEDGE)
            this.time2 = time;
        if (stage == Stage.RECEIVE)
            this.time3 = time;
    }

    private HeartbeatPacket(long deadline, @NotNull Connection connection, long responseCode, long time1, long time2) {
        super(deadline, connection, responseCode, Direction.OUTBOUND, TYPE);
        this.time1 = time1;
        this.time2 = time2;
        this.time3 = 0;

        this.stage = getState(time1, time2, 0);
    }

    @Override
    public byte[] getBytes() {
        byte[] bytes = new byte[Long.BYTES * 3];
        System.arraycopy(MathUtil.longToBytes(time1), 0, bytes,  0, 8);
        System.arraycopy(MathUtil.longToBytes(time2), 0, bytes,  8, 8);
        System.arraycopy(MathUtil.longToBytes(time3), 0, bytes, 16, 8);
        return bytes;
    }

    private static @NotNull Stage getState(long time1, long time2, long time3) throws IllegalStateException {
        if (time1 == 0)
            return Stage.SEND;
        else if (time2 == 0)
            return Stage.ACKNOWLEDGE;
        else if (time3 == 0)
            return Stage.RECEIVE;
        else
            throw new IllegalStateException("Heartbeat may not go past state RECEIVE");
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

    public long getPing() {
        return time3 - time1;
    }

    public @NotNull HeartbeatPacket buildResponse() throws IllegalStateException {
        if (stage != Stage.SEND)
            throw new IllegalStateException("Unable to respond to a heartbeat that is not in stage SEND");

        // keep the deadline from the initial packet
        return new HeartbeatPacket(deadline, connection, responseCode, time1, time2);
    }
}
