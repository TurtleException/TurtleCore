package de.turtle_exception.server.data;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import de.turtle_exception.client.api.entities.Turtle;
import de.turtle_exception.client.internal.ActionImpl;
import de.turtle_exception.client.internal.Provider;
import de.turtle_exception.client.internal.request.actions.SimpleAction;
import org.jetbrains.annotations.NotNull;

import java.lang.annotation.AnnotationFormatError;
import java.util.logging.Level;

public abstract class DatabaseProvider extends Provider {
    protected DatabaseProvider() {
        super(1);
    }

    @Override
    public final <T extends Turtle> @NotNull SimpleAction<Boolean> delete(@NotNull Class<T> type, long id) throws AnnotationFormatError {
        this.logger.log(Level.FINER, "DELETE request for id " + id);
        return new SimpleAction<>(this, () -> this.doDelete(type, id));
    }

    @Override
    public final <T extends Turtle> @NotNull SimpleAction<JsonObject> get(@NotNull Class<T> type, long id) throws AnnotationFormatError {
        this.logger.log(Level.FINER, "GET request for id " + id);
        return new SimpleAction<>(this, () -> this.doGet(type, id));
    }

    @Override
    public final <T extends Turtle> @NotNull SimpleAction<JsonArray> get(@NotNull Class<T> type) throws AnnotationFormatError {
        this.logger.log(Level.FINER, "GET request for type " + type.getSimpleName());
        return new SimpleAction<>(this, () -> this.doGet(type));
    }

    @Override
    public final <T extends Turtle> @NotNull SimpleAction<JsonObject> put(@NotNull Class<T> type, @NotNull JsonObject content) throws AnnotationFormatError {
        this.logger.log(Level.FINER, "PUT request for object of type " + type.getSimpleName());
        return new SimpleAction<>(this, () -> this.doPut(type, content));
    }

    @Override
    public final <T extends Turtle> @NotNull SimpleAction<JsonObject> patch(@NotNull Class<T> type, @NotNull JsonObject content, long id) throws AnnotationFormatError {
        this.logger.log(Level.FINER, "PATCH request for id " + id);
        return new SimpleAction<>(this, () -> this.doPatch(type, content, id));
    }

    @Override
    public final <T extends Turtle> @NotNull ActionImpl<JsonObject> patchEntryAdd(@NotNull Class<T> type, long id, @NotNull String key, @NotNull Object obj) {
        this.logger.log(Level.FINER, "PATCH_ENTRY_ADD request for id " + id);
        return new SimpleAction<>(this, () -> this.doPatchEntry(type, id, key, obj, true));
    }

    @Override
    public final <T extends Turtle> @NotNull ActionImpl<JsonObject> patchEntryDel(@NotNull Class<T> type, long id, @NotNull String key, @NotNull Object obj) {
        this.logger.log(Level.FINER, "PATCH_ENTRY_DEL request for id " + id);
        return new SimpleAction<>(this, () -> this.doPatchEntry(type, id, key, obj, false));
    }

    /* - - - */

    protected abstract boolean doDelete(Class<? extends Turtle> type, long id) throws Exception;

    protected abstract JsonObject doGet(Class<? extends Turtle> type, long id) throws Exception;

    protected abstract JsonArray doGet(Class<? extends Turtle> type) throws Exception;

    protected abstract JsonObject doPut(Class<? extends Turtle> type, JsonObject content) throws Exception;

    protected abstract JsonObject doPatch(Class<? extends Turtle> type, JsonObject content, long id) throws Exception;

    protected abstract JsonObject doPatchEntry(Class<? extends Turtle> type, long id, String key, Object obj, boolean add) throws Exception;
}
