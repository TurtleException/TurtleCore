package de.turtle_exception.core.client.api;

import de.turtle_exception.core.client.internal.TurtleClientImpl;
import de.turtle_exception.core.core.util.Checks;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * The TurtleClientBuilder is used to instantiate the {@link TurtleClient} interface, while avoiding an explicit
 * interaction with the {@code client.internal} package.
 * <p>A TurtleClientBuilder can be re-used to instantiate multiple instances of a {@link TurtleClient}. This might be
 * useful to connect to the server with multiple users to clearly distinct between different features of an application.
 */
public class TurtleClientBuilder {
    private static final AtomicInteger increment = new AtomicInteger(0);

    private @Nullable String host;
    private @Nullable Integer port;

    private @Nullable String login;
    private @Nullable String pass;

    public TurtleClientBuilder() { }

    public @NotNull TurtleClient build() throws IllegalArgumentException, IOException, LoginException {
        try {
            Checks.nonNull(host , "Host" );
            Checks.nonNull(port , "Port" );
            Checks.nonNull(login, "Login");
            Checks.nonNull(pass , "Pass" );
        } catch (NullPointerException e) {
            throw new IllegalArgumentException(e);
        }

        return new TurtleClientImpl(String.valueOf(increment.getAndIncrement()), host, port, login, pass);
    }

    /* - - - */

    public TurtleClientBuilder setHost(String host) {
        this.host = host;
        return this;
    }

    public TurtleClientBuilder setPort(Integer port) {
        this.port = port;
        return this;
    }

    public TurtleClientBuilder setLogin(String login) {
        this.login = login;
        return this;
    }

    public TurtleClientBuilder setPass(String pass) {
        this.pass = pass;
        return this;
    }

    /* - - - */

    public @Nullable String getHost() {
        return host;
    }

    public @Nullable Integer getPort() {
        return port;
    }

    public @Nullable String getLogin() {
        return login;
    }

    public @Nullable String getPass() {
        return pass;
    }
}
