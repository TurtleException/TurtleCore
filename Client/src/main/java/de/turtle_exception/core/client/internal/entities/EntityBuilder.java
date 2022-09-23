package de.turtle_exception.core.client.internal.entities;

import de.turtle_exception.core.api.entities.Group;
import de.turtle_exception.core.api.entities.User;
import de.turtle_exception.core.client.internal.util.JsonUtil;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EntityBuilder {
    /**
     * Builds a {@link User} object from the provided JSON data.
     * @param json The JSON-formatted user data.
     * @return A User with the provided attributes from the JSON data.
     * @throws IllegalArgumentException if the JSON String does not represent a valid User or is not a properly
     *                                  formatted JSON object.
     */
    public static @NotNull User buildUser(@NotNull String json) throws NullPointerException, IllegalArgumentException {
        Map<String, String> data = JsonUtil.jsonToMap(json);

        // TODO
    }

    public static @NotNull List<User> buildUsers(@NotNull String json) throws NullPointerException, IllegalArgumentException {
        List<String> data  = JsonUtil.jsonToList(json);
        List<User>   users = new ArrayList<>();

        for (String userJson : data)
            users.add(buildUser(userJson));

        return users;
    }

    /**
     * Builds a {@link Group} object from the provided JSON data.
     * @param json The JSON-formatted group data.
     * @return A Group with the provided attributes from the JSON data.
     * @throws IllegalArgumentException if the JSON String does not represent a valid Group or is not a properly
     *                                  formatted JSON object.
     */
    public static @NotNull Group buildGroup(@NotNull String json) throws NullPointerException, IllegalArgumentException {
        Map<String, String> data = JsonUtil.jsonToMap(json);

        // TODO
    }

    public static @NotNull List<Group> buildGroups(@NotNull String json) throws NullPointerException, IllegalArgumentException {
        List<String> data   = JsonUtil.jsonToList(json);
        List<Group>  groups = new ArrayList<>();

        for (String groupJson : data)
            groups.add(buildGroup(groupJson));

        return groups;
    }
}
