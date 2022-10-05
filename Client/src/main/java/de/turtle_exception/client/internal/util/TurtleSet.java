package de.turtle_exception.client.internal.util;

import de.turtle_exception.client.api.entities.Turtle;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A simple set containing Turtle entities. The set is backed by a {@link ConcurrentHashMap} and values are mapped to
 * their unique id.
 * <p> This implementation should be thread-safe.
 * @see Turtle#getId()
 */
public class TurtleSet<T extends Turtle> implements Set<T> {
    private final ConcurrentHashMap<Long, T> content = new ConcurrentHashMap<>();

    /** Returns the value with the specified id, or {@code null} if this set does not contain that value. */
    public @Nullable T get(long id) {
        return content.get(id);
    }

    @Override
    public int size() {
        return content.size();
    }

    @Override
    public boolean isEmpty() {
        return content.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        if (o instanceof Turtle turtleObject) {
            return content.get(turtleObject.getId()) != null;
        }
        return false;
    }

    @Override
    public @NotNull Iterator<T> iterator() {
        return content.values().iterator();
    }

    @Override
    public Object @NotNull [] toArray() {
        return content.values().toArray();
    }

    @Override
    @SuppressWarnings({"SuspiciousToArrayCall", "NullableProblems"})
    public <E> E @NotNull [] toArray(E[] a) {
        return content.values().toArray(a);
    }

    @Override
    public boolean add(T t) {
        // comparing the old and new value is not necessary because the id should be unique to this object.
        return content.put(t.getId(), t) == null;
    }

    @Override
    public boolean remove(Object o) {
        return content.remove(o) != null;
    }

    public boolean removeStringId(@NotNull String id) {
        try {
            return this.remove(Long.parseLong(id));
        } catch (NumberFormatException e) {
            return false;
        }
    }

    @Override
    public boolean containsAll(@NotNull Collection<?> c) {
        return content.values().containsAll(c);
    }

    @Override
    public boolean addAll(@NotNull Collection<? extends T> c) {
        boolean b = false;
        for (T e : c)
            b = this.add(e) || b;
        return b;
    }

    @Override
    public boolean retainAll(@NotNull Collection<?> c) {
        boolean b = false;
        for (T e : content.values())
            if (!c.contains(e))
                b = this.remove(e) || b;
        return b;
    }

    @Override
    public boolean removeAll(@NotNull Collection<?> c) {
        boolean b = false;
        for (Object e : c)
            b = this.remove(e) || b;
        return b;
    }

    @Override
    public void clear() {
        content.clear();
    }
}
