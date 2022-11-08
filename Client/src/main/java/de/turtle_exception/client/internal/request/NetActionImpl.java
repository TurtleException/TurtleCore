package de.turtle_exception.client.internal.request;

import com.google.gson.JsonObject;
import de.turtle_exception.client.api.TurtleClient;
import de.turtle_exception.client.api.request.NetAction;
import de.turtle_exception.client.internal.net.Connection;
import de.turtle_exception.client.internal.net.message.Message;
import de.turtle_exception.client.internal.net.message.Route;
import de.turtle_exception.client.internal.util.ExceptionalFunction;
import de.turtle_exception.client.internal.util.time.TurtleUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class NetActionImpl<T> implements NetAction<T> {
    protected @NotNull Connection connection;
    protected @NotNull Route route;
    protected @NotNull JsonObject json;

    private final @NotNull ExceptionalFunction<? super Message, T> finalizer;

    protected long conversation;
    protected boolean terminating;
    protected long deadline;

    protected Consumer<? super T>         successFinalizer = null;
    protected Consumer<? super Throwable> failureFinalizer = null;

    private NetActionImpl(@NotNull Connection connection, @NotNull Route route, @NotNull JsonObject json, boolean terminating, @NotNull ExceptionalFunction<? super Message, T> finalizer) {
        this.connection = connection;
        this.route = route;
        this.json = json.deepCopy();

        this.finalizer = finalizer;

        this.terminating = terminating;

        this.conversation = TurtleUtil.newId(0);
    }

    public NetActionImpl(@NotNull Connection connection, @NotNull Route route, @NotNull JsonObject json) {
        this(connection, route, json, true, message -> null);
    }

    public NetActionImpl(@NotNull Connection connection, @NotNull Route route, @NotNull JsonObject json, @NotNull ExceptionalFunction<? super Message, T> finalizer) {
        this(connection, route, json, false, finalizer);
    }

    @Override
    public @NotNull TurtleClient getClient() {
        return connection.getAdapter().getClient();
    }

    @Override
    public @NotNull Connection getConnection() {
        return connection;
    }

    @Override
    public @NotNull Route getRoute() {
        return route;
    }

    @Override
    public @NotNull JsonObject getJson() {
        this.prepareJson();
        return json;
    }

    @Override
    public @NotNull Message buildMessage() throws Exception {
        return new Message(connection, route, conversation, deadline, getJson());
    }

    protected void prepareJson() {
        this.json.addProperty("route", route.code);
        this.json.addProperty("conversation", conversation);
        this.json.addProperty("deadline", deadline);
    }

    /* - - - */

    public @NotNull NetActionImpl<T> onSuccess(@Nullable Consumer<? super T> successFinalizer) {
        this.successFinalizer = successFinalizer;
        return this;
    }

    public @NotNull NetActionImpl<T> onFailure(@Nullable Consumer<? super Throwable> failureFinalizer) {
        this.failureFinalizer = failureFinalizer;
        return this;
    }

    @Override
    public final @NotNull T createResult(@NotNull Message response) throws Exception {
        return this.finalizer.apply(response);
    }
}
