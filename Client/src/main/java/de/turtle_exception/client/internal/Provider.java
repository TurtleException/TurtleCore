package de.turtle_exception.client.internal;

import com.google.gson.JsonObject;
import de.turtle_exception.client.api.request.DataAction;
import de.turtle_exception.client.internal.util.logging.NestedLogger;
import org.jetbrains.annotations.NotNull;

import java.lang.annotation.AnnotationFormatError;

/** Source of data */
public abstract class Provider {
    protected TurtleClientImpl client;
    protected NestedLogger logger;

    protected Provider() { }

    /* - - - */

    public abstract <T> @NotNull DataAction<Boolean> delete(@NotNull Class<T> type, @NotNull Object primary) throws AnnotationFormatError;

    public abstract <T> @NotNull DataAction<JsonObject> get(@NotNull Class<T> type, @NotNull Object primary) throws AnnotationFormatError;

    public abstract <T> @NotNull DataAction<JsonObject> put(@NotNull Class<T> type, @NotNull JsonObject data) throws AnnotationFormatError;

    public abstract <T> @NotNull DataAction<JsonObject> patch(@NotNull Class<T> type, @NotNull JsonObject data, @NotNull Object primary) throws AnnotationFormatError;

    /* - - - */

    final void setClient(@NotNull TurtleClientImpl client) {
        this.client = client;
        this.logger = new NestedLogger("Provider", client.getLogger());
    }
}
