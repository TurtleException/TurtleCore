package de.turtle_exception.client.api;

import de.turtle_exception.client.internal.TurtleClientImpl;
import de.turtle_exception.client.internal.net.NetworkAdapter;
import de.turtle_exception.client.internal.util.Checks;
import net.dv8tion.jda.api.JDA;
import org.bukkit.Server;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

/**
 * The TurtleClientBuilder is used to instantiate the {@link TurtleClient} interface, while avoiding an explicit
 * interaction with the {@code client.internal} package.
 * <p>A TurtleClientBuilder can be re-used to instantiate multiple instances of a {@link TurtleClient}. This might be
 * useful to connect to the server with multiple users to clearly distinct between different features of an application.
 */
@SuppressWarnings("unused")
public class TurtleClientBuilder {
    private static final AtomicInteger increment = new AtomicInteger(0);

    private @Nullable NetworkAdapter networkAdapter;

    private @Nullable Logger logger;

    /* THIRD PARTY SERVICES */
    private @Nullable Server spigot;
    private @Nullable JDA    jda;

    public TurtleClientBuilder() { }

    public @NotNull TurtleClient build() throws IllegalArgumentException, IOException, LoginException {
        try {
            Checks.nonNull(networkAdapter, "NetworkAdapter" );
        } catch (NullPointerException e) {
            throw new IllegalArgumentException(e);
        }

        String name   = String.valueOf(increment.getAndIncrement());
        Logger logger = this.logger != null ? this.logger : Logger.getLogger("CLIENT#" + name);

        TurtleClientImpl client = new TurtleClientImpl(name, logger, networkAdapter);

        if (spigot != null)
            client.setSpigotServer(spigot);

        if (jda != null)
            client.setJDA(jda);

        return client;
    }

    /* - - - */

    public TurtleClientBuilder setNetworkAdapter(NetworkAdapter networkAdapter) {
        this.networkAdapter = networkAdapter;
        return this;
    }

    public TurtleClientBuilder setLogger(Logger logger) {
        this.logger = logger;
        return this;
    }

    public TurtleClientBuilder setSpigot(Server spigot) {
        this.spigot = spigot;
        return this;
    }

    public TurtleClientBuilder setJda(JDA jda) {
        this.jda = jda;
        return this;
    }

    /* - - - */

    public @Nullable NetworkAdapter getNetworkAdapter() {
        return networkAdapter;
    }

    public @Nullable Logger getLogger() {
        return logger;
    }

    public @Nullable Server getSpigot() {
        return spigot;
    }

    public @Nullable JDA getJda() {
        return jda;
    }
}
