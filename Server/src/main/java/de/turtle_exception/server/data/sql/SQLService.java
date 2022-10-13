package de.turtle_exception.server.data.sql;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import de.turtle_exception.core.data.JsonChecks;
import de.turtle_exception.core.util.ExceptionalFunction;
import de.turtle_exception.server.data.DataAccessException;
import de.turtle_exception.server.data.DataService;
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

    // These locks are used to prevent reading old data while it is being rewritten by another Thread
    private final Object  groupLock = new Object();
    private final Object   userLock = new Object();
    private final Object ticketLock = new Object();

    public SQLService(@NotNull String host, int port, @NotNull String database, @NotNull String login, @NotNull String pass) throws SQLException {
        this.sqlConnector = new SQLConnector(host, port, database, login, pass);

        // create tables (if they don't already exist)
        this.sqlConnector.executeSilentRaw(Statements.CT_CREDENTIALS);
        this.sqlConnector.executeSilentRaw(Statements.CT_GROUPS);
        this.sqlConnector.executeSilentRaw(Statements.CT_MEMBERS);
        this.sqlConnector.executeSilentRaw(Statements.CT_USERS);
        this.sqlConnector.executeSilentRaw(Statements.CT_USER_DISCORD);
        this.sqlConnector.executeSilentRaw(Statements.CT_USER_MINECRAFT);
        this.sqlConnector.executeSilentRaw(Statements.CT_TICKETS);
        this.sqlConnector.executeSilentRaw(Statements.CT_TICKET_TAGS);
        this.sqlConnector.executeSilentRaw(Statements.CT_TICKET_USERS);
    }

    /* - - - */

    @Override
    public @NotNull String getPass(@NotNull String login) throws DataAccessException {
        try (ResultSet resultSet = this.sqlConnector.executeQuery(Statements.GET_PASS, login)) {
            if (!resultSet.next())
                throw new DataAccessException("Could not parse pass from empty ResultSet.");

            return resultSet.getString("pass");
        } catch (Exception e) {
            throw new DataAccessException(e);
        }
    }

    @Override
    public @NotNull List<Long> getGroupIds() throws DataAccessException {
        try (ResultSet resultSet = this.sqlConnector.executeQuery(Statements.GET_GROUP_IDS)) {
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
        try (ResultSet resultSet = this.sqlConnector.executeQuery(Statements.GET_GROUP)) {
            JsonObject json = new JsonObject();

            if (!resultSet.next())
                throw new DataAccessException("Could not parse group from empty ResultSet.");

            json.addProperty("id"         , resultSet.getLong("id"));
            json.addProperty("name"       , resultSet.getString("name"));
            json.addProperty("permissions", resultSet.getLong("permissions"));
            json.add("members", this.getGroupMembers(id));

            return json;
        } catch (Exception e) {
            throw new DataAccessException(e);
        }
    }

    @Override
    public void setGroup(@NotNull JsonObject group) throws DataAccessException {
        long id, permissions;
        String name;

        try {
            id = group.get("id").getAsLong();
            name = group.get("name").getAsString();
            permissions = group.get("permissions").getAsLong();
        } catch (Exception e) {
            throw new DataAccessException("Malformed parameters for group object");
        }

        // simpler to use this (all members will also be deleted)
        this.deleteGroup(id);

        this.sqlConnector.executeSilent(Statements.SET_GROUP, id, name, permissions);

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
    public void modifyGroup(long group, @NotNull ExceptionalFunction<JsonObject, JsonObject> function) throws DataAccessException {
        synchronized (groupLock) {
            JsonObject json = this.getGroup(group);
            try {
                json = function.apply(json);
            } catch (Exception e) {
                throw new DataAccessException(e);
            }
            this.setGroup(json);
        }
    }

    @Override
    public @NotNull JsonArray getGroupMembers(long group) throws DataAccessException {
        try (ResultSet resultSet = this.sqlConnector.executeQuery(Statements.GET_MEMBERS, group)) {
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

    /* - - - */

    @Override
    public @NotNull List<Long> getUserIds() throws DataAccessException {
        try (ResultSet resultSet = this.sqlConnector.executeQuery(Statements.GET_USER_IDS)) {
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
        try (ResultSet resultUser = this.sqlConnector.executeQuery(Statements.GET_USER, id)) {
            JsonObject json = new JsonObject();

            if (!resultUser.next())
                throw new DataAccessException("Could not parse user from empty ResultSet.");

            json.addProperty("id"         , resultUser.getLong("id"));
            json.addProperty("name"       , resultUser.getString("name"));
            json.addProperty("permissions", resultUser.getLong("permissions"));
            json.add("discord"  , this.getUserDiscord(id));
            json.add("minecraft", this.getUserMinecraft(id));

            return json;
        } catch (Exception e) {
            throw new DataAccessException(e);
        }
    }

    @Override
    public void setUser(@NotNull JsonObject user) throws DataAccessException {
        long id, permissions;
        String name;

        try {
            id = user.get("id").getAsLong();
            name = user.get("name").getAsString();
            permissions = user.get("permissions").getAsLong();
        } catch (Exception e) {
            throw new DataAccessException("Malformed parameters for user object");
        }

        this.sqlConnector.executeSilent(Statements.SET_USER, id, name, permissions);

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

        this.sqlConnector.executeSilent(Statements.DEL_USER, id);

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
    public void modifyUser(long user, @NotNull ExceptionalFunction<JsonObject, JsonObject> function) throws DataAccessException {
        synchronized (userLock) {
            JsonObject json = this.getUser(user);
            try {
                json = function.apply(json);
            } catch (Exception e) {
                throw new DataAccessException(e);
            }
            this.setUser(json);
        }
    }

    @Override
    public @NotNull JsonArray getUserDiscord(long user) throws DataAccessException {
        try (ResultSet resultSet = this.sqlConnector.executeQuery(Statements.GET_USER_DISCORD, user)) {
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
        try (ResultSet resultSet = this.sqlConnector.executeQuery(Statements.GET_USER_MINECRAFT, user)) {
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
        this.sqlConnector.executeSilent(Statements.ADD_USER_MINECRAFT, user, minecraft.toString());
    }

    @Override
    public void delUserMinecraft(long user, @NotNull UUID minecraft) throws DataAccessException {
        this.sqlConnector.executeSilent(Statements.DEL_USER_MINECRAFT, user, minecraft.toString());
    }

    /* - - - */

    @Override
    public @NotNull List<Long> getTicketIds() throws DataAccessException {
        try (ResultSet resultSet = this.sqlConnector.executeQuery(Statements.GET_TICKET_IDS)) {
            ArrayList<Long> list = new ArrayList<>();

            while (resultSet.next())
                list.add(resultSet.getLong(1));

            return List.copyOf(list);
        } catch (Exception e) {
            throw new DataAccessException(e);
        }
    }

    @Override
    public @NotNull JsonObject getTicket(long id) throws DataAccessException {
        try (ResultSet resultUser = this.sqlConnector.executeQuery(Statements.GET_TICKET, id)) {
            JsonObject json = new JsonObject();

            if (!resultUser.next())
                throw new DataAccessException("Could not parse ticket from empty ResultSet.");

            json.addProperty("id"  , resultUser.getLong("id"));
            json.addProperty("state", resultUser.getByte("state"));
            json.addProperty("title", resultUser.getString("title"));
            json.addProperty("category", resultUser.getString("category"));
            json.add("tags"  , this.getTicketTags(id));
            json.addProperty("discord_channel", resultUser.getLong("discord_channel"));
            json.add("users", this.getTicketUsers(id));

            return json;
        } catch (Exception e) {
            throw new DataAccessException(e);
        }
    }

    @Override
    public void setTicket(@NotNull JsonObject ticket) throws DataAccessException {
        JsonChecks.validateTicket(ticket);

        long id             = ticket.get("id").getAsLong();
        byte state          = ticket.get("state").getAsByte();
        long discordChannel = ticket.get("discord_channel").getAsLong();
        String category     = ticket.get("category").getAsString();
        String title = "NULL";

        try {
            title = "'" +  ticket.get("title").getAsString() + "'";
        } catch (Exception ignored) { }

        this.sqlConnector.executeSilent(Statements.SET_TICKET, id, state, title, category, discordChannel);

        JsonArray tags = ticket.getAsJsonArray("tags");
        if (tags != null) {
            for (JsonElement entry : tags) {
                try {
                    this.addTicketTag(id, entry.getAsString());
                } catch (Exception ignored) { }
            }
        }

        JsonArray users = ticket.getAsJsonArray("users");
        if (users != null) {
            for (JsonElement entry : users) {
                try {
                    this.addTicketUser(id, entry.getAsLong());
                } catch (Exception ignored) { }
            }
        }
    }

    @Override
    public void deleteTicket(long id) throws DataAccessException {
        JsonObject ticket = this.getGroup(id);

        this.sqlConnector.executeSilent(Statements.DEL_TICKET, id);

        for (JsonElement entry : ticket.getAsJsonArray("tags")) {
            String tag = entry.getAsString();
            this.delTicketTag(id, tag);
        }

        for (JsonElement entry : ticket.getAsJsonArray("users")) {
            long user = entry.getAsLong();
            this.delTicketUser(id, user);
        }
    }

    @Override
    public void modifyTicket(long ticket, @NotNull ExceptionalFunction<JsonObject, JsonObject> function) throws DataAccessException {
        synchronized (ticketLock) {
            JsonObject json = this.getTicket(ticket);
            try {
                json = function.apply(json);
            } catch (Exception e) {
                throw new DataAccessException(e);
            }
            this.setTicket(json);
        }
    }

    @Override
    public @NotNull JsonArray getTicketTags(long ticket) throws DataAccessException {
        try (ResultSet resultSet = this.sqlConnector.executeQuery(Statements.GET_TICKET_TAGS, ticket)) {
            JsonArray tags = new JsonArray();

            while (resultSet.next())
                tags.add(resultSet.getString("tags"));

            return tags;
        } catch (Exception e) {
            throw new DataAccessException(e);
        }
    }

    @Override
    public void addTicketTag(long ticket, @NotNull String tag) throws DataAccessException {
        this.sqlConnector.executeSilent(Statements.ADD_TICKET_TAG, ticket, tag);
    }

    @Override
    public void delTicketTag(long ticket, @NotNull String tag) throws DataAccessException {
        this.sqlConnector.executeSilent(Statements.DEL_TICKET_TAG, ticket, tag);
    }

    @Override
    public @NotNull JsonArray getTicketUsers(long ticket) throws DataAccessException {
        try (ResultSet resultSet = this.sqlConnector.executeQuery(Statements.GET_TICKET_USERS, ticket)) {
            JsonArray users = new JsonArray();

            while (resultSet.next())
                users.add(resultSet.getLong("users"));

            return users;
        } catch (Exception e) {
            throw new DataAccessException(e);
        }
    }

    @Override
    public void addTicketUser(long ticket, long user) throws DataAccessException {
        this.sqlConnector.executeSilent(Statements.ADD_TICKET_USER, ticket, user);
    }

    @Override
    public void delTicketUser(long ticket, long user) throws DataAccessException {
        this.sqlConnector.executeSilent(Statements.DEL_TICKET_USER, ticket, user);
    }

    /* - - - */

    @Override
    public void stop() throws Exception {
        this.sqlConnector.shutdown();
    }
}
