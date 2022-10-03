package de.turtle_exception.core.server.data;

import de.turtle_exception.core.netcore.util.Checks;
import de.turtle_exception.core.server.TurtleServer;
import de.turtle_exception.core.server.data.filesystem.FilesystemService;
import de.turtle_exception.core.server.data.sql.SQLService;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

public class DataServiceProvider {
    private final TurtleServer server;

    public DataServiceProvider(@NotNull TurtleServer server) {
        this.server = server;
    }

    public @NotNull DataService get() throws NullPointerException, IllegalArgumentException, IOException, SQLException {
        String type = server.getConfig().getProperty("data_service.type");

        return switch (type.toLowerCase()) {
            case "filesystem" -> this.getFilesystem();
            case "sql"        -> this.getSQL();
            default -> throw new IllegalArgumentException("Unknown DataService: " + type);
        };
    }

    private @NotNull FilesystemService getFilesystem() throws IOException {
        String path = server.getConfig().getProperty("data_service.path");
        File   file = path.startsWith("./")
                // relative path
                ? new File(TurtleServer.DIR, path.substring(2))
                // absolute path
                : new File(path);

        return new FilesystemService(file);
    }

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
