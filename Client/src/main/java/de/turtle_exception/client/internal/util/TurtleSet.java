package de.turtle_exception.client.internal.util;

import de.turtle_exception.client.api.entities.Turtle;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;

/**
 * A simple set containing Turtle entities. The set is backed by a {@link ConcurrentHashMap} and values are mapped to
 * their unique id.
 * <p> This implementation should be thread-safe.
 * @see Turtle#getId()
 */
public class TurtleSet<T extends Turtle> implements Set<T> {
    private final Object lock = new Object();
    private final ConcurrentHashMap<Long, T> content = new ConcurrentHashMap<>();

    /** Returns the value with the specified id, or {@code null} if this set does not contain that value. */
    public @Nullable T get(long id) {
        synchronized (lock) {
            return content.get(id);
        }
    }

    @SuppressWarnings("unchecked")
    public <T1> @Nullable T1 get(long id, Class<T1> type) {
        T val = this.get(id);
        if (type.isInstance(val))
            return (T1) val;
        return null;
    }

    @Override
    public int size() {
        synchronized (lock) {
            return content.size();
        }
    }

    @Override
    public boolean isEmpty() {
        synchronized (lock) {
            return content.isEmpty();
        }
    }

    @Override
    public boolean contains(Object o) {
        synchronized (lock) {
            if (o instanceof Turtle turtleObject) {
                return content.get(turtleObject.getId()) != null;
            }
            return false;
        }
    }

    public boolean containsId(long id) {
        synchronized (lock) {
            return content.get(id) != null;
        }
    }

    @Override
    public @NotNull Iterator<T> iterator() {
        synchronized (lock) {
            return content.values().iterator();
        }
    }

    @Override
    public Object @NotNull [] toArray() {
        synchronized (lock) {
            return content.values().toArray();
        }
    }

    @Override
    @SuppressWarnings({"SuspiciousToArrayCall", "NullableProblems"})
    public <E> E @NotNull [] toArray(E[] a) {
        synchronized (lock) {
            return content.values().toArray(a);
        }
    }

    @Override
    public boolean add(T t) {
        synchronized (lock) {
            // comparing the old and new value is not necessary because the id should be unique to this object.
            return content.put(t.getId(), t) == null;
        }
    }

    public @Nullable T put(@NotNull T t) {
        synchronized (lock) {
            return content.put(t.getId(), t);
        }
    }

    @Override
    public boolean remove(Object o) {
        synchronized (lock) {
            return content.remove(o) != null;
        }
    }

    public T removeById(long id) {
        synchronized (lock) {
            return content.remove(id);
        }
    }

    public T removeById(@NotNull String str) throws NumberFormatException {
        synchronized (lock) {
            return this.removeById(Long.parseLong(str));
        }
    }

    @Override
    public boolean containsAll(@NotNull Collection<?> c) {
        synchronized (lock) {
            return content.values().containsAll(c);
        }
    }

    @Override
    public boolean addAll(@NotNull Collection<? extends T> c) {
        synchronized (lock) {
            boolean b = false;
            for (T e : c)
                b = this.add(e) || b;
            return b;
        }
    }

    @Override
    public boolean retainAll(@NotNull Collection<?> c) {
        synchronized (lock) {
            boolean b = false;
            for (T e : content.values())
                if (!c.contains(e))
                    b = this.remove(e) || b;
            return b;
        }
    }

    @Override
    public boolean removeAll(@NotNull Collection<?> c) {
        synchronized (lock) {
            boolean b = false;
            for (Object e : c)
                b = this.remove(e) || b;
            return b;
        }
    }

    public int removeAll(@NotNull Predicate<T> predicate) {
        synchronized (lock) {
            int i = 0;
            for (T value : content.values()) {
                if (predicate.test(value)) {
                    content.remove(value.getId(), value);
                    i++;
                }
            }
            return i;
        }
    }

    public int removeAll(@NotNull Class<? extends T> type) {
        return this.removeAll(type::isInstance);
    }

    @Override
    public void clear() {
        synchronized (lock) {
            content.clear();
        }
    }
}
