package de.turtle_exception.server.data;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import de.turtle_exception.client.api.entities.Turtle;
import de.turtle_exception.client.internal.data.ResourceUtil;
import de.turtle_exception.client.internal.data.annotations.*;
import de.turtle_exception.client.internal.data.annotations.Types;
import de.turtle_exception.client.internal.util.AnnotationUtil;
import de.turtle_exception.client.internal.util.StringUtil;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;

// TODO: simplify (reduce duplicate code)
// TODO: sanitize statements!!!
// TODO: reverse applied changes if an operation fails
public class SQLProvider extends DatabaseProvider {
    private final String host;
    private final int    port;
    private final String database;
    private final String user;
    private final String pass;

    /** Set of resources that already are confirmed to have tables. */
    private final HashSet<Class<? extends Turtle>> safeTypes = new HashSet<>();

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
    protected boolean doDelete(Class<? extends Turtle> type, long id) throws SQLException {
        this.checkTables(type);

        Resource resourceAnnotation = ResourceUtil.getResourceAnnotation(type);
        String table = resourceAnnotation.path();

        List<Key> keys = ResourceUtil.getKeyAnnotations(type);

        // handle references
        for (Key key : keys) {
            if (key.relation() == Relation.ONE_TO_ONE) continue;

            Relational relAnnotation = ResourceUtil.getRelationalAnnotation(type, key.name());

            if (relAnnotation == null)
                throw new IllegalArgumentException("Illegal key: " + key);

            String rTable = relAnnotation.table();
            String rName1 = relAnnotation.self();

            try (Statement statement = connection.createStatement()) {
                statement.executeUpdate("DELETE FROM `" + rTable + "` WHERE `" + rName1 + "` = '" + id + "';");
            }
        }

        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate("DELETE FROM `" + table + "` WHERE `" + Keys.Turtle.ID + "` = '" + id + "';");
        }

        return true;
    }

    @Override
    protected JsonObject doGet(Class<? extends Turtle> type, long id) throws SQLException {
        this.checkTables(type);

        Resource resourceAnnotation = ResourceUtil.getResourceAnnotation(type);
        String table = resourceAnnotation.path();

        List<Key> keys = ResourceUtil.getKeyAnnotations(type);

        JsonObject json = new JsonObject();

        try (Statement statement = connection.createStatement()) {
            ResultSet result = statement.executeQuery("SELECT * FROM `" + table + "` WHERE `" + Keys.Turtle.ID + "` = '" + id + "';");

            if (!result.next())
                throw new NullPointerException("Entry does not exist!");

            for (Key key : keys) {
                if (key.relation() != Relation.ONE_TO_ONE) continue;

                Object val = result.getObject(key.name());
                ResourceUtil.addValue(json, key.name(), val);
            }
        }

        // handle references
        for (Key key : keys) {
            if (key.relation() == Relation.ONE_TO_ONE) continue;

            Relational relAnnotation = ResourceUtil.getRelationalAnnotation(type, key.name());

            if (relAnnotation == null)
                throw new IllegalArgumentException("Illegal key: " + key);

            JsonArray arr = new JsonArray();

            String rTable = relAnnotation.table();
            String rName1 = relAnnotation.self();
            String rName2 = relAnnotation.foreign();

            try (Statement statement = connection.createStatement()) {
                ResultSet result = statement.executeQuery("SELECT * FROM `" + rTable + "` WHERE `" + rName1 + "` = '" + id + "';");

                while (result.next())
                    ResourceUtil.addValue(arr, result.getObject(rName2));
            }

            json.add(key.name(), arr);
        }

        return json;
    }

    @Override
    protected JsonArray doGet(Class<? extends Turtle> type) throws SQLException {
        this.checkTables(type);

        Resource resourceAnnotation = ResourceUtil.getResourceAnnotation(type);
        String table = resourceAnnotation.path();

        JsonArray arr = new JsonArray();

        try (Statement statement = connection.createStatement()) {
            ResultSet result = statement.executeQuery("SELECT `" + Keys.Turtle.ID + "` FROM " + table + ";");

            while (result.next())
                arr.add(this.doGet(type, result.getLong(Keys.Turtle.ID)));
        }

        return arr;
    }

    @Override
    protected JsonObject doPut(Class<? extends Turtle> type, JsonObject content) throws SQLException {
        this.checkTables(type);

        Resource resourceAnnotation = ResourceUtil.getResourceAnnotation(type);
        String table = resourceAnnotation.path();

        long id = content.get(Keys.Turtle.ID).getAsLong();

        List<Key> keys = ResourceUtil.getKeyAnnotations(type);

        List<String> keyStrings = keys.stream()
                .filter(key -> key.relation() == Relation.ONE_TO_ONE)
                .map(key1 -> "`" + key1.name() + "`")
                .toList();
        List<Object> valStrings = new ArrayList<>();

        for (String keyString : keyStrings)
            valStrings.add("'" + ResourceUtil.getValue((JsonPrimitive) content.get(keyString)) + "'");

        String sqlKeys = StringUtil.join(", ", keyStrings);
        String sqlVals = StringUtil.join(", ", valStrings);

        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate("INSERT INTO `" + table + "` (" + sqlKeys + ") VALUES (" + sqlVals + ");");
        }


        List<Key> refKeys = keys.stream().filter(key -> key.relation() != Relation.ONE_TO_ONE).toList();

        for (Key refKey : refKeys) {
            Relational relAnnotation = ResourceUtil.getRelationalAnnotation(type, refKey.name());

            if (relAnnotation == null)
                throw new IllegalArgumentException("Illegal key: " + refKey);

            JsonArray arr = content.getAsJsonArray(refKey.name());

            for (JsonElement entry : arr) {
                Object entryObj = ResourceUtil.getValue((JsonPrimitive) entry);

                String refSqlKeys = relAnnotation.self() + "`, `" + relAnnotation.foreign();
                String refSqlVals = id + "', '" + entryObj;

                try (Statement statement = connection.createStatement()) {
                    statement.executeUpdate("INSERT INTO `" + relAnnotation.table() + "` (`" + refSqlKeys + "`) VALUES ('" + refSqlVals + "');");
                }
            }
        }

        return null;
    }

    @Override
    protected JsonObject doPatch(Class<? extends Turtle> type, JsonObject content, long id) throws SQLException {
        this.checkTables(type);

        Resource resourceAnnotation = ResourceUtil.getResourceAnnotation(type);
        String table = resourceAnnotation.path();

        // note: this will only work with ONE_TO_ONE relations

        List<String> patches = new ArrayList<>();

        for (String key : content.keySet()) {
            Object val = ResourceUtil.getValue((JsonPrimitive) content.get(key));
            patches.add("`" + key + "` = '" + val + "'");
        }

        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate("UPDATE `" + table + "` SET " + StringUtil.join(", ", patches) + " WHERE `" + Keys.Turtle.ID + "` = '" + id + "';");
        }

        return null;
    }

    @Override
    protected JsonObject doPatchEntry(Class<? extends Turtle> type, long id, String key, Object obj, boolean add) throws SQLException {
        this.checkTables(type);

        Relational relAnnotation = ResourceUtil.getRelationalAnnotation(type, key);

        if (relAnnotation == null)
            throw new IllegalArgumentException("Illegal key: " + key);

        String table = relAnnotation.table();

        try (Statement statement = connection.createStatement()) {
            if (add)
                statement.executeUpdate("INSERT INTO `" + table + "` (" + relAnnotation.self() + ", " + relAnnotation.foreign() + ") VALUES (" + id + ", " + obj + ");");
            else
                statement.executeUpdate("DELETE FROM `" + table + "` WHERE `" + relAnnotation.self() + "` = '" + id + "' AND `" + relAnnotation.foreign() + "` = '" + obj + "';");
        }

        return null;
    }

    /* - - - */

    private void checkTables(@NotNull Class<? extends Turtle> type) throws SQLException {
        if (safeTypes.contains(type)) return;
        this.createResourceTable(type);

        List<Key> keys = ResourceUtil.getKeyAnnotations(type);
        for (Key key : keys) {
            if (key.relation() != Relation.ONE_TO_ONE) continue;
            this.createReferenceTable(type, key);
        }
    }

    private void createResourceTable(@NotNull Class<? extends Turtle> type) throws SQLException {
        Resource resourceAnnotation = ResourceUtil.getResourceAnnotation(type);

        String table = resourceAnnotation.path();
        List<String> keys = new ArrayList<>();

        for (Method method : type.getMethods()) {
            Key atKey = AnnotationUtil.getAnnotation(method, Key.class);

            // value should be ignored
            if (atKey == null) continue;
            if (atKey.relation() != Relation.ONE_TO_ONE) continue;

            keys.add("`" + atKey.name() +  "` " + atKey.sqlType());
        }

        keys.add("PRIMARY KEY (`" + Keys.Turtle.ID + "`)");

        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS `" + table + "` (" + StringUtil.join(", ", keys) + ");");
        }
    }

    private void createReferenceTable(@NotNull Class<? extends Turtle> resource, @NotNull Key key) throws SQLException {
        Relational relAnnotation = ResourceUtil.getRelationalAnnotation(resource, key.name());

        if (relAnnotation == null)
            throw new IllegalArgumentException("Illegal key: " + key);

        String table = relAnnotation.table();
        List<String> keys = new ArrayList<>();

        String foreignType = key.sqlType();

        if (foreignType.equalsIgnoreCase("TURTLE"))
            foreignType = Types.Turtle.ID;

        keys.add("`" + relAnnotation.self() + "` " + Types.Turtle.ID);
        keys.add("`" + relAnnotation.foreign() + "` " + foreignType);

        if (key.relation() == Relation.ONE_TO_MANY)
            keys.add("PRIMARY KEY (`" + relAnnotation.foreign() + "`)");
        if (key.relation() == Relation.MANY_TO_MANY)
            keys.add("PRIMARY KEY (`" + relAnnotation.self() + "`, `" + relAnnotation.foreign() + "`)");
        if (key.relation() == Relation.MANY_TO_ONE)
            keys.add("PRIMARY KEY (`" + relAnnotation.self() + "`)");

        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS `" + table + "` (" + StringUtil.join(", ", keys) + ");");
        }
    }
}
