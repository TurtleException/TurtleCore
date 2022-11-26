package de.turtle_exception.server.data;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import de.turtle_exception.client.api.entities.Turtle;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;
import java.util.logging.Level;

public class SQLProvider extends DatabaseProvider {
    private final String host;
    private final int    port;
    private final String database;
    private final String user;
    private final String pass;

    private Connection connection;

    public SQLProvider(@NotNull String host, int port, @NotNull String database, @NotNull String user, @NotNull String pass) {
        this.host = host;
        this.port = port;
        this.database = database;
        this.user = user;
        this.pass = pass;
    }

    public SQLProvider(@NotNull Properties properties) {
        this(
                properties.getProperty("sql.host"),
                Integer.parseInt(properties.getProperty("sql.port")),
                properties.getProperty("sql.database"),
                properties.getProperty("sql.user"),
                properties.getProperty("sql.pass")
        );
    }

    @Override
    protected void onStart() throws Exception {
        this.logger.log(Level.FINE, "Establishing connection to SQL database.");

        String url = "jdbc:mysql://" + host + ":" + port + "/" + database;
        this.connection = DriverManager.getConnection(url, user, pass);

        this.logger.log(Level.INFO, "SQL database connected.");
    }

    /* - - - */

    @Override
    protected boolean doDelete(Class<? extends Turtle> type, long id) {
        // TODO
        return false;
    }

    @Override
    protected JsonObject doGet(Class<? extends Turtle> type, long id) {
        // TODO
        return null;
    }

    @Override
    protected JsonArray doGet(Class<? extends Turtle> type) {
        // TODO
        return null;
    }

    @Override
    protected JsonObject doPut(Class<? extends Turtle> type, JsonObject content) {
        // TODO
        return null;
    }

    @Override
    protected JsonObject doPatch(Class<? extends Turtle> type, JsonObject content, long id) {
        // TODO
        return null;
    }

    @Override
    protected JsonObject doPatchEntry(Class<? extends Turtle> type, long id, String key, Object obj, boolean add) {
        // TODO
        return null;
    }
}
