package de.turtle_exception.core.server.data;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public interface DataService {
    /**
     * Provides a List containing the ids of all groups.
     * @return List of group ids
     * @throws DataAccessException if the request fails.
     */
    @NotNull List<Long> getGroupIds() throws DataAccessException;

    // TODO: docs
    default @NotNull List<String> getGroups() throws DataAccessException {
        ArrayList<String> list = new ArrayList<>();

        for (Long group : this.getGroupIds())
            list.add(this.getGroup(group));

        return List.copyOf(list);
    }

    /**
     * Provides a single group specified by its id in form of a JSON object.
     * @return JSON representation of the group.
     * @throws DataAccessException if the request fails.
     */
    @NotNull String getGroup(long id) throws DataAccessException;

    /**
     * Takes in a JSON object representing a group and feeds it into the underlying database, overwriting any existing
     * data of that group.
     * @param group JSON representation of the group.
     */
    void setGroup(@NotNull String group) throws DataAccessException;

    /**
     * Deletes all data of the group specified by the id.
     * @param id id of the group that should be deleted.
     * @implNote This method should generally also delete all relational data (i.e. membership of a user)
     */
    void deleteGroup(long id) throws DataAccessException;

    /**
     * Provides a List containing the ids of all users.
     * @return List of user ids
     * @throws DataAccessException if the request fails.
     */
    @NotNull List<Long> getUserIds() throws DataAccessException;

    // TODO: docs
    default @NotNull List<String> getUsers() throws DataAccessException {
        ArrayList<String> list = new ArrayList<>();

        for (Long user : this.getUserIds())
            list.add(this.getGroup(user));

        return List.copyOf(list);
    }

    /**
     * Provides a single user specified by its id in form of a JSON object.
     * @return JSON representation of the user.
     * @throws DataAccessException if the request fails.
     */
    @NotNull String getUser(long id) throws DataAccessException;

    /**
     * Takes in a JSON object representing a user and feeds it into the underlying database, overwriting any existing
     * data of that user.
     * @param user JSON representation of the user.
     */
    void setUser(@NotNull String user) throws DataAccessException;

    /**
     * Deletes all data of the user specified by the id.
     * @param id id of the user that should be deleted.
     * @implNote This method should generally also delete all relational data (i.e. membership in a group)
     */
    void deleteUser(long id) throws DataAccessException;

    void userJoinGroup(long userId, long groupId) throws DataAccessException;

    void userLeaveGroup(long userId, long groupId) throws DataAccessException;

    @NotNull List<Long> getUserDiscord(long user) throws DataAccessException;

    @NotNull List<UUID> getUserMinecraft(long user) throws DataAccessException;

    void addUserDiscord(long user, long discord) throws DataAccessException;

    void delUserDiscord(long user, long discord) throws DataAccessException;

    void addUserMinecraft(long user, @NotNull UUID minecraft) throws DataAccessException;

    void delUserMinecraft(long user, @NotNull UUID minecraft) throws DataAccessException;
}
