package de.turtle_exception.server.data.sql;

import de.turtle_exception.server.data.DataAccessException;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.MessageFormat;

class SQLConnector {
    private final String login;
    private final String pass;

    private final String url;

    private final Object lock = new Object();

    private Connection connection;

    public SQLConnector(String host, int port, String database, String login, String pass) throws SQLException {
        this.login = login;
        this.pass = pass;

        this.url = "jdbc:mysql://" + host + ":" + port + "/" + database;

        this.checkConnection();
    }

    private void checkConnection() throws SQLException {
        if (this.connection.isClosed()) {
            try {
                this.connection = DriverManager.getConnection(this.url, this.login, this.pass);
            } catch (SQLException e) {
                // TODO: handle failed reconnect
            }
        }
    }

    public void executeSilent(@NotNull String sql, Object... args) throws DataAccessException {
        try {
            if (args == null)
                this.executeSilentRaw(sql);
            else
                this.executeSilentRaw(MessageFormat.format(sql, args));
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }

    public @NotNull ResultSet executeQuery(@NotNull String sql, Object... args) throws DataAccessException {
        try {
            if (args == null)
                return this.executeQueryRaw(sql);
            else
                return this.executeQueryRaw(MessageFormat.format(sql, args));
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }

    public void executeSilentRaw(@NotNull String sql) throws SQLException {
        synchronized (lock) {
            this.checkConnection();
            this.connection.createStatement().execute(sql);
        }
    }

    public @NotNull ResultSet executeQueryRaw(@NotNull String sql) throws SQLException {
        synchronized (lock) {
            this.checkConnection();
            return this.connection.createStatement().executeQuery(sql);
        }
    }

    /* - - - */

    public void shutdown() throws SQLException {
        this.connection.close();
    }
}
