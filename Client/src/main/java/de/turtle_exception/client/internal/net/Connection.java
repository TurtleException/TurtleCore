package de.turtle_exception.client.internal.net;

import de.turtle_exception.client.internal.NetworkAdapter;
import de.turtle_exception.client.internal.net.packets.*;
import de.turtle_exception.client.internal.request.ErrorResponse;
import de.turtle_exception.client.internal.request.IResponse;
import de.turtle_exception.client.internal.request.Request;
import de.turtle_exception.client.internal.request.Response;
import de.turtle_exception.client.internal.util.Worker;
import de.turtle_exception.client.internal.util.logging.NestedLogger;
import kotlin.NotImplementedError;
import org.jetbrains.annotations.NotNull;

import javax.security.auth.login.LoginException;
import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;

public class Connection {
    private final NetworkAdapter adapter;
    private final NestedLogger logger;

    private final Socket socket;
    private final DataOutputStream out;
    private final DataInputStream in;

    private final Handshake handshake;
    private String pass;

    private final Worker receiver;

    public enum Status { INIT, LOGIN, CONNECTED, DISCONNECTED }
    volatile Status status;

    private final RequestCallbackPool requestCallbacks;

    public Connection(@NotNull NetworkAdapter adapter, @NotNull Socket socket, @NotNull Handshake handshake, String pass) throws IOException, LoginException, TimeoutException {
        this.status = Status.INIT;

        this.adapter = adapter;
        this.logger = new NestedLogger("CON#" + socket.getInetAddress() + ":" + socket.getPort(), adapter.getLogger());

        this.requestCallbacks = new RequestCallbackPool(adapter.getClient().getDefaultTimeoutOutbound(), logger);

        this.socket = socket;
        this.out = new DataOutputStream(socket.getOutputStream());
        this.in  = new DataInputStream(socket.getInputStream());

        this.pass = pass;

        this.logger.log(Level.INFO, "Initiating Handshake. (" + handshake.getClass().getSimpleName() + ")");

        this.status = Status.LOGIN;
        this.handshake = handshake;
        this.handshake.setConnection(this);
        this.handshake.init();

        this.logger.log(Level.FINE, "Starting Receiver.");
        this.receiver = new Worker(() -> status != Status.DISCONNECTED, () -> {
            if (socket.isClosed())
                this.stop(false);

            try {
                int length = in.readInt();
                if (length > 0) {
                    byte[] msg = new byte[length];
                    in.readFully(msg, 0, length);
                    this.receive(msg);
                }
            } catch (EOFException e) {
                // TODO: could this be fired for a different reason?
                // TODO: logging
                this.stop(false);
            } catch (SocketException e) {
                if (status == Status.DISCONNECTED) {
                    // in.readInt() threw an exception because the socked closed as intended by stop().
                    // Meaning everything is working as intended, and we can safely ignore the exception.
                    return;
                }

                logger.log(Level.WARNING, "Unexpected SocketException", e);
                this.stop(true);
            } catch (IOException e) {
                logger.log(Level.WARNING, "Could not read input", e);
            }
        });

        this.handshake.await(10, TimeUnit.HOURS);
    }

    public boolean stop(boolean notify) {
        // ignore call if the connection is already stopped
        if (status == Status.DISCONNECTED) return true;

        this.logger.log(Level.INFO, "Stopping connection.");

        if (notify) {
            this.logger.log(Level.INFO, "Sending QUIT packet.");
            this.send(new HandshakePacket(Long.MAX_VALUE, this, "QUIT").compile());
        }

        if (this.adapter.getStatus() != NetworkAdapter.Status.DISCONNECTED) {
            this.logger.log(Level.FINE, "Notifying NetworkAdapter.");
            this.adapter.handleQuit(this);
        } else {
            this.logger.log(Level.FINE, "NetworkAdapter is already disconnected.");
        }

        this.status = Status.DISCONNECTED;
        this.logger.log(Level.FINE, "Interrupting Receiver.");
        this.receiver.interrupt();
        try {
            this.logger.log(Level.FINE, "Closing Socket.");
            this.socket.close();
            this.logger.log(Level.INFO, "Socket closed successfully.");
            return true;
        } catch (IOException e) {
            this.logger.log(Level.WARNING, "Could not close connection!", e);
            return false;
        }
    }

    /* - - - */

    public synchronized void send(@NotNull Packet packet, boolean encrypt) {
        final String pckId = packet.getClass().getSimpleName() + ":" + packet.getResponseCode();

        if (encrypt) {
            try {
                this.logger.log(Level.FINER, "Sending " + pckId + " encrypted.");
                this.send(packet.compile(pass));
            } catch (Error e) {
                logger.log(Level.SEVERE, "Encountered an Error when attempting to encrypt a packet", e);
            } catch (Throwable t) {
                logger.log(Level.WARNING, "Encountered an Exception when attempting to encrypt a packet", t);
            }
        } else {
            this.logger.log(Level.FINER, "Sending " + pckId + " as cleartext.");
            this.send(packet.compile());
        }
    }

    private synchronized void send(@NotNull CompiledPacket packet) {
        try {
            byte[] msg = packet.getBytes();

            this.out.writeInt(msg.length);
            this.out.write(msg);
        } catch (Error e) {
            logger.log(Level.SEVERE, "Encountered an Error when attempting to send a packet", e);
        } catch (Throwable t) {
            logger.log(Level.WARNING, "Encountered an Exception when attempting to send a packet", t);
        }
    }

    private void receive(byte[] msg) {
        if (msg == null)    return;
        if (msg.length < 1) return;

        final long deadline = System.currentTimeMillis() + adapter.getClient().getDefaultTimeoutInbound();

        try {
            this.receive(new CompiledPacket(msg, Direction.INBOUND, this, deadline));
        } catch (Error e) {
            logger.log(Level.SEVERE, "Encountered an Error when attempting to receive a packet", e);
        } catch (Throwable t) {
            logger.log(Level.WARNING, "Encountered an Exception when attempting to receive a packet", t);
        }
    }

    /* - - - */

    public @NotNull CompletableFuture<IResponse> send(@NotNull Request request) {
        CompletableFuture<IResponse> future = new CompletableFuture<>();
        this.requestCallbacks.put(request.getPacket().getResponseCode(), future);
        try {
            this.logger.log(Level.FINER, "Attempting to dispatch request " + request.getPacket().getId());
            this.send(request.getPacket().compile(pass));
        } catch (Exception e) {
            this.requestCallbacks.remove(request.getPacket().getResponseCode());
            this.logger.log(Level.FINER, "Dispatch failed: " + request.getPacket().getId());
            return CompletableFuture.failedFuture(new IllegalArgumentException("Encryption error. Please check your pass!", e));
        }
        return future;
    }

    public void receive(@NotNull CompiledPacket packet) throws Exception {
        this.logger.log(Level.FINER, "Receiving packet (type " + packet.getTypeId() + ") with id " + packet.getId());

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
                this.logger.log(Level.INFO, "Received QUIT packet.");
                this.stop(false);
                return;
            }

            if (status != Status.LOGIN)
                throw new IllegalStateException("Unexpected Handshake packet while not in login");

            handshake.handle(pck);
            return;
        }

        CompletableFuture<IResponse> future = requestCallbacks.get(packet.getResponseCode());

        if (packet.getTypeId() == ErrorPacket.TYPE) {
            if (status == Status.LOGIN) {
                // during LOGIN errors are not encrypted
                handshake.handle((ErrorPacket) packet.toPacket());
                return;
            }

            ErrorPacket pck = (ErrorPacket) packet.toPacket(pass);
            if (future != null) {
                future.complete(new ErrorResponse(pck));
            } else {
                logger.log(Level.WARNING, "Dangling error: " + pck.getMessage(), pck.getThrowable());
            }
            return;
        }

        if (status != Status.CONNECTED)
            throw new IllegalStateException("Unable to process packet (type " + packet.getTypeId() + ").");

        if (packet.getTypeId() == DataPacket.TYPE) {
            DataPacket pck = (DataPacket) packet.toPacket(pass);
            if (future != null) {
                future.complete(new Response(pck));
            }
            // handle cache operation
            adapter.handleDataRequest(pck);
            return;
        }

        throw new NotImplementedError("Unknown packet type: " + packet.getTypeId());
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
