package de.turtle_exception.client.internal.entities;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import de.turtle_exception.client.api.TicketState;
import de.turtle_exception.client.api.TurtleClient;
import de.turtle_exception.client.api.entities.Group;
import de.turtle_exception.client.api.entities.Ticket;
import de.turtle_exception.client.api.entities.User;
import de.turtle_exception.client.internal.data.IllegalJsonException;
import de.turtle_exception.client.internal.data.JsonChecks;
import de.turtle_exception.client.internal.util.Checks;
import de.turtle_exception.client.internal.util.TurtleSet;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.UUID;

@SuppressWarnings("unused")
public class EntityBuilder {
    private EntityBuilder() { }

    /**
     * Builds a {@link Group} object from the provided JSON data.
     * @param data The JSON-formatted group data.
     * @return A Group with the provided attributes from the JSON data.
     * @throws IllegalArgumentException if the JSON String does not represent a valid Group or is not a properly
     *                                  formatted JSON object.
     */
    public static @NotNull Group buildGroup(@NotNull JsonObject data, @NotNull TurtleClient client) throws NullPointerException, IllegalArgumentException, IllegalJsonException {
        Checks.nonNull(data, "JSON");
        JsonChecks.validateGroup(data);

        long   id   = data.get("id").getAsLong();
        String name = data.get("name").getAsString();

        JsonArray       userArr  = data.getAsJsonArray("members");
        TurtleSet<User> users    = new TurtleSet<>();
        for (JsonElement element : userArr)
            users.add(client.getUserById(element.getAsLong()));

        return new GroupImpl(client, id, name, users);
    }

    public static @NotNull Ticket buildTicket(@NotNull JsonObject data, @NotNull TurtleClient client) throws NullPointerException, IllegalArgumentException, IllegalJsonException {
        Checks.nonNull(data, "JSON");
        JsonChecks.validateTicket(data);

        long   id             = data.get("id").getAsLong();
        byte   stateCode      = data.get("state").getAsByte();
        String title          = data.get("title").getAsString();
        String category       = data.get("category").getAsString();
        long   discordChannel = data.get("discord_channel").getAsLong();

        if (title == null)
            title = "null";

        TicketState state = TicketState.of(stateCode);

        JsonArray         tagArr = data.getAsJsonArray("tags");
        ArrayList<String> tags   = new ArrayList<>();
        for (JsonElement element : tagArr)
            tags.add(element.getAsString());

        JsonArray       userArr  = data.getAsJsonArray("users");
        TurtleSet<User> users    = new TurtleSet<>();
        for (JsonElement element : userArr)
            users.add(client.getUserById(element.getAsLong()));

        return new TicketImpl(client, id, state, title, category, discordChannel, tags, users);
    }

    /**
     * Builds a {@link User} object from the provided JSON data.
     * @param data The JSON-formatted user data.
     * @return A User with the provided attributes from the JSON data.
     * @throws IllegalArgumentException if the JSON String does not represent a valid User or is not a properly
     *                                  formatted JSON object.
     */
    public static @NotNull User buildUser(@NotNull JsonObject data, @NotNull TurtleClient client) throws NullPointerException, IllegalArgumentException, IllegalJsonException {
        Checks.nonNull(data, "JSON");
        JsonChecks.validateUser(data);

        long id = data.get("id").getAsLong();
        String name = data.get("name").getAsString();

        JsonArray discordArr = data.getAsJsonArray("discord");
        ArrayList<Long> discordList = new ArrayList<>();
        for (JsonElement element : discordArr)
            discordList.add(element.getAsLong());

        JsonArray minecraftArr = data.getAsJsonArray("discord");
        ArrayList<UUID> minecraftList = new ArrayList<>();
        for (JsonElement element : minecraftArr)
            minecraftList.add(UUID.fromString(element.getAsString()));

        return new UserImpl(client, id, name, discordList, minecraftList);
    }
}
