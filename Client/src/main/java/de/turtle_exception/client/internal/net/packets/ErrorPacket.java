package de.turtle_exception.client.internal.net.packets;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import de.turtle_exception.client.internal.net.Direction;
import de.turtle_exception.client.internal.net.message.Conversation;
import de.turtle_exception.client.internal.util.time.TurtleType;
import de.turtle_exception.client.internal.util.time.TurtleUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.util.Base64;

public class ErrorPacket extends Packet {
    public static final byte TYPE = 3;

    protected final @NotNull JsonObject json;

    /**
     * An optional {@link Throwable} that will be serialized and added to the json data. This field is not strictly
     * necessary to compile the packet, but it exists to prevent deserializing a Throwable multiple times.
     */
    protected final @Nullable Throwable throwable;

    protected ErrorPacket(long id, @NotNull Conversation conversation, @NotNull JsonObject json) {
        super(id, conversation, Direction.INBOUND, TYPE);
        this.json = json;
        this.throwable = getThrowable(json);
    }

    public ErrorPacket(long id, @NotNull Conversation conversation, @NotNull String msg, @Nullable Throwable t) {
        super(id, conversation, Direction.OUTBOUND, TYPE);

        this.json = new JsonObject();
        this.json.addProperty("message", msg);
        this.json.addProperty("throwable", getString(t));

        this.throwable = t;
    }

    public ErrorPacket(@NotNull Conversation conversation, @NotNull String msg, @Nullable Throwable t) {
        this(TurtleUtil.newId(TurtleType.PACKET), conversation, msg, t);
    }

    public ErrorPacket(long id, @NotNull Conversation conversation, byte[] bytes) {
        this(id, conversation, new Gson().fromJson(new String(bytes), JsonObject.class));
    }

    @Override
    public byte[] getBytes() {
        return new Gson().toJson(this.json).getBytes();
    }

    public @NotNull String getMessage() {
        return this.json.get("message").getAsString();
    }

    public @Nullable Throwable getThrowable() {
        return throwable;
    }

    /* - - - */

    private static @Nullable Throwable getThrowable(@NotNull JsonObject json) {
        try {
            return deserialize(json.get("throwable").getAsString());
        } catch (Exception e) {
            return null;
        }
    }

    private static @Nullable String getString(@Nullable Throwable t) {
        if (t == null) return null;

        try {
            return serialize(t);
        } catch (Exception ignored) {
            return null;
        }
    }

    private static @NotNull String serialize(@NotNull Throwable t) throws IOException {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        ObjectOutputStream  objectStream = new ObjectOutputStream(byteStream);

        objectStream.writeObject(t);
        objectStream.close();

        return Base64.getEncoder().encodeToString(byteStream.toByteArray());
    }

    private static @NotNull Throwable deserialize(@NotNull String str) throws IOException, ClassNotFoundException {
        byte[] bytes = Base64.getDecoder().decode(str);

        ByteArrayInputStream byteStream = new ByteArrayInputStream(bytes);
        ObjectInputStream  objectStream = new ObjectInputStream(byteStream);

        Object obj = objectStream.readObject();
        objectStream.close();

        return (Throwable) obj;
    }
}
