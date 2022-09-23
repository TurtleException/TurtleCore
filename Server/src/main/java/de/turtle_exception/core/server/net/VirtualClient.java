package de.turtle_exception.core.server.net;

import de.turtle_exception.core.client.internal.util.AsyncLoopThread;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;

/** A virtual client on the server-side of the application. Used as a communication interface. */
public class VirtualClient {
    private final InternalServer internalServer;
    private final Socket socket;
    private final PrintWriter out;
    private final BufferedReader in;

    private final AsyncLoopThread inputReader;

    private String login;
    private String pass;

    // TODO: use this
    public enum Status {
        CONNECTED, LOGIN, LOGGED_IN, DISCONNECTED;
    }
    private Status status = Status.CONNECTED;

    VirtualClient(InternalServer internalServer, Socket socket) throws IOException {
        this.internalServer = internalServer;
        this.socket = socket;

        this.out = new PrintWriter(socket.getOutputStream(), true);
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        this.inputReader = new AsyncLoopThread(() -> status != Status.DISCONNECTED, () -> {
            try {
                receive(in.readLine());
            } catch (IOException e) {
                internalServer.logger.log(Level.WARNING, "Could not read input from client " + socket.getInetAddress(), e);
            }
        });
    }

    /* - - - */

    void send(@NotNull Message msg) {
        String str = Message.parseToClient(msg);
        // TODO: encrypt
        out.write(str);
    }

    private void receive(@NotNull String msg) {
        String str = msg; // TODO: decrypt
        Message message = Message.parseFromClient(this, msg);
        internalServer.receive(message);
    }

    /* - - - */

    public @NotNull String getIdentifier() {
        return login;
    }

    void closeSocket() throws IOException {
        status = Status.DISCONNECTED;
        this.in.close();
        this.out.close();
        this.socket.close();
    }

    public @NotNull Status getStatus() {
        return status;
    }

    InternalServer getInternalServer() {
        return internalServer;
    }

    Socket getSocket() {
        return socket;
    }
}
