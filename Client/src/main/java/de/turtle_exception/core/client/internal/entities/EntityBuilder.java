package de.turtle_exception.core.client.internal.entities;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import de.turtle_exception.core.client.api.entities.Group;
import de.turtle_exception.core.client.api.entities.User;
import de.turtle_exception.core.core.util.Checks;
import de.turtle_exception.core.core.util.JsonUtil;
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
    public static @NotNull User buildUser(JsonObject json) throws NullPointerException, IllegalArgumentException {
        Checks.nonNull(json, "JSON");

        // TODO
    }

    public static @NotNull List<User> buildUsers(JsonArray json) throws NullPointerException, IllegalArgumentException {
        Checks.nonNull(json, "JSON");

        List<User> users = new ArrayList<>();

        for (JsonElement jsonElement : json) {
            // ignore all elements that are not a JsonObject
            if (!(jsonElement instanceof JsonObject obj)) continue;
            users.add(buildUser(obj));
        }

        return List.copyOf(users);
    }

    /**
     * Builds a {@link Group} object from the provided JSON data.
     * @param json The JSON-formatted group data.
     * @return A Group with the provided attributes from the JSON data.
     * @throws IllegalArgumentException if the JSON String does not represent a valid Group or is not a properly
     *                                  formatted JSON object.
     */
    public static @NotNull Group buildGroup(JsonObject json) throws NullPointerException, IllegalArgumentException {
        Checks.nonNull(json, "JSON");

        // TODO
    }

    public static @NotNull List<Group> buildGroups(JsonArray json) throws NullPointerException, IllegalArgumentException {
        Checks.nonNull(json, "JSON");

        List<Group> groups = new ArrayList<>();

        for (JsonElement jsonElement : json) {
            // ignore all elements that are not a JsonObject
            if (!(jsonElement instanceof JsonObject obj)) continue;
            groups.add(buildGroup(obj));
        }

        return List.copyOf(groups);
    }
}
