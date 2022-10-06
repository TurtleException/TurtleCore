package de.turtle_exception.client.internal.entities;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import de.turtle_exception.client.api.TurtleClient;
import de.turtle_exception.client.api.entities.Group;
import de.turtle_exception.client.api.entities.User;
import de.turtle_exception.client.internal.util.TurtleSet;
import de.turtle_exception.core.util.Checks;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class EntityBuilder {
    /**
     * Builds a {@link User} object from the provided JSON data.
     * @param json The JSON-formatted user data.
     * @return A User with the provided attributes from the JSON data.
     * @throws IllegalArgumentException if the JSON String does not represent a valid User or is not a properly
     *                                  formatted JSON object.
     */
    public static @NotNull User buildUser(TurtleClient client, JsonObject json) throws NullPointerException, IllegalArgumentException {
        Checks.nonNull(json, "JSON");

        long   id   = json.get("id").getAsLong();
        String name = json.get("name").getAsString();

        JsonArray       discordArr  = json.getAsJsonArray("discord");
        ArrayList<Long> discordList = new ArrayList<>();
        for (JsonElement element : discordArr)
            discordList.add(element.getAsLong());

        JsonArray       minecraftArr  = json.getAsJsonArray("discord");
        ArrayList<UUID> minecraftList = new ArrayList<>();
        for (JsonElement element : minecraftArr)
            minecraftList.add(UUID.fromString(element.getAsString()));

        return new UserImpl(client, id, name, discordList, minecraftList);
    }

    public static @NotNull List<User> buildUsers(TurtleClient client, JsonArray json) throws NullPointerException, IllegalArgumentException {
        Checks.nonNull(json, "JSON");

        List<User> users = new ArrayList<>();
        for (JsonElement jsonElement : json)
            users.add(buildUser(client, ((JsonObject) jsonElement)));

        return List.copyOf(users);
    }

    /**
     * Builds a {@link Group} object from the provided JSON data.
     * @param json The JSON-formatted group data.
     * @return A Group with the provided attributes from the JSON data.
     * @throws IllegalArgumentException if the JSON String does not represent a valid Group or is not a properly
     *                                  formatted JSON object.
     */
    public static @NotNull Group buildGroup(TurtleClient client, JsonObject json) throws NullPointerException, IllegalArgumentException {
        Checks.nonNull(json, "JSON");

        long   id   = json.get("id").getAsLong();
        String name = json.get("name").getAsString();

        JsonArray       userArr  = json.getAsJsonArray("members");
        TurtleSet<User> users    = new TurtleSet<>();
        for (JsonElement element : userArr)
            users.add(client.getUserById(element.getAsLong()));

        return new GroupImpl(client, id, name, users);
    }

    public static @NotNull List<Group> buildGroups(TurtleClient client, JsonArray json) throws NullPointerException, IllegalArgumentException {
        Checks.nonNull(json, "JSON");

        List<Group> groups = new ArrayList<>();
        for (JsonElement jsonElement : json)
            groups.add(buildGroup(client, (JsonObject) jsonElement));

        return List.copyOf(groups);
    }
}
