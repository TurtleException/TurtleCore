package de.turtle_exception.core.server.data;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;
import java.util.function.Function;

public interface DataService {
    /**
     * Provides the pass for a given login. If no such login exists a {@link DataAccessException} will be thrown.
     * @param login Login to retrieve the pass for.
     * @return pass for the given login.
     * @throws DataAccessException if the request fails.
     */
    @NotNull String getPass(@NotNull String login) throws DataAccessException;

    /**
     * Provides a List containing the ids of all groups.
     * @return List of group ids.
     * @throws DataAccessException if the request fails.
     */
    @NotNull List<Long> getGroupIds() throws DataAccessException;

    /**
     * Provides a {@link JsonArray} containing all groups.
     * @return Array of all groups.
     * @throws DataAccessException if the request fails.
     */
    default @NotNull JsonArray getGroups() throws DataAccessException {
        JsonArray arr = new JsonArray();

        for (Long group : this.getGroupIds())
            arr.add(this.getGroup(group));

        return arr;
    }

    /**
     * Provides a single group specified by its id in form of a JSON object.
     * @return JSON representation of the group.
     * @throws DataAccessException if the request fails.
     */
    @NotNull JsonObject getGroup(long id) throws DataAccessException;

    /**
     * Takes in a JSON object representing a group and feeds it into the underlying database, overwriting any existing
     * data of that group.
     * @param group JSON representation of the group.
     * @throws DataAccessException if the request fails.
     */
    void setGroup(@NotNull JsonObject group) throws DataAccessException;

    // TODO: (see impl note)
    /**
     * Deletes all data of the group specified by the id.
     * @param id id of the group that should be deleted.
     * @throws DataAccessException if the request fails.
     * @implNote This method should generally also delete all relational data (i.e. membership of a user).
     */
    void deleteGroup(long id) throws DataAccessException;

    void modifyGroup(long group, @NotNull Function<JsonObject, JsonObject> function) throws DataAccessException;

    /**
     * Provides a List containing the ids of all users.
     * @return List of user ids.
     * @throws DataAccessException if the request fails.
     */
    @NotNull List<Long> getUserIds() throws DataAccessException;

    /**
     * Provides a {@link JsonArray} containing all users.
     * @return Array of all users.
     * @throws DataAccessException if the request fails.
     */
    default @NotNull JsonArray getUsers() throws DataAccessException {
        JsonArray arr = new JsonArray();

        for (Long user : this.getUserIds())
            arr.add(this.getGroup(user));

        return arr;
    }

    /**
     * Provides a single user specified by its id in form of a JSON object.
     * @return JSON representation of the user.
     * @throws DataAccessException if the request fails.
     */
    @NotNull JsonObject getUser(long id) throws DataAccessException;

    /**
     * Takes in a JSON object representing a user and feeds it into the underlying database, overwriting any existing
     * data of that user.
     * @param user JSON representation of the user.
     * @throws DataAccessException if the request fails.
     */
    void setUser(@NotNull JsonObject user) throws DataAccessException;

    // TODO: (see impl note)
    /**
     * Deletes all data of the user specified by the id.
     * @param id id of the user that should be deleted.
     * @throws DataAccessException if the request fails.
     * @implNote This method should generally also delete all relational data (i.e. membership in a group).
     */
    void deleteUser(long id) throws DataAccessException;

    void modifyUser(long user, @NotNull Function<JsonObject, JsonObject> function) throws DataAccessException;

    /**
     * Provides a List containing the ids of all groups the given user is a member of.
     * @param user id of the user.
     * @return List of group ids.
     * @throws DataAccessException if the request fails.
     */
    @NotNull JsonArray getUserGroups(long user) throws DataAccessException;

    /**
     * Adds a group id to the list of groups that this user is a member of.
     * @param user id of the user.
     * @param group id of the group.
     * @throws DataAccessException if the request fails.
     */
    void addUserGroup(long user, long group) throws DataAccessException;

    /**
     * Removes a group id from the list of groups that this user is a member of.
     * @param user id of the user.
     * @param group id of the group.
     * @throws DataAccessException if the request fails.
     */
    void delUserGroup(long user, long group) throws DataAccessException;

    /**
     * Provides a List containing the ids of all Discord accounts associated with this user.
     * @param user id of the user.
     * @return List of Discord ids.
     * @throws DataAccessException if the request fails.
     */
    @NotNull JsonArray getUserDiscord(long user) throws DataAccessException;

    /**
     * Adds a Discord id to the list of all Discord accounts associated with this user.
     * @param user id of the user.
     * @param discord id of the Discord user.
     * @throws DataAccessException if the request fails.
     */
    void addUserDiscord(long user, long discord) throws DataAccessException;

    /**
     * Removes a Discord id from the list of all Discord accounts associated with this user.
     * @param user id of the user.
     * @param discord id of the Discord user.
     * @throws DataAccessException if the request fails.
     */
    void delUserDiscord(long user, long discord) throws DataAccessException;

    /**
     * Provides a List containing the ids of all Minecraft accounts associated with this user.
     * @param user id of the user.
     * @return List of Minecraft ids.
     * @throws DataAccessException if the request fails.
     */
    @NotNull JsonArray getUserMinecraft(long user) throws DataAccessException;

    /**
     * Adds a Minecraft id to the list of all Minecraft accounts associated with this user.
     * @param user id of the user.
     * @param minecraft id of the Minecraft account.
     * @throws DataAccessException if the request fails.
     */
    void addUserMinecraft(long user, @NotNull UUID minecraft) throws DataAccessException;

    /**
     * Removes a Minecraft id from the list of all Minecraft accounts associated with this user.
     * @param user id of the user.
     * @param minecraft id of the Minecraft account.
     * @throws DataAccessException if the request fails.
     */
    void delUserMinecraft(long user, @NotNull UUID minecraft) throws DataAccessException;
}
