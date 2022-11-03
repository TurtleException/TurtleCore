package de.turtle_exception.core.internal.data;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.annotation.AnnotationFormatError;
import java.util.List;

public interface Provider {
    <T> @Nullable T get(Class<T> type, Object primaryKey, Object... primaryKeys)
            throws IllegalArgumentException, AnnotationFormatError;

    <T> @NotNull List<T> getAll(Class<T> type)
            throws IllegalArgumentException, AnnotationFormatError;


}
