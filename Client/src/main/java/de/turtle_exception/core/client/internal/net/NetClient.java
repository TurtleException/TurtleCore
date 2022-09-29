package de.turtle_exception.core.client.internal.net;

import de.turtle_exception.core.client.api.TurtleClient;
import de.turtle_exception.core.client.api.requests.Request;
import de.turtle_exception.core.client.internal.ActionImpl;
import de.turtle_exception.core.client.internal.TurtleClientImpl;
import de.turtle_exception.core.netcore.net.ConnectionStatus;
import de.turtle_exception.core.netcore.net.NetworkAdapter;
import de.turtle_exception.core.netcore.net.message.OutboundMessage;
import de.turtle_exception.core.netcore.net.route.Routes;
import de.turtle_exception.core.netcore.util.AsyncLoopThread;
import de.turtle_exception.core.netcore.util.logging.NestedLogger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;

/** The actual client part of the {@link TurtleClient}. */
public class NetClient extends NetworkAdapter {
    private final TurtleClientImpl client;

    private final String host;
    private final int port;

    /** The username to log in with */
    private final String login;
    /** The password used for en-/decryption */
    private final String pass;

    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    public NetClient(TurtleClientImpl client, String host, @Range(from = 0, to = 65535) int port, String login, String pass) {
        super(client, new NestedLogger("Client#" + port, client.getLogger()));

        this.client = client;

        this.host = host;
        this.port = port;

        this.login = login;
        this.pass = pass;
    }

    public void start() throws IOException {
        // create the underlying socket (the spicy part)
        this.socket = new Socket(host, port);
        this.status = ConnectionStatus.CONNECTED;

        this.out = new PrintWriter(socket.getOutputStream(), true);
        this.in  = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        this.receiver = new AsyncLoopThread(() -> status != ConnectionStatus.DISCONNECTED, () -> {
            try {
                this.handleInbound(in.readLine());
            } catch (IOException e) {
                client.getLogger().log(Level.WARNING, "Could not read input from client " + socket.getInetAddress(), e);
            }
        });

        // TODO: login
    }

    @Override
    public void stop() throws IOException {
        // notify server
        new ActionImpl<Void>(client, Routes.Login.QUIT, null).queue();

        this.quit();
    }

    public void quit() throws IOException {
        this.stopReceiver();
        this.awaitExecutorShutdown();

        // close socket
        this.status = ConnectionStatus.DISCONNECTED;
        this.in.close();
        this.out.close();
        this.socket.close();
    }

    @Override
    protected void send(@NotNull String msg) {
        this.out.write(msg);
    }

    /* - - - */

    public <T> void request(@NotNull Request<T> request) {
        try {
            this.submit(new OutboundMessage(client, request.getRoute().setCallbackCode(callbackRegistrar.newCode()).build(), request.getDeadline(), request::handleResponse));
        } catch (Throwable t) {
            request.onFailure(t);
        }
    }
}
