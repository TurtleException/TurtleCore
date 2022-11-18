package de.turtle_exception.client.internal.net;

import de.turtle_exception.client.internal.request.IResponse;
import de.turtle_exception.client.internal.util.logging.NestedLogger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RequestCallbackPool {
    private final Object lock = new Object();

    private final long timeout;
    private final NestedLogger logger;
    private final HashMap<Long, Entry> entries = new HashMap<>();

    public RequestCallbackPool(long timeout, @NotNull Logger parentLogger) {
        this.timeout = timeout;
        this.logger = new NestedLogger("RequestCallbackPool", parentLogger);
    }

    public void put(long responseCode, @NotNull CompletableFuture<IResponse> future) {
        Entry entry = new Entry(responseCode, System.currentTimeMillis() + timeout, future);
        synchronized (lock) {
            entries.put(responseCode, entry);
            this.logger.log(Level.FINER, "Submitted new future for response-code " + responseCode);
        }
    }

    public @Nullable CompletableFuture<IResponse> get(long responseCode) {
        this.logger.log(Level.FINEST, "Requested future for response-code " + responseCode);
        synchronized (lock) {
            Entry entry = entries.remove(responseCode);
            if (entry != null)
                return entry.future();
            return null;
        }
    }

    public boolean remove(long responseCode) {
        this.logger.log(Level.FINEST, "Requested deletion of future for response-code " + responseCode);
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
