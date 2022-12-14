package de.turtle_exception.client.api;

import de.turtle_exception.client.TurtleException;
import de.turtle_exception.client.api.event.EventListener;
import de.turtle_exception.client.internal.NetworkAdapter;
import de.turtle_exception.client.internal.Provider;
import de.turtle_exception.client.internal.ProviderException;
import de.turtle_exception.client.internal.TurtleClientImpl;
import de.turtle_exception.client.internal.net.NetClient;
import de.turtle_exception.client.internal.net.NetworkProvider;
import de.turtle_exception.client.internal.util.Checks;
import net.dv8tion.jda.api.JDA;
import org.bukkit.Server;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

/**
 * The TurtleClientBuilder is used to instantiate the {@link TurtleClient} interface, while avoiding an explicit
 * interaction with the {@code client.internal} package.
 * <p> A TurtleClientBuilder can be re-used to instantiate multiple instances of a {@link TurtleClient}. This might be
 * useful to connect to the server with multiple logins to clearly distinct between different features of an application.
 */
@SuppressWarnings("unused")
public class TurtleClientBuilder {
    private static final AtomicInteger increment = new AtomicInteger(0);

    private @Nullable NetworkAdapter networkAdapter;
    private @Nullable Provider provider;

    private final ArrayList<EventListener> listeners = new ArrayList<>();

    private @Nullable Logger logger;

    private boolean autoFillCache = true;

    /* THIRD PARTY SERVICES */
    private @Nullable Server spigot;
    private @Nullable JDA    jda;

    public TurtleClientBuilder() { }

    public @NotNull TurtleClient build() throws IllegalArgumentException, TurtleException {
        try {
            Checks.nonNull(networkAdapter, "NetworkAdapter" );
            Checks.nonNull(provider, "Provider");
        } catch (NullPointerException e) {
            throw new IllegalArgumentException(e);
        }

        String name   = String.valueOf(increment.getAndIncrement());
        Logger logger = this.logger != null ? this.logger : Logger.getLogger("CLIENT#" + name);

        TurtleClientImpl client;
        try {
            client = new TurtleClientImpl(name, logger, networkAdapter, provider, autoFillCache);
        } catch (ProviderException | IOException | TimeoutException | LoginException e) {
            throw new TurtleException(e);
        }

        for (EventListener listener : listeners)
            client.getEventManager().register(listener);

        if (spigot != null)
            client.setSpigotServer(spigot);

        if (jda != null)
            client.setJDA(jda);

        return client;
    }

    public static @NotNull TurtleClientBuilder createDefault(@NotNull String host, int port, @NotNull String login, @NotNull String pass) {
        return new TurtleClientBuilder()
                .setNetworkAdapter(new NetClient(host, port, login, pass))
                .setProvider(new NetworkProvider(4))
                .setAutoFillCache(true);
    }

    /* - - - */

    public TurtleClientBuilder setNetworkAdapter(NetworkAdapter networkAdapter) {
        this.networkAdapter = networkAdapter;
        return this;
    }

    public TurtleClientBuilder setProvider(Provider provider) {
        this.provider = provider;
        return this;
    }

    public TurtleClientBuilder addListeners(EventListener... listeners) {
        if (listeners == null) return this;
        this.listeners.addAll(Arrays.asList(listeners));
        return this;
    }

    public TurtleClientBuilder removeListeners(EventListener... listeners) {
        if (listeners == null) return this;
        this.listeners.removeAll(Arrays.asList(listeners));
        return this;
    }

    public TurtleClientBuilder setLogger(Logger logger) {
        this.logger = logger;
        return this;
    }

    public TurtleClientBuilder setAutoFillCache(boolean b) {
        this.autoFillCache = b;
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

    public @Nullable Provider getProvider() {
        return provider;
    }

    public @Nullable Logger getLogger() {
        return logger;
    }

    public boolean getAutoFillCache() {
        return autoFillCache;
    }

    public @Nullable Server getSpigot() {
        return spigot;
    }

    public @Nullable JDA getJda() {
        return jda;
    }
}
