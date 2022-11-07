package de.turtle_exception.client.internal.net.message;

import de.turtle_exception.client.internal.net.Connection;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

public class Conversation {
    private final ConcurrentHashMap<Long, Message> messages = new ConcurrentHashMap<>();
    private CompletableFuture<Message> nextMessage = new CompletableFuture<>();

    private final Connection connection;
    private final long id;

    public Conversation(@NotNull Connection connection, long id) {
        this.connection = connection;
        this.id = id;
    }

    public synchronized @NotNull CompletableFuture<Message> append(@NotNull Message message) {
        messages.put(System.currentTimeMillis(), message);
        this.nextMessage.complete(message);
        this.nextMessage = new CompletableFuture<>();
        return nextMessage;
    }

    public void close() {
        connection.closeConversation(this);
        this.nextMessage.cancel(false);
    }

    /* - - -*/

    public @NotNull Connection getConnection() {
        return connection;
    }

    public long getId() {
        return id;
    }

    public @NotNull Map<Long, Message> getMessages() {
        return Map.copyOf(messages);
    }

    public boolean isEmpty() {
        return messages.isEmpty();
    }
}
