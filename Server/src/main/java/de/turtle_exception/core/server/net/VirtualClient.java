package de.turtle_exception.core.server.net;

import de.turtle_exception.core.core.net.ConnectionStatus;
import de.turtle_exception.core.core.net.NetworkAdapter;
import de.turtle_exception.core.core.util.AsyncLoopThread;
import de.turtle_exception.core.core.util.logging.NestedLogger;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;

/** A virtual client on the server-side of the application. Used as a communication interface. */
public class VirtualClient extends NetworkAdapter {
    private final InternalServer internalServer;
    private final Socket socket;
    private final PrintWriter out;
    private final BufferedReader in;

    VirtualClient(InternalServer internalServer, Socket socket, @NotNull String login, @NotNull String pass) throws IOException {
        super(internalServer.getServer(), new NestedLogger("Client#" + login, internalServer.getLogger()), login, pass);
        this.internalServer = internalServer;
        this.socket = socket;

        this.out = new PrintWriter(socket.getOutputStream(), true);
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        this.receiver = new AsyncLoopThread(() -> status != ConnectionStatus.DISCONNECTED, () -> {
            try {
                this.handleInbound(in.readLine());
            } catch (IOException e) {
                logger.log(Level.WARNING, "Could not read input from client.", e);
            }
        });
    }

    /* - - - */
    @Override
    public void stop() throws IOException {
        // TODO
    }

    @Override
    protected void send(@NotNull String msg) {
        this.out.println(msg);
    }

    /* - - - */

    public @NotNull String getIdentifier() {
        return login;
    }

    void closeSocket() throws IOException {
        status = ConnectionStatus.DISCONNECTED;
        this.in.close();
        this.out.close();
        this.socket.close();
    }

    public @NotNull ConnectionStatus getStatus() {
        return status;
    }

    InternalServer getInternalServer() {
        return internalServer;
    }
}
