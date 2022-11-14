package de.turtle_exception.client.internal.net;

import de.turtle_exception.client.internal.NetworkAdapter;
import de.turtle_exception.client.internal.net.packets.*;
import de.turtle_exception.client.internal.util.Worker;
import de.turtle_exception.client.internal.util.logging.NestedLogger;
import kotlin.NotImplementedError;
import org.jetbrains.annotations.NotNull;

import javax.security.auth.login.LoginException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;

public class Connection {
    private final NetworkAdapter adapter;
    private final NestedLogger logger;

    private final Socket socket;
    private final PrintWriter out;
    private final BufferedReader in;

    private final Handshake handshake;
    private String pass;

    private final Worker receiver;

    public enum Status { INIT, LOGIN, CONNECTED, DISCONNECTED;}
    volatile Status status;

    private final ConcurrentHashMap<CompiledPacket, CompletableFuture<Packet>> responses = new ConcurrentHashMap<>();

    public Connection(@NotNull NetworkAdapter adapter, @NotNull Socket socket, @NotNull Handshake handshake, String pass) throws IOException, LoginException {
        this.status = Status.INIT;

        this.adapter = adapter;
        this.logger = new NestedLogger("CON#" + socket.getInetAddress() + ":" + socket.getPort(), adapter.getLogger());

        this.socket = socket;
        this.out = new PrintWriter(socket.getOutputStream(), true);
        this.in  = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        this.status = Status.LOGIN;
        this.handshake = handshake;
        this.handshake.setConnection(this);
        this.handshake.init();

        this.pass = pass;

        this.receiver = new Worker(() -> status != Status.DISCONNECTED, () -> {
            try {
                this.receive(in.readLine());
            } catch (IOException e) {
                logger.log(Level.WARNING, "Could not read input", e);
            }
            this.clearTimedOutResponses();
        });
    }

    public boolean stop(boolean notify) {
        if (notify)
            this.send(new HandshakePacket(Long.MAX_VALUE, this, "QUIT").compile());

        this.status = Status.DISCONNECTED;
        this.receiver.interrupt();
        try {
            this.socket.close();
            return true;
        } catch (IOException e) {
            this.logger.log(Level.WARNING, "Could not close connection!", e);
            return false;
        }
    }

    /* - - - */

    public synchronized void send(@NotNull CompiledPacket packet) {
        try {
            // this can't be the most efficient way to do this, right? right...?
            this.out.println(new String(packet.getBytes()));
        } catch (Error e) {
            logger.log(Level.SEVERE, "Encountered an Error when attempting to send a packet", e);
        } catch (Throwable t) {
            logger.log(Level.WARNING, "Encountered an Exception when attempting to send a packet", t);
        }
    }

    private void receive(@NotNull String msg) {
        long deadline = System.currentTimeMillis() + adapter.getClient().getDefaultTimeoutInbound();

        try {
            this.receive(new CompiledPacket(msg.getBytes(), Direction.INBOUND, this, deadline));
        } catch (Error e) {
            logger.log(Level.SEVERE, "Encountered an Error when attempting to receive a packet", e);
        } catch (Throwable t) {
            logger.log(Level.WARNING, "Encountered an Exception when attempting to receive a packet", t);
        }
    }

    /* - - - */

    public @NotNull CompletableFuture<Response> send(@NotNull Request request) {
        // TODO

        return new CompletableFuture<>();
    }

    public void receive(@NotNull CompiledPacket packet) throws Exception {
        if (packet.getTypeId() == HeartbeatPacket.TYPE) {
            HeartbeatPacket pck = (HeartbeatPacket) packet.toPacket();

            if (pck.getStage() != HeartbeatPacket.Stage.RECEIVE) {
                this.send(pck.buildResponse().compile());
                return;
            }

            logger.log(Level.FINER, "Heartbeat successful: " + pck.getPing() + "ms");
            return;
        }

        if (packet.getTypeId() == HandshakePacket.TYPE) {
            HandshakePacket pck = (HandshakePacket) packet.toPacket();

            if (pck.getMessage().equals("QUIT")) {
                this.stop(false);
                return;
            }

            if (status != Status.LOGIN)
                throw new IllegalStateException("Unexpected Handshake packet while not in login");

            handshake.handle(pck);
            return;
        }

        if (packet.getTypeId() == ErrorPacket.TYPE) {
            if (status == Status.LOGIN) {
                // during LOGIN errors are not encrypted
                handshake.handle((ErrorPacket) packet.toPacket());
                return;
            }

            ErrorPacket pck = (ErrorPacket) packet.toPacket(pass);
            // TODO

            return;
        }

        if (status != Status.CONNECTED) return;

        if (packet.getTypeId() == DataPacket.TYPE) {
            // TODO

            return;
        }

        throw new NotImplementedError("Unknown packet type: " + packet.getTypeId());
    }

    private synchronized void clearTimedOutResponses() {
        for (CompiledPacket packet : Set.copyOf(this.responses.keySet())) {
            if (System.currentTimeMillis() < packet.getDeadline()) continue;

            CompletableFuture<Packet> future = this.responses.get(packet);
            future.completeExceptionally(new TimeoutException("Response deadline exceeded"));
        }
    }

    /* - - - */

    public NetworkAdapter getAdapter() {
        return adapter;
    }

    public NestedLogger getLogger() {
        return logger;
    }

    public void setPass(@NotNull String pass) {
        this.pass = pass;
    }
}
