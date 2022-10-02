package de.turtle_exception.core.server.data.sql;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import de.turtle_exception.core.server.data.DataAccessException;
import de.turtle_exception.core.server.data.DataService;
import org.jetbrains.annotations.NotNull;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SQLService implements DataService {
    private final SQLConnector sqlConnector;

    public SQLService(@NotNull String host, int port, @NotNull String database, @NotNull String login, @NotNull String pass) throws SQLException {
        this.sqlConnector = new SQLConnector(host, port, database, login, pass);

        this.sqlConnector.executeSilentRaw(Statements.CT_GROUPS);
        this.sqlConnector.executeSilentRaw(Statements.CT_USERS);
        this.sqlConnector.executeSilentRaw(Statements.CT_USER_GROUPS);
        this.sqlConnector.executeSilentRaw(Statements.CT_USER_DISCORD);
        this.sqlConnector.executeSilentRaw(Statements.CT_USER_MINECRAFT);
    }

    /* - - - */

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
    public @NotNull String getGroup(long id) throws DataAccessException {
        ResultSet resultSet =  this.sqlConnector.executeQuery(Statements.GET_GROUP);

        JsonObject json = new JsonObject();

        try {
            json.addProperty("id"  , resultSet.getString("id"));
            json.addProperty("name", resultSet.getString("name"));
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }

        return json.toString();
    }

    @Override
    public void setGroup(@NotNull String group) throws DataAccessException {
        JsonObject json = new Gson().fromJson(group, JsonObject.class);
        
        String sql = MessageFormat.format(Statements.SET_GROUP,
                json.get("id").getAsString(),
                json.get("name").getAsString()
        );

        this.sqlConnector.executeSilent(sql);
    }

    @Override
    public void deleteGroup(long id) throws DataAccessException {
        this.sqlConnector.executeSilent(MessageFormat.format(Statements.DEL_GROUP, id));
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
    public @NotNull String getUser(long id) throws DataAccessException {
        ResultSet resultUser          = this.sqlConnector.executeQuery(Statements.GET_USER          , id);
        ResultSet resultUserGroups    = this.sqlConnector.executeQuery(Statements.GET_USER_GROUP_IDS, id);
        ResultSet resultUserDiscord   = this.sqlConnector.executeQuery(Statements.GET_USER_DISCORD  , id);
        ResultSet resultUserMinecraft = this.sqlConnector.executeQuery(Statements.GET_USER_MINECRAFT, id);

        JsonObject json = new JsonObject();

        try {
            json.addProperty("id"  , resultUser.getString("id"));
            json.addProperty("name", resultUser.getString("name"));

            JsonArray groups = new JsonArray();
            while (resultUserGroups.next())
                groups.add(resultUserGroups.getString("group"));
            json.add("groups", groups);

            JsonArray discord = new JsonArray();
            while (resultUserDiscord.next())
                discord.add(resultUserDiscord.getString("discord"));
            json.add("discord", discord);

            JsonArray minecraft = new JsonArray();
            while (resultUserMinecraft.next())
                minecraft.add(resultUserMinecraft.getString("minecraft"));
            json.add("minecraft", minecraft);
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }

        return json.toString();
    }

    // TODO: should this handle groups, discord & minecraft?
    @Override
    public void setUser(@NotNull String user) throws DataAccessException {
        JsonObject newUser = new Gson().fromJson(user, JsonObject.class);

        this.sqlConnector.executeSilent(Statements.SET_USER,
                newUser.get("id").getAsLong(),
                newUser.get("name").getAsString()
        );
    }

    @Override
    public void deleteUser(long id) throws DataAccessException {
        this.sqlConnector.executeSilent(MessageFormat.format(Statements.DEL_USER, id));

    }

    @Override
    public void userJoinGroup(long user, long group) throws DataAccessException {
        this.sqlConnector.executeSilent(Statements.USER_GROUP_JOIN, user, group);
    }

    @Override
    public void userLeaveGroup(long user, long group) throws DataAccessException {
        this.sqlConnector.executeSilent(Statements.USER_GROUP_LEAVE, user, group);
    }

    @Override
    public @NotNull List<Long> getUserDiscord(long user) throws DataAccessException {
        ResultSet resultSet = this.sqlConnector.executeQuery(Statements.GET_USER_DISCORD, user);

        try {
            List<Long> discord = new ArrayList<>();

            while (resultSet.next())
                discord.add(resultSet.getLong("discord"));

            return List.copyOf(discord);
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }

    @Override
    public @NotNull List<UUID> getUserMinecraft(long user) throws DataAccessException {
        ResultSet resultSet = this.sqlConnector.executeQuery(Statements.GET_USER_MINECRAFT, user);

        try {
            List<UUID> minecraft = new ArrayList<>();

            while (resultSet.next())
                minecraft.add(UUID.fromString(resultSet.getString("minecraft")));

            return List.copyOf(minecraft);
        } catch (SQLException | IllegalArgumentException e) {
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
    public void addUserMinecraft(long user, @NotNull UUID minecraft) throws DataAccessException {
        this.sqlConnector.executeSilent(Statements.ADD_USER_MINECRAFT, user,  minecraft.toString());
    }

    @Override
    public void delUserMinecraft(long user, @NotNull UUID minecraft) throws DataAccessException {
        this.sqlConnector.executeSilent(Statements.DEL_USER_MINECRAFT, user, minecraft.toString());
    }
}
