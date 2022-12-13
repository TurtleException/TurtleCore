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
import java.text.MessageFormat;
import java.util.*;
import java.util.logging.Level;

// TODO: simplify (reduce duplicate code)
// TODO: reverse applied changes if an operation fails
public class SQLProvider extends DatabaseProvider {
    private static final String STMT_DELETE_RESOURCE  = "DELETE FROM `?` WHERE `?` = '?';";
    private static final String STMT_DELETE_REFERENCE = "DELETE FROM `?` WHERE `" + Keys.Turtle.ID + "` = '?';";
    private static final String STMT_GET_RESOURCE  = "SELECT * FROM `?` WHERE `" + Keys.Turtle.ID + "` = '?';";
    private static final String STMT_GET_REFERENCE = "SELECT * FROM `?` WHERE `?` = '?';";
    private static final String STMT_GET_ALL = "SELECT `" + Keys.Turtle.ID + "` FROM `?`;";
    private static final String TEMPLATE_INSERT = "INSERT INTO `?` ({0}) VALUES ({1});";
    private static final String TEMPLATE_UPDATE = "UPDATE `?` SET {0} WHERE `" + Keys.Turtle.ID + "` = '?';";
    private static final String STMT_DELETE_ENTRY = "DELETE FROM `?` WHERE `?` = '?' AND `?` = '?';";

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

            try (PreparedStatement statement = connection.prepareStatement(STMT_DELETE_REFERENCE)) {
                statement.setString(1, rTable);
                statement.setString(2, rName1);
                statement.setLong(3, id);
                statement.executeUpdate();
            }
        }

        try (PreparedStatement statement = connection.prepareStatement(STMT_DELETE_RESOURCE)) {
            statement.setString(1, table);
            statement.setLong(2, id);
            statement.executeUpdate();
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

        try (PreparedStatement statement = connection.prepareStatement(STMT_GET_RESOURCE)) {
            statement.setString(1, table);
            statement.setLong(2, id);

            ResultSet result = statement.executeQuery();

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

            try (PreparedStatement statement = connection.prepareStatement(STMT_GET_REFERENCE)) {
                statement.setString(1, rTable);
                statement.setString(2, rName1);
                statement.setLong(3, id);

                ResultSet result = statement.executeQuery();

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

        try (PreparedStatement statement = connection.prepareStatement(STMT_GET_ALL)) {
            statement.setString(1, table);

            ResultSet result = statement.executeQuery();

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

        String stmt = MessageFormat.format(TEMPLATE_INSERT, sqlKeys, sqlVals);

        try (PreparedStatement statement = connection.prepareStatement(stmt)) {
            statement.setString(1, table);
            statement.executeUpdate();
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

                String refStmt = MessageFormat.format(TEMPLATE_INSERT, refSqlKeys, refSqlVals);

                try (PreparedStatement statement = connection.prepareStatement(refStmt)) {
                    statement.setString(1, relAnnotation.table());
                    statement.executeUpdate();
                }
            }
        }

        return this.doGet(type, id);
    }

    @Override
    protected JsonObject doPatch(Class<? extends Turtle> type, JsonObject content, long id) throws SQLException {
        this.checkTables(type);

        Resource resourceAnnotation = ResourceUtil.getResourceAnnotation(type);
        String table = resourceAnnotation.path();

        // note: this will only work with ONE_TO_ONE relations

        Set<String> keys = content.keySet();
        String stmt = MessageFormat.format(TEMPLATE_UPDATE, StringUtil.repeat("`?` = '?'", ", ", keys.size()));

        try (PreparedStatement statement = connection.prepareStatement(stmt)) {
            statement.setString(1, table);

            int i = 2;
            for (String key : keys) {
                Object val = ResourceUtil.getValue((JsonPrimitive) content.get(key));

                statement.setString(i++, key);
                statement.setObject(i++, val);
            }

            statement.setLong(i, id);
            statement.executeUpdate();
        }

        return this.doGet(type, id);
    }

    @Override
    protected JsonObject doPatchEntry(Class<? extends Turtle> type, long id, String key, Object obj, boolean add) throws SQLException {
        this.checkTables(type);

        Relational relAnnotation = ResourceUtil.getRelationalAnnotation(type, key);

        if (relAnnotation == null)
            throw new IllegalArgumentException("Illegal key: " + key);

        if (add)
            this.doPatchEntryInsert(relAnnotation, id, obj);
        else
            this.doPatchEntryDelete(relAnnotation, id, obj);

        return this.doGet(type, id);
    }

    private void doPatchEntryInsert(Relational annotation, long id, Object val) throws SQLException {
        String keys = "`" + annotation.self() + ", `" + annotation.foreign() + "`";
        String stmt = MessageFormat.format(TEMPLATE_INSERT, keys, "`?`, `?`");

        try (PreparedStatement statement = connection.prepareStatement(stmt)) {
            statement.setString(1, annotation.table());
            statement.setLong(2, id);
            statement.setObject(3, val);

            statement.executeUpdate();
        }
    }

    private void doPatchEntryDelete(Relational annotation, long id, Object val) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(STMT_DELETE_ENTRY)) {
            statement.setString(1, annotation.table());
            statement.setString(2, annotation.self());
            statement.setLong(3, id);
            statement.setString(4, annotation.foreign());
            statement.setObject(5, val);

            statement.executeUpdate();
        }
    }

    /* - - - */

    /*
     * The following methods use Statements instead of PreparedStatements because they don't need to sanitize their
     * input data, as it is provided by the source-code. No user input can manipulate any parameters below this comment.
     */

    private void checkTables(@NotNull Class<? extends Turtle> type) throws SQLException {
        if (safeTypes.contains(type)) return;
        this.createResourceTable(type);

        List<Key> keys = ResourceUtil.getKeyAnnotations(type);
        for (Key key : keys) {
            if (key.relation() != Relation.ONE_TO_ONE) continue;
            this.createReferenceTable(type, key);
        }

        safeTypes.add(type);
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

            NotNull atNotNull = AnnotationUtil.getAnnotation(method, NotNull.class);
            String nullSuffix = atNotNull != null ? " NOT NULL" : "";

            keys.add("`" + atKey.name() +  "` " + atKey.sqlType() + nullSuffix);
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

        keys.add("`" + relAnnotation.self() + "` " + Types.Turtle.ID + " NOT NULL");
        keys.add("`" + relAnnotation.foreign() + "` " + foreignType + " NOT NULL");

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
