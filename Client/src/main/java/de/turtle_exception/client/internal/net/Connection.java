package de.turtle_exception.client.internal.net;

import de.turtle_exception.client.internal.util.Worker;
import de.turtle_exception.client.internal.util.logging.NestedLogger;
import org.jetbrains.annotations.NotNull;

import javax.security.auth.login.LoginException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class Connection {
    private final NetworkAdapter adapter;
    private final NestedLogger logger;

    private final Socket socket;
    private final PrintWriter out;
    private final BufferedReader in;

    private final String pass;

    private final Worker receiver;

    public enum Status { INIT, LOGIN, CONNECTED, DISCONNECTED }
    private Status status;

    private final ConcurrentHashMap<Long, /* TODO */ Void> conversations = new ConcurrentHashMap<>();

    public Connection(@NotNull NetworkAdapter adapter, @NotNull Socket socket, @NotNull Handshake handshake, @NotNull String pass) throws IOException, LoginException {
        this.status = Status.INIT;

        this.adapter = adapter;
        this.logger = new NestedLogger(null /* TODO */, adapter.getLogger());

        this.socket = socket;
        this.out = new PrintWriter(socket.getOutputStream(), true);
        this.in  = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        this.pass = pass;

        this.status = Status.LOGIN;
        handshake.start();
        handshake.await(10, TimeUnit.SECONDS);
        this.status = Status.CONNECTED;

        this.receiver = new Worker(() -> status == Status.CONNECTED, () -> {
            // TODO
        });
    }

    public void stop() throws IOException {
        // TODO: notify the other side
        this.status = Status.DISCONNECTED;
        this.socket.close();
    }

    public NetworkAdapter getAdapter() {
        return adapter;
    }
}
