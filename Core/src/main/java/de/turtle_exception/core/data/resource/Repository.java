package de.turtle_exception.core.data.resource;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 *
 * @param <R> Resource type
 * @param <I> Identifier type
 */
public interface Repository<R, I> {
    void save(R obj);

    @Nullable R get(I identifier);

    @NotNull List<R> getAll();

    void delete(I identifier);
}
