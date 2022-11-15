package de.turtle_exception.client.internal;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import de.turtle_exception.client.api.entities.Turtle;
import de.turtle_exception.client.api.request.Action;
import de.turtle_exception.client.internal.request.actions.ProxyAction;
import de.turtle_exception.client.internal.util.ExceptionalFunction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public abstract class ActionImpl<T> implements Action<T> {
    protected final @NotNull Provider provider;

    protected boolean priority = false;

    protected @NotNull Consumer<T> consumer = t -> { };

    public ActionImpl(@NotNull Provider provider) {
        this.provider = provider;
    }

    @Override
    public @NotNull Provider getProvider() {
        return provider;
    }

    @Override
    public @NotNull CompletableFuture<T> submit() {
        return provider.submit(this);
    }

    /* - BUILDER - */

    public ActionImpl<T> priority() {
        priority = true;
        return this;
    }

    public ActionImpl<T> onSuccess(@Nullable Consumer<T> consumer) {
        this.consumer = consumer != null ? consumer : t -> { };
        return this;
    }

    public <U> ProxyAction<T, U> andThen(@NotNull ExceptionalFunction<T, U> function) {
        return new ProxyAction<>(this, function);
    }

    public <U extends Turtle> ProxyAction<T, U> andThenParse(@NotNull Class<U> type) {
        return this.andThen(t -> {
            // TODO: maybe move getJsonBuilder() to TurtleClient to avoid casting here
            if (t instanceof JsonObject json)
                return ((TurtleClientImpl) getClient()).getJsonBuilder().buildObject(type, json);
            throw new ClassCastException("Expected return type JsonObject");
        });
    }

    public <U extends Turtle> ProxyAction<T, List<U>> andThenParseList(@NotNull Class<U> type) {
        return this.andThen(t -> {
            // TODO: maybe move getJsonBuilder() to TurtleClient to avoid casting here
            if (t instanceof JsonArray json)
                return ((TurtleClientImpl) getClient()).getJsonBuilder().buildObjects(type, json);
            throw new ClassCastException("Expected return type JsonArray");
        });
    }

    /* - - - */

    public boolean isPriority() {
        return priority;
    }

    /* - - - */

    /** Proxy method, so that {@link ActionImpl#asCallable()} can be used by the {@link Provider}. */
    @NotNull Callable<T> getCallable() throws IllegalStateException {
        return () -> {
            T result = this.asCallable().call();
            this.consumer.accept(result);
            return result;
        };
    }

    protected abstract @NotNull Callable<T> asCallable() throws IllegalStateException;
}
