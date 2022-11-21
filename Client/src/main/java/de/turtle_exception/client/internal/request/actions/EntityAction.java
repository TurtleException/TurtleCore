package de.turtle_exception.client.internal.request.actions;

import com.google.gson.JsonObject;
import de.turtle_exception.client.api.entities.Turtle;
import de.turtle_exception.client.api.request.Action;
import de.turtle_exception.client.internal.Provider;
import de.turtle_exception.client.internal.util.function.ExceptionalConsumer;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public abstract class EntityAction<T extends Turtle> implements Action<T> {
    protected final @NotNull Provider provider;
    protected final @NotNull Class<T> type;

    protected @NotNull JsonObject content;

    protected final @NotNull List<ExceptionalConsumer<JsonObject>> checks = new ArrayList<>();

    public EntityAction(@NotNull Provider provider, @NotNull Class<T> type) {
        this.provider = provider;
        this.type = type;

        this.content = new JsonObject();
    }

    @Override
    public @NotNull Provider getProvider() {
        return provider;
    }

    @Override
    public @NotNull CompletableFuture<T> submit() {
        try {
            this.updateContent();
            for (ExceptionalConsumer<JsonObject> check : checks)
                check.accept(content);
        } catch (Exception e) {
            return CompletableFuture.failedFuture(new IllegalArgumentException("Check failed.", e));
        }

        return this.provider.put(type, content).andThenParse(type).submit();
    }

    protected abstract void updateContent();
}
