package de.turtle_exception.server.net;

import com.google.common.collect.Sets;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import de.turtle_exception.client.api.entities.Turtle;
import de.turtle_exception.client.internal.ActionImpl;
import de.turtle_exception.client.internal.NetworkAdapter;
import de.turtle_exception.client.internal.data.Data;
import de.turtle_exception.client.internal.net.Connection;
import de.turtle_exception.client.internal.net.Handshake;
import de.turtle_exception.client.internal.net.NetworkProvider;
import de.turtle_exception.client.internal.net.packets.DataPacket;
import de.turtle_exception.client.internal.net.packets.ErrorPacket;
import de.turtle_exception.client.internal.net.packets.HeartbeatPacket;
import de.turtle_exception.client.internal.net.packets.Packet;
import de.turtle_exception.client.internal.util.Worker;
import de.turtle_exception.client.internal.util.time.TurtleType;
import de.turtle_exception.client.internal.util.time.TurtleUtil;
import de.turtle_exception.server.TurtleServer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.security.auth.login.LoginException;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

public class NetServer extends NetworkAdapter {
    private final TurtleServer server;
    private final int port;

    private final File loginFile;

    private Set<Connection> clients;
    private ServerSocket socket;
    private Worker heartbeats;
    private Worker listener;

    public NetServer(@NotNull TurtleServer server, int port) {
        this.server = server;
        this.port = port;

        this.loginFile = new File(TurtleServer.DIR, "meta" + File.separator + "login.json");
    }

    @Override
    public void onStart() throws IOException {
        this.clients = Sets.newConcurrentHashSet();

        getLogger().log(Level.INFO, "Starting server socket.");
        this.socket = new ServerSocket(port);

        getLogger().log(Level.FINE, "Starting heartbeat-worker.");
        this.heartbeats = new Worker(() -> status == Status.CONNECTED, () -> {
            getLogger().log(Level.FINEST, "Dispatching heartbeats for " + clients.size() + " clients.");
            this.clients.forEach(connection -> {
                connection.send(
                        new HeartbeatPacket(server.getClient().getDefaultTimeoutOutbound(), connection).compile()
                );
            });
        }, 10, TimeUnit.SECONDS);

        getLogger().log(Level.FINE, "Starting listener.");
        this.listener = new Worker(() -> status == Status.CONNECTED, () -> {
            try {
                Socket client = socket.accept();
                getLogger().log(Level.FINE, "New socket connection: " + socket.getInetAddress().toString());

                try {
                    Handshake handshake  = new ServerHandshake(this);
                    Connection connection = new Connection(this, client, handshake, null);

                    this.clients.add(connection);
                    getLogger().log(Level.FINE, "Client added.");
                } catch (IOException | LoginException e) {
                    getLogger().log(Level.WARNING, "Handshake failed.", e);
                }
            } catch (IOException e) {
                getLogger().log(Level.WARNING, "Could not establish requested connection.", e);
            }
        });
    }

    @Override
    public void onShutdown() throws IOException {
        getLogger().log(Level.FINE, "Interrupting listener.");
        this.listener.interrupt();
        this.listener = null;

        getLogger().log(Level.INFO, "Closing " + clients.size() + " connections.");
        for (Connection connection : this.clients)
            connection.stop(true);
        this.clients.clear();

        getLogger().log(Level.FINE, "Interrupting heartbeat-worker.");
        this.heartbeats.interrupt();
        this.heartbeats = null;

        getLogger().log(Level.FINE, "Closing socket.");
        this.socket.close();
    }

    @Override
    public void handleDataRequest(@NotNull DataPacket packet) {
        getLogger().log(Level.FINER, "Incoming data request: " + packet.getId());

        // this would create a loop
        if (getClient().getProvider() instanceof NetworkProvider)
            throw new AssertionError("Unsupported provider");

        try {
            switch (packet.getData().method()) {
                case DELETE -> this.handleDelete(packet);
                case GET -> this.handleGet(packet);
                case PUT -> this.handlePut(packet);
                case PATCH -> this.handlePatch(packet);
                case PATCH_ENTRY_ADD -> this.handlePatchEntry(packet, true);
                case PATCH_ENTRY_DEL -> this.handlePatchEntry(packet, false);
                case UPDATE, REMOVE -> throw new UnsupportedOperationException();
                default -> throw new AssertionError();
            }
        } catch (Exception e) {
            respond(packet, "Internal error", e);
        }
    }

    /* - - - */

    private void handleDelete(@NotNull DataPacket packet) {
        // TODO: should the data type be checked?
        long id = packet.getData().id();
        Turtle turtle = getClientImpl().getTurtleById(id);

        getLogger().log(Level.FINER, "DELETE request for turtle " + id);

        if (turtle == null) {
            respond(packet, "Turtle " + id + " does not exist", null);
            return;
        }

        turtle.delete().queue(result -> {
            getClientImpl().removeCache(turtle.getClass(), id);
            if (result)
                notifyClients(packet, Data.buildRemove(turtle.getClass(), id));
            else
                respond(packet, "Internal error: Could not delete turtle", null);
        }, throwable -> {
            respond(packet, "Internal error", throwable);
        });
    }

    private void handleGet(@NotNull DataPacket packet) {
        if (packet.getData().content().isJsonNull()) {
            this.handleGetAll(packet);
            return;
        }

        // TODO: should the data type be checked?
        long id = packet.getData().id();
        Turtle turtle = getClientImpl().getTurtleById(id);

        getLogger().log(Level.FINER, "GET request for turtle " + id);

        if (turtle == null) {
            respond(packet, "Turtle " + id + " does not exist", null);
            return;
        }

        JsonObject content = getClientImpl().getJsonBuilder().buildJson(turtle);
        respond(packet, Data.buildUpdate(turtle.getClass(), content));
    }

    private void handleGetAll(@NotNull DataPacket packet) {
        Class<? extends Turtle> type = packet.getData().type();

        getLogger().log(Level.FINER, "GET request for type " + type);

        JsonArray content = new JsonArray();
        for (Turtle turtle : getClientImpl().getTurtles()) {
            // filter types
            if (!turtle.getClass().isAssignableFrom(type)) continue;

            JsonObject turtleJson = getClientImpl().getJsonBuilder().buildJson(turtle);
            content.add(turtleJson);
        }

        respond(packet, Data.buildUpdate(type, content));
    }

    private void handlePut(@NotNull DataPacket packet) {
        Class<? extends Turtle> type = packet.getData().type();
        JsonObject content = packet.getData().contentObject();

        getLogger().log(Level.FINER, "PUT request for turtle of type " + type.getSimpleName());

        Turtle turtle = getClientImpl().updateTurtle(type, content);

        // TODO: check for used discord / minecraft / ...

        notifyClients(packet, Data.buildUpdate(type, getClientImpl().getJsonBuilder().buildJson(turtle)));
    }

    private void handlePatch(@NotNull DataPacket packet) {
        Class<? extends Turtle> type = packet.getData().type();
        long id = packet.getData().id();

        getLogger().log(Level.FINER, "PATCH request for turtle " + id);

        // TODO: should the data type be checked?
        Turtle turtle = getClientImpl().getTurtleById(id);
        if (turtle == null) {
            respond(packet, "Turtle " + id + " does not exist", null);
            return;
        }

        getClient().getProvider().patch(type, packet.getData().contentObject(), turtle.getId()).queue(result -> {
            Turtle resTurtle = getClientImpl().updateTurtle(turtle.getClass(), result);
            notifyClients(packet, Data.buildUpdate(resTurtle.getClass(), getClientImpl().getJsonBuilder().buildJson(resTurtle)));
        }, throwable -> {
            respond(packet, "Internal error", throwable);
        });
    }

    private void handlePatchEntry(@NotNull DataPacket packet, boolean add) {
        Class<? extends Turtle> type = packet.getData().type();
        long id = packet.getData().id();

        getLogger().log(Level.FINER, (add ? "PATCH_ENTRY_ADD" : "PATCH_ENTRY_DEL") + " request for turtle " + id);

        String      key = packet.getData().contentObject().get("key").getAsString();
        // we don't need to parse this to an Object because it will be converted back to JSON later
        JsonElement val = packet.getData().contentObject().get("val");

        ActionImpl<JsonObject> action = add
                ? getClient().getProvider().patchEntryAdd(type, id, key, val)
                : getClient().getProvider().patchEntryDel(type, id, key, val);

        action.queue(result -> {
            Turtle resTurtle = getClientImpl().updateTurtle(type, result);
            notifyClients(packet, Data.buildUpdate(resTurtle.getClass(), getClientImpl().getJsonBuilder().buildJson(resTurtle)));
        }, throwable -> {
            respond(packet, "Internal error", throwable);
        });
    }

    /* - HELPER METHODS - */

    private void respond(@NotNull Packet packet, @NotNull Data data) {
        sendData(packet.getConnection(), data, packet.getResponseCode());
    }

    private void respond(@NotNull Packet packet, @NotNull String error, @Nullable Throwable t) {
        packet.getConnection().send(
                new ErrorPacket(defaultDeadline(), packet.getConnection(), packet.getResponseCode(), error, t), true
        );
    }

    private void sendData(@NotNull Connection connection, @NotNull Data data, @Nullable Long responseCode) {
        long resp = responseCode != null ? responseCode : TurtleUtil.newId(TurtleType.RESPONSE_CODE);
        connection.send(
                new DataPacket(defaultDeadline(), connection, resp, data), true
        );
    }

    private void notifyClients(@NotNull Packet packet, @NotNull Data data) {
        for (Connection client : clients) {
            if (packet.getConnection().equals(client))
                respond(packet, data);
            else
                sendData(client, data, null);
        }
    }

    private long defaultDeadline() {
        return System.currentTimeMillis() + getClient().getDefaultTimeoutOutbound();
    }

    /* - - - */

    @SuppressWarnings("ResultOfMethodCallIgnored")
    synchronized @Nullable String checkLogin(@NotNull String login) throws LoginException {
        try {
            loginFile.getParentFile().mkdir();
            loginFile.createNewFile();

            JsonObject json = new Gson().fromJson(new FileReader(loginFile), JsonObject.class);

            String pass = json.get(login).getAsString();

            if (pass == null)
                throw new LoginException("Unknown login or pass");

            getLogger().log(Level.FINE, "Permitted check for login \"" + login + "\"");
            return pass;
        } catch (IOException e) {
            getLogger().log(Level.WARNING, "Internal IO error for login request \"" + login + "\"", e);
            throw new LoginException("Internal IO error");
        } catch (ClassCastException | IllegalStateException e) {
            getLogger().log(Level.FINE, "JSON error for login request \"" + login + "\"", e);
            return null;
        } catch (Throwable t) {
            getLogger().log(Level.WARNING, "Unknown internal error for login request \"" + login + "\"", t);
            throw new LoginException("Unknown internal error");
        }
    }
}
