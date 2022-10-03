package de.turtle_exception.core.server.data.sql;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import de.turtle_exception.core.server.data.DataAccessException;
import de.turtle_exception.core.server.data.DataService;
import org.jetbrains.annotations.NotNull;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * An implementation of {@link DataService} that uses a MySQL-server as database. All data is written to tables with
 * relational info (e.g. a user being a member of a group) being stored according to SQL-conventions.
 * <p> See all {@code CREATE TABLE} statements in {@link Statements} for more info.
 */
public class SQLService implements DataService {
    private final SQLConnector sqlConnector;

    public SQLService(@NotNull String host, int port, @NotNull String database, @NotNull String login, @NotNull String pass) throws SQLException {
        this.sqlConnector = new SQLConnector(host, port, database, login, pass);

        // create tables (if they don't already exist)
        this.sqlConnector.executeSilentRaw(Statements.CT_CREDENTIALS);
        this.sqlConnector.executeSilentRaw(Statements.CT_GROUPS);
        this.sqlConnector.executeSilentRaw(Statements.CT_USERS);
        this.sqlConnector.executeSilentRaw(Statements.CT_USER_GROUPS);
        this.sqlConnector.executeSilentRaw(Statements.CT_USER_DISCORD);
        this.sqlConnector.executeSilentRaw(Statements.CT_USER_MINECRAFT);
    }

    /* - - - */

    @Override
    public @NotNull String getPass(@NotNull String login) throws DataAccessException {
        ResultSet resultSet = this.sqlConnector.executeQuery(Statements.GET_PASS, login);

        try {
            if (!resultSet.next())
                throw new DataAccessException("Could not parse pass from empty ResultSet.");

            return resultSet.getString("pass");
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }

    @Override
    public @NotNull List<Long> getGroupIds() throws DataAccessException {
        ResultSet resultSet = this.sqlConnector.executeQuery(Statements.GET_GROUP_IDS);

        try {
            ArrayList<Long> list = new ArrayList<>();

            while (resultSet.next())
                list.add(resultSet.getLong(1));

            return List.copyOf(list);
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }

    @Override
    public @NotNull JsonObject getGroup(long id) throws DataAccessException {
        ResultSet resultSet =  this.sqlConnector.executeQuery(Statements.GET_GROUP);

        JsonObject json = new JsonObject();

        try {
            if (!resultSet.next())
                throw new DataAccessException("Could not parse group from empty ResultSet.");

            json.addProperty("id"  , resultSet.getString("id"));
            json.addProperty("name", resultSet.getString("name"));
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }

        return json;
    }

    @Override
    public void setGroup(@NotNull JsonObject group) throws DataAccessException {
        this.sqlConnector.executeSilent(Statements.SET_GROUP,
                group.get("id").getAsString(),
                group.get("name").getAsString()
        );
    }

    @Override
    public void deleteGroup(long id) throws DataAccessException {
        this.sqlConnector.executeSilent(Statements.DEL_GROUP, id);
    }

    @Override
    public @NotNull List<Long> getUserIds() throws DataAccessException {
        ResultSet resultSet = this.sqlConnector.executeQuery(Statements.GET_USER_IDS);

        try {
            ArrayList<Long> list = new ArrayList<>();

            while (resultSet.next())
                list.add(resultSet.getLong(1));

            return List.copyOf(list);
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }

    @Override
    public @NotNull JsonObject getUser(long id) throws DataAccessException {
        ResultSet resultUser = this.sqlConnector.executeQuery(Statements.GET_USER, id);

        JsonObject json = new JsonObject();

        try {
            if (!resultUser.next())
                throw new DataAccessException("Could not parse user from empty ResultSet.");

            json.addProperty("id"  , resultUser.getString("id"));
            json.addProperty("name", resultUser.getString("name"));
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }

        return json;
    }

    @Override
    public void setUser(@NotNull JsonObject user) throws DataAccessException {
        this.sqlConnector.executeSilent(Statements.SET_USER,
                user.get("id").getAsLong(),
                user.get("name").getAsString()
        );
    }

    @Override
    public void deleteUser(long id) throws DataAccessException {
        this.sqlConnector.executeSilent(Statements.DEL_USER, id);
    }

    @Override
    public @NotNull JsonArray getUserGroups(long user) throws DataAccessException {
        ResultSet resultSet = this.sqlConnector.executeQuery(Statements.GET_USER_GROUPS, user);

        try {
            JsonArray groups = new JsonArray();

            while (resultSet.next())
                groups.add(resultSet.getLong("groups"));

            return groups;
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }

    @Override
    public void addUserGroup(long user, long group) throws DataAccessException {
        this.sqlConnector.executeSilent(Statements.ADD_USER_GROUP, user, group);
    }

    @Override
    public void delUserGroup(long user, long group) throws DataAccessException {
        this.sqlConnector.executeSilent(Statements.DEL_USER_GROUP, user, group);
    }

    @Override
    public @NotNull JsonArray getUserDiscord(long user) throws DataAccessException {
        ResultSet resultSet = this.sqlConnector.executeQuery(Statements.GET_USER_DISCORD, user);

        try {
            JsonArray discord = new JsonArray();

            while (resultSet.next())
                discord.add(resultSet.getLong("discord"));

            return discord;
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }

    @Override
    public void addUserDiscord(long user, long discord) throws DataAccessException {
        this.sqlConnector.executeSilent(Statements.ADD_USER_DISCORD, user, discord);
    }

    @Override
    public void delUserDiscord(long user, long discord) throws DataAccessException {
        this.sqlConnector.executeSilent(Statements.DEL_USER_DISCORD, user, discord);
    }

    @Override
    public @NotNull JsonArray getUserMinecraft(long user) throws DataAccessException {
        ResultSet resultSet = this.sqlConnector.executeQuery(Statements.GET_USER_MINECRAFT, user);

        try {
            JsonArray minecraft = new JsonArray();

            while (resultSet.next())
                minecraft.add(resultSet.getString("minecraft"));

            return minecraft;
        } catch (SQLException | IllegalArgumentException e) {
            throw new DataAccessException(e);
        }
    }

    @Override
    public void addUserMinecraft(long user, @NotNull UUID minecraft) throws DataAccessException {
        this.sqlConnector.executeSilent(Statements.ADD_USER_MINECRAFT, user,  minecraft.toString());
    }

    @Override
    public void delUserMinecraft(long user, @NotNull UUID minecraft) throws DataAccessException {
        this.sqlConnector.executeSilent(Statements.DEL_USER_MINECRAFT, user, minecraft.toString());
    }
}
