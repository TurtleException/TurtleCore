package de.turtle_exception.server.data;

import de.turtle_exception.core.netcore.util.Checks;
import de.turtle_exception.server.TurtleServer;
import de.turtle_exception.server.data.filesystem.FilesystemService;
import de.turtle_exception.server.data.sql.SQLService;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

/**
 * A DataServiceProvider is responsible for determining which implementation of {@link DataService} a
 * {@link TurtleServer} should use and to initialize that instance for it. To determine which implementation is the
 * correct one the server {@link java.util.Properties Properties config} is used.
 * <p> While this could have easily been implemented in {@link TurtleServer#run()} itself this functionality has been
 * moved to its own class for readability purposes.
 */
public class DataServiceProvider {
    private final TurtleServer server;

    /**
     * Constructs a new DataServiceProvider for a provided {@link TurtleServer}.
     * @param server The server this instance should be bound to.
     */
    public DataServiceProvider(@NotNull TurtleServer server) {
        this.server = server;
    }

    /**
     * This method determines which implementation of {@link DataService} should be used and delegates the corresponding
     * instantiation to a private method, which will then return the constructed instance.
     * @return Instance of DataService, determined by the server config.
     *
     * @throws NullPointerException if a critical value has not been set in the config.
     * @throws IllegalArgumentException if a critical value that has been set in the config is illegal or could not be
     *                                  parsed properly.
     * @throws IOException if an instance of {@link FilesystemService} could not be created.
     * @throws SQLException if an instance of {@link SQLException} could not be created.
     *
     * @see FilesystemService
     * @see SQLService
     */
    public @NotNull DataService get() throws NullPointerException, IllegalArgumentException, IOException, SQLException {
        String type = server.getConfig().getProperty("data_service.type");

        return switch (type.toLowerCase()) {
            case "filesystem" -> this.getFilesystem();
            case "sql"        -> this.getSQL();
            default -> throw new IllegalArgumentException("Unknown DataService: " + type);
        };
    }

    /** Provides an instance of {@link FilesystemService} */
    private @NotNull FilesystemService getFilesystem() throws IOException {
        String path = server.getConfig().getProperty("data_service.path");
        File   file = path.startsWith("./")
                // relative path
                ? new File(TurtleServer.DIR, path.substring(2))
                // absolute path
                : new File(path);

        return new FilesystemService(file);
    }

    /** Provides an instance of {@link SQLService} */
    private @NotNull SQLService getSQL() throws NullPointerException, IllegalArgumentException, SQLException {
        String host     = server.getConfig().getProperty("data_service.host");
        String portStr  = server.getConfig().getProperty("data_service.port");
        String database = server.getConfig().getProperty("data_service.database");
        String login    = server.getConfig().getProperty("data_service.login");
        String pass     = server.getConfig().getProperty("data_service.pass");

        Checks.nonNull(host    , "Host"    );
        Checks.nonNull(portStr , "Port"    );
        Checks.nonNull(database, "Database");
        Checks.nonNull(login   , "Login"   );
        Checks.nonNull(pass    , "Pass"    );

        int port;
        try {
            port = Integer.parseInt(portStr);

            if (port < 0 || port > 65535)
                throw new IllegalArgumentException();
        } catch (ClassCastException | IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid port: " + portStr);
        }

        return new SQLService(host, port, database, login, pass);
    }
}
