package de.turtle_exception.server.data.sql;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import de.turtle_exception.server.data.DataAccessException;
import de.turtle_exception.server.data.DataService;
import org.jetbrains.annotations.NotNull;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;

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
        this.sqlConnector.executeSilentRaw(Statements.CT_MEMBERS);
        this.sqlConnector.executeSilentRaw(Statements.CT_USERS);
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
        } catch (Exception e) {
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
        } catch (Exception e) {
            throw new DataAccessException(e);
        }
    }

    @Override
    public @NotNull JsonObject getGroup(long id) throws DataAccessException {
        ResultSet resultSet = this.sqlConnector.executeQuery(Statements.GET_GROUP);

        JsonObject json = new JsonObject();

        try {
            if (!resultSet.next())
                throw new DataAccessException("Could not parse group from empty ResultSet.");

            json.addProperty("id"  , resultSet.getString("id"));
            json.addProperty("name", resultSet.getString("name"));
            json.add("members", this.getGroupMembers(id));
        } catch (Exception e) {
            throw new DataAccessException(e);
        }

        return json;
    }

    @Override
    public void setGroup(@NotNull JsonObject group) throws DataAccessException {
        long id;
        String name;

        try {
            id = group.get("id").getAsLong();
            name = group.get("name").getAsString();
        } catch (Exception e) {
            throw new DataAccessException("Malformed parameters for group object");
        }

        // simpler to use this (all members will also be deleted)
        this.deleteGroup(id);

        this.sqlConnector.executeSilent(Statements.SET_GROUP, id, name);

        JsonArray members = group.getAsJsonArray("members");
        if (members != null) {
            for (JsonElement member : members) {
                try {
                    this.addGroupMember(id, member.getAsLong());
                } catch (Exception ignored) { }
            }
        }
    }

    @Override
    public void deleteGroup(long id) throws DataAccessException {
        JsonObject group = this.getGroup(id);

        this.sqlConnector.executeSilent(Statements.DEL_GROUP, id);

        for (JsonElement member : group.getAsJsonArray("members")) {
            long user = member.getAsLong();
            this.delGroupMember(id, user);
        }
    }

    @Override
    public void modifyGroup(long group, @NotNull Function<JsonObject, JsonObject> function) throws DataAccessException {
        // TODO: lock

        JsonObject json = this.getGroup(group);
        json = function.apply(json);
        this.setGroup(json);
    }

    @Override
    public @NotNull JsonArray getGroupMembers(long group) throws DataAccessException {
        ResultSet resultSet = this.sqlConnector.executeQuery(Statements.GET_MEMBERS, group);

        try {
            JsonArray groups = new JsonArray();

            while (resultSet.next())
                groups.add(resultSet.getLong("user"));

            return groups;
        } catch (Exception e) {
            throw new DataAccessException(e);
        }
    }

    @Override
    public void addGroupMember(long group, long user) throws DataAccessException {
        this.sqlConnector.executeSilent(Statements.MEMBER_JOIN, group, user);
    }

    @Override
    public void delGroupMember(long group, long user) throws DataAccessException {
        this.sqlConnector.executeSilent(Statements.MEMBER_LEAVE, group, user);
    }

    @Override
    public @NotNull List<Long> getUserIds() throws DataAccessException {
        ResultSet resultSet = this.sqlConnector.executeQuery(Statements.GET_USER_IDS);

        try {
            ArrayList<Long> list = new ArrayList<>();

            while (resultSet.next())
                list.add(resultSet.getLong(1));

            return List.copyOf(list);
        } catch (Exception e) {
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
            json.add("discord"  , this.getUserDiscord(id));
            json.add("minecraft", this.getUserMinecraft(id));
        } catch (Exception e) {
            throw new DataAccessException(e);
        }

        return json;
    }

    @Override
    public void setUser(@NotNull JsonObject user) throws DataAccessException {
        long id;
        String name;

        try {
            id = user.get("id").getAsLong();
            name = user.get("name").getAsString();
        } catch (Exception e) {
            throw new DataAccessException("Malformed parameters for user object");
        }

        this.sqlConnector.executeSilent(Statements.SET_USER, id, name);

        JsonArray discord = user.getAsJsonArray("discord");
        if (discord != null) {
            for (JsonElement entry : discord) {
                try {
                    this.addUserDiscord(id, entry.getAsLong());
                } catch (Exception ignored) { }
            }
        }

        JsonArray minecraft = user.getAsJsonArray("minecraft");
        if (minecraft != null) {
            for (JsonElement entry : minecraft) {
                try {
                    this.addUserMinecraft(id, UUID.fromString(entry.getAsString()));
                } catch (Exception ignored) { }
            }
        }
    }

    @Override
    public void deleteUser(long id) throws DataAccessException {
        JsonObject user = this.getGroup(id);

        this.sqlConnector.executeSilent(Statements.DEL_GROUP, id);

        for (JsonElement entry : user.getAsJsonArray("discord")) {
            long discord = entry.getAsLong();
            this.delUserDiscord(id, discord);
        }

        for (JsonElement entry : user.getAsJsonArray("minecraft")) {
            UUID minecraft = UUID.fromString(entry.getAsString());
            this.delUserMinecraft(id, minecraft);
        }
    }

    @Override
    public void modifyUser(long user, @NotNull Function<JsonObject, JsonObject> function) throws DataAccessException {
        // TODO: lock

        JsonObject json = this.getUser(user);
        json = function.apply(json);
        this.setUser(json);
    }

    @Override
    public @NotNull JsonArray getUserDiscord(long user) throws DataAccessException {
        ResultSet resultSet = this.sqlConnector.executeQuery(Statements.GET_USER_DISCORD, user);

        try {
            JsonArray discord = new JsonArray();

            while (resultSet.next())
                discord.add(resultSet.getLong("discord"));

            return discord;
        } catch (Exception e) {
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
        } catch (Exception e) {
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
