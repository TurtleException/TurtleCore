package de.turtle_exception.client.api.request;

import com.google.gson.JsonObject;
import de.turtle_exception.client.internal.net.message.DataMethod;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface DataAction<T> extends Action<T> {
    @NotNull DataMethod getMethod();

    @NotNull Class<?> getContentType();

    @Nullable JsonObject getContent();
}
