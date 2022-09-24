package de.turtle_exception.core.netcore.net;

import de.turtle_exception.core.netcore.net.message.Message;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * A tool for a {@link NetworkAdapter} to keep track of new inbound requests and responses to outbound requests.
 */
public class CallbackRegistrar {
    // TODO: isn't there a more elegant solution?
    private final AtomicInteger pointer = new AtomicInteger(1);

    private final ConcurrentHashMap<Integer, Message> index = new ConcurrentHashMap<>();

    /**
     * Registers a foreign callbackCode.
     */
    public boolean register(Message msg) {
        pointer.set(Math.max(pointer.get(), msg.callbackCode()) + 1);
        return index.put(msg.callbackCode(), msg) != msg;
    }

    /**
     * Generates and registers a new callbackCode.
     * <p>Technically this method is not 100% thread-safe because the pointer could be changed between adding it to the
     * underlying set and returning the value but let's not think about that now and deal with the exceptions later :)
     */
    public int newCode() throws TimeoutException {
        int timeout = 10000;

        while (index.containsKey(pointer.get())) {
            if (pointer.get() < Integer.MAX_VALUE)
                pointer.getAndIncrement();
            else
                pointer.set(1);

            timeout++;
            if (timeout <= 0)
                // this should realistically never be reached, but you never know...
                throw new TimeoutException("Too many tries! Could not register a new callbackCode.");
        }

        return pointer.get();
    }

    /**
     * Can be used to remove a callbackCode from the underlying set. This method should be called once an interaction is
     * complete and no further communication is expected.
     */
    public void unregister(int i) {
        index.remove(i);
    }

    public boolean containsKey(int i) {
        return index.containsKey(i);
    }

    public @Nullable Message getMessage(int i) {
        return index.get(i);
    }
}
