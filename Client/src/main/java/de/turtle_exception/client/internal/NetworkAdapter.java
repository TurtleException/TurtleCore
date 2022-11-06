package de.turtle_exception.client.internal;

import de.turtle_exception.client.api.TurtleClient;
import de.turtle_exception.client.internal.util.Checks;
import de.turtle_exception.client.internal.util.logging.NestedLogger;
import org.jetbrains.annotations.NotNull;

import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.util.logging.Level;

public abstract class NetworkAdapter {
    private TurtleClientImpl client;
    private NestedLogger logger;

    public enum Status { PRE_INIT, INIT, CONNECTED, DISCONNECTED }
    protected Status status = Status.PRE_INIT;

    protected NetworkAdapter() { }

    public final void start() throws IllegalStateException, NullPointerException, IOException, LoginException {
        if (status != Status.PRE_INIT)
            throw new IllegalStateException("The NetworkAdapter is already running.");
        Checks.nonNull(this.client, "Client");
        this.logger = new NestedLogger("NetworkAdapter", client.getLogger());
        this.status = Status.INIT;
        this.onStart();
    }

    public final void stop() throws IOException {
        logger.log(Level.INFO, "Stopping...");
        this.status = Status.DISCONNECTED;
        this.onStop();
        logger.log(Level.INFO, "Stopped");
    }

    /** Called after the NetworkAdapter has been started. */
    public void onStart() throws IOException, LoginException { }

    /** Called when the NetworkAdapter stops. */
    public void onStop() throws IOException { }

    final void setClient(TurtleClientImpl client) throws IllegalStateException {
        if (status != Status.PRE_INIT)
            throw new IllegalStateException("The client can only be set before initialization");
        this.client = client;
    }

    /* - - -*/

    public final @NotNull TurtleClient getClient() {
        return client;
    }

    public final @NotNull NestedLogger getLogger() {
        return logger;
    }

    public final @NotNull Status getStatus() {
        return status;
    }
}
