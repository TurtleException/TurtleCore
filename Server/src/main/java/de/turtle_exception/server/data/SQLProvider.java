package de.turtle_exception.server.data;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import de.turtle_exception.client.api.entities.Turtle;
import de.turtle_exception.client.internal.ActionImpl;
import de.turtle_exception.client.internal.Provider;
import org.jetbrains.annotations.NotNull;

import java.lang.annotation.AnnotationFormatError;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.logging.Level;

public class SQLProvider extends Provider {
    private final String host;
    private final int    port;
    private final String database;
    private final String user;
    private final String pass;

    private Connection connection;

    protected SQLProvider(@NotNull String host, int port, @NotNull String database, @NotNull String user, @NotNull String pass) {
        super(1);

        this.host = host;
        this.port = port;
        this.database = database;
        this.user = user;
        this.pass = pass;
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
    public @NotNull <T extends Turtle> ActionImpl<Boolean> delete(@NotNull Class<T> type, long id) throws AnnotationFormatError {
        // TODO
        return null;
    }

    @Override
    public @NotNull <T extends Turtle> ActionImpl<JsonObject> get(@NotNull Class<T> type, long id) throws AnnotationFormatError {
        // TODO
        return null;
    }

    @Override
    public @NotNull <T extends Turtle> ActionImpl<JsonArray> get(@NotNull Class<T> type) throws AnnotationFormatError {
        // TODO
        return null;
    }

    @Override
    public @NotNull <T extends Turtle> ActionImpl<JsonObject> put(@NotNull Class<T> type, @NotNull JsonObject content) throws AnnotationFormatError {
        // TODO
        return null;
    }

    @Override
    public @NotNull <T extends Turtle> ActionImpl<JsonObject> patch(@NotNull Class<T> type, @NotNull JsonObject content, long id) throws AnnotationFormatError {
        // TODO
        return null;
    }

    @Override
    public @NotNull <T extends Turtle> ActionImpl<JsonObject> patchEntryAdd(@NotNull Class<T> type, long id, @NotNull String key, @NotNull Object obj) {
        // TODO
        return null;
    }

    @Override
    public @NotNull <T extends Turtle> ActionImpl<JsonObject> patchEntryDel(@NotNull Class<T> type, long id, @NotNull String key, @NotNull Object obj) {
        // TODO
        return null;
    }
}
