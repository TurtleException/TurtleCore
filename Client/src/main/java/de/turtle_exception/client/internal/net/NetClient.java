package de.turtle_exception.client.internal.net;

import de.turtle_exception.client.internal.NetworkAdapter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.net.Socket;
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
    public void onStart() throws IOException, LoginException {
        getLogger().log(Level.FINE, "Establishing connection...");
        this.connection = new Connection(this, new Socket(host, port), new ClientHandshake(getClient().getVersion(), login), pass);
        getLogger().log(Level.FINE, "Connection established. Login successful");
    }

    @Override
    public void onStop() throws IOException {
        this.connection.stop();
    }
}
