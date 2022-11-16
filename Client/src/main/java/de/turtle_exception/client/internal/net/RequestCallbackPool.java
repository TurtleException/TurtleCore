package de.turtle_exception.client.internal.net;

import de.turtle_exception.client.internal.request.IResponse;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

public class RequestCallbackPool {
    private final Object lock = new Object();

    private final long timeout;

    private final HashMap<Long, Entry> entries = new HashMap<>();

    public RequestCallbackPool(long timeout) {
        this.timeout = timeout;
    }

    public void put(long responseCode, @NotNull CompletableFuture<IResponse> future) {
        Entry entry = new Entry(responseCode, System.currentTimeMillis() + timeout, future);
        synchronized (lock) {
            entries.put(responseCode, entry);
        }
    }

    public @Nullable CompletableFuture<IResponse> get(long responseCode) {
        synchronized (lock) {
            Entry entry = entries.remove(responseCode);
            if (entry != null)
                return entry.future();
            return null;
        }
    }

    public boolean remove(long responseCode) {
        synchronized (lock) {
            for (Long l : entries.keySet())
                if (l == responseCode)
                    return entries.remove(l) != null;
            return false;
        }
    }

    /* - - - */

    private record Entry(
            long responseCode,
            long deadline,
            CompletableFuture<IResponse> future
    ) { }
}
