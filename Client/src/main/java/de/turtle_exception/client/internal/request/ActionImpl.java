package de.turtle_exception.client.internal.request;

import com.google.gson.JsonObject;
import de.turtle_exception.client.api.TurtleClient;
import de.turtle_exception.client.api.request.Action;
import de.turtle_exception.client.internal.net.Connection;
import de.turtle_exception.client.internal.net.message.Message;
import de.turtle_exception.client.internal.net.message.Route;
import de.turtle_exception.client.internal.util.time.TurtleUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class ActionImpl<T> implements Action<T> {
    protected @NotNull Connection connection;
    protected @NotNull Route route;
    protected @NotNull JsonObject json;

    protected final long conversation;
    protected long deadline;

    protected Consumer<? super T>         successFinalizer = null;
    protected Consumer<? super Throwable> failureFinalizer = null;

    public ActionImpl(@NotNull Connection connection, @NotNull Route route, @NotNull JsonObject json) {
        this.connection = connection;
        this.route = route;
        this.json = json.deepCopy();

        this.conversation = TurtleUtil.newId(0);
    }

    @Override
    public @NotNull TurtleClient getClient() {
        return connection.getAdapter().getClient();
    }

    public @NotNull Connection getConnection() {
        return connection;
    }

    @Override
    public @NotNull CompletableFuture<T> submit() {
        Message message = this.buildMessage();
        // TODO: dispatch
    }

    @Override
    public void queue(@Nullable Consumer<T> success, @Nullable Consumer<Throwable> failure) {
        Message message = this.buildMessage();
        // TODO: dispatch
    }

    protected @NotNull Message buildMessage() {
        this.prepareJson();
        return new Message(connection, route, conversation, deadline, json);
    }

    protected void prepareJson() {
        this.json.addProperty("route", route.code);
        this.json.addProperty("conversation", conversation);
        this.json.addProperty("deadline", deadline);
    }

    /* - - - */

    public @NotNull ActionImpl<T> onSuccess(@Nullable Consumer<? super T> successFinalizer) {
        this.successFinalizer = successFinalizer;
        return this;
    }

    public @NotNull ActionImpl<T> onFailure(@Nullable Consumer<? super Throwable> failureFinalizer) {
        this.failureFinalizer = failureFinalizer;
        return this;
    }
}
