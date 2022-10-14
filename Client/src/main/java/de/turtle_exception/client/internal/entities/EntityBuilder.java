package de.turtle_exception.client.internal.entities;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import de.turtle_exception.client.api.TurtlePermission;
import de.turtle_exception.client.api.TurtleClient;
import de.turtle_exception.client.api.entities.Group;
import de.turtle_exception.client.api.entities.Ticket;
import de.turtle_exception.client.api.entities.User;
import de.turtle_exception.core.data.TicketState;
import de.turtle_exception.client.internal.util.TurtleSet;
import de.turtle_exception.core.data.IllegalJsonException;
import de.turtle_exception.core.data.JsonChecks;
import de.turtle_exception.core.util.Checks;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.UUID;

public class EntityBuilder {
    private EntityBuilder() { }

    /**
     * Builds a {@link User} object from the provided JSON data.
     * @param json The JSON-formatted user data.
     * @return A User with the provided attributes from the JSON data.
     * @throws IllegalArgumentException if the JSON String does not represent a valid User or is not a properly
     *                                  formatted JSON object.
     */
    public static @NotNull User buildUser(TurtleClient client, JsonObject json) throws NullPointerException, IllegalArgumentException, IllegalJsonException {
        Checks.nonNull(json, "JSON");
        JsonChecks.validateUser(json);

        long   id          = json.get("id").getAsLong();
        String name        = json.get("name").getAsString();
        long   permissions = json.get("permissions").getAsLong();
        long   permissionsDiscord = json.get("permissions_discord").getAsLong();

        JsonArray       discordArr  = json.getAsJsonArray("discord");
        ArrayList<Long> discordList = new ArrayList<>();
        for (JsonElement element : discordArr)
            discordList.add(element.getAsLong());

        JsonArray       minecraftArr  = json.getAsJsonArray("discord");
        ArrayList<UUID> minecraftList = new ArrayList<>();
        for (JsonElement element : minecraftArr)
            minecraftList.add(UUID.fromString(element.getAsString()));

        EnumSet<TurtlePermission> permissionSet = TurtlePermission.fromRaw(permissions);
        EnumSet<net.dv8tion.jda.api.Permission> permissionDiscordSet = net.dv8tion.jda.api.Permission.getPermissions(permissionsDiscord);

        return new UserImpl(client, id, name, discordList, minecraftList, permissionSet, permissionDiscordSet);
    }

    public static @NotNull List<User> buildUsers(TurtleClient client, JsonArray json) throws NullPointerException, IllegalArgumentException, IllegalJsonException {
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
    public static @NotNull Group buildGroup(TurtleClient client, JsonObject json) throws NullPointerException, IllegalArgumentException, IllegalJsonException {
        Checks.nonNull(json, "JSON");
        JsonChecks.validateGroup(json);

        long   id          = json.get("id").getAsLong();
        String name        = json.get("name").getAsString();
        long   permissions = json.get("permissions").getAsLong();
        long   permissionsDiscord = json.get("permissions_discord").getAsLong();

        JsonArray       userArr  = json.getAsJsonArray("members");
        TurtleSet<User> users    = new TurtleSet<>();
        for (JsonElement element : userArr)
            users.add(client.getUserById(element.getAsLong()));

        EnumSet<TurtlePermission> permissionSet = TurtlePermission.fromRaw(permissions);
        EnumSet< net.dv8tion.jda.api.Permission> permissionDiscordSet = net.dv8tion.jda.api.Permission.getPermissions(permissionsDiscord);

        return new GroupImpl(client, id, name, users, permissionSet, permissionDiscordSet);
    }

    public static @NotNull List<Group> buildGroups(TurtleClient client, JsonArray json) throws NullPointerException, IllegalArgumentException, IllegalJsonException {
        Checks.nonNull(json, "JSON");

        List<Group> groups = new ArrayList<>();
        for (JsonElement jsonElement : json)
            groups.add(buildGroup(client, (JsonObject) jsonElement));

        return List.copyOf(groups);
    }

    public static @NotNull Ticket buildTicket(TurtleClient client, JsonObject json) throws NullPointerException, IllegalArgumentException, IllegalJsonException {
        Checks.nonNull(json, "JSON");
        JsonChecks.validateTicket(json);

        long   id             = json.get("id").getAsLong();
        byte   stateCode      = json.get("state").getAsByte();
        String title          = json.get("title").getAsString();
        String category       = json.get("category").getAsString();
        long   discordChannel = json.get("discord_channel").getAsLong();

        if (title == null)
            title = "null";

        TicketState state = TicketState.of(stateCode);

        JsonArray         tagArr = json.getAsJsonArray("tags");
        ArrayList<String> tags   = new ArrayList<>();
        for (JsonElement element : tagArr)
            tags.add(element.getAsString());

        JsonArray       userArr  = json.getAsJsonArray("users");
        TurtleSet<User> users    = new TurtleSet<>();
        for (JsonElement element : userArr)
            users.add(client.getUserById(element.getAsLong()));

        return new TicketImpl(client, id, state, title, category, discordChannel, tags, users);
    }

    public static @NotNull List<Ticket> buildTickets(TurtleClient client, JsonArray json) throws NullPointerException, IllegalArgumentException, IllegalJsonException {
        Checks.nonNull(json, "JSON");

        List<Ticket> tickets = new ArrayList<>();
        for (JsonElement jsonElement : json)
            tickets.add(buildTicket(client, (JsonObject) jsonElement));

        return List.copyOf(tickets);
    }
}
