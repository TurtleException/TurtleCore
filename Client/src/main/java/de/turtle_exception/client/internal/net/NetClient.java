package de.turtle_exception.client.internal.net;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import de.turtle_exception.client.internal.NetworkAdapter;
import de.turtle_exception.client.internal.net.packets.DataPacket;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;

public class NetClient extends NetworkAdapter {
    private final String host;
    private final int port;

    private final String login;
    private final String pass;

    private Connection connection;

    public NetClient(String host, @Range(from = 0, to = 65535) int port, @NotNull String login, @NotNull String pass) {
        super();

        this.host = host;
        this.port = port;

        this.login = login;
        this.pass  = pass;
    }

    @Override
    public void onStart() throws IOException, LoginException, TimeoutException {
        getLogger().log(Level.FINE, "Establishing connection...");
        this.connection = new Connection(this, new Socket(host, port), new ClientHandshake(getClient().getVersion(), login), pass);
    }

    @Override
    public void onShutdown() throws IOException {
        this.connection.stop(true);
    }

    @Override
    public void handleQuit(@NotNull Connection connection) {
        try {
            this.shutdown();
        } catch (IOException e) {
            getLogger().log(Level.WARNING, "Could not shut down.", e);
        }
    }

    @Override
    public void handleDataRequest(@NotNull DataPacket packet) {
        switch (packet.getData().method()) {
            case DELETE, GET, PUT, PATCH, PATCH_ENTRY_ADD, PATCH_ENTRY_DEL -> throw new UnsupportedOperationException();
            case UPDATE -> this.handleUpdate(packet);
            case REMOVE -> this.handleRemove(packet);
            default -> throw new AssertionError();
        }
    }

    private void handleUpdate(@NotNull DataPacket packet) {
        JsonElement json = packet.getData().content();
        if (json instanceof JsonObject jsonObj)
            getClientImpl().updateTurtle(packet.getData().type(), jsonObj);
        if (json instanceof JsonArray jsonArr)
            for (JsonElement element : jsonArr)
                getClientImpl().updateTurtle(packet.getData().type(), (JsonObject) element);
    }

    private void handleRemove(@NotNull DataPacket packet) {
        getClientImpl().removeCache(packet.getData().type(), packet.getData().id());
    }

    public Connection getConnection() {
        return connection;
    }
}
