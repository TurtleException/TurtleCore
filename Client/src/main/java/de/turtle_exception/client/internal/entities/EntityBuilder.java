package de.turtle_exception.client.internal.entities;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import de.turtle_exception.client.api.TurtleClient;
import de.turtle_exception.client.api.entities.Group;
import de.turtle_exception.client.api.entities.Ticket;
import de.turtle_exception.client.api.entities.User;
import de.turtle_exception.client.api.entities.attributes.MessageFormat;
import de.turtle_exception.client.api.entities.attributes.ProjectState;
import de.turtle_exception.client.api.entities.attributes.TicketState;
import de.turtle_exception.client.api.entities.form.ContentType;
import de.turtle_exception.client.api.entities.form.QueryElement;
import de.turtle_exception.client.api.entities.form.QueryResponse;
import de.turtle_exception.client.api.entities.form.TemplateForm;
import de.turtle_exception.client.api.entities.messages.IChannel;
import de.turtle_exception.client.api.entities.messages.MinecraftChannel;
import de.turtle_exception.client.api.entities.messages.SyncChannel;
import de.turtle_exception.client.internal.data.IllegalJsonException;
import de.turtle_exception.client.internal.data.annotations.Keys;
import de.turtle_exception.client.internal.entities.form.*;
import de.turtle_exception.client.internal.entities.messages.DiscordChannelImpl;
import de.turtle_exception.client.internal.entities.messages.MinecraftChannelImpl;
import de.turtle_exception.client.internal.entities.messages.SyncChannelImpl;
import de.turtle_exception.client.internal.entities.messages.SyncMessageImpl;
import de.turtle_exception.client.internal.util.Checks;
import de.turtle_exception.client.internal.util.TurtleSet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.logging.Level;

@SuppressWarnings("unused")
public class EntityBuilder {
    private EntityBuilder() { }

    @SuppressWarnings("SameParameterValue")
    private static void log(@NotNull TurtleClient client, @NotNull Level level, @NotNull String type, Long id, @NotNull String msg) {
        client.getLogger().log(Level.FINE, java.text.MessageFormat.format("[EntityBuilder] [{0}:{1}]  " + msg, type, id));
    }

    /**
     * Builds a {@link Group} object from the provided JSON data.
     * @param data The JSON-formatted group data.
     * @return A Group with the provided attributes from the JSON data.
     * @throws IllegalArgumentException if the JSON String does not represent a valid Group or is not a properly
     *                                  formatted JSON object.
     */
    public static @NotNull Group buildGroup(@NotNull JsonObject data, @NotNull TurtleClient client) throws NullPointerException, IllegalArgumentException, IllegalJsonException {
        Checks.nonNull(data, "JSON");

        long   id   = data.get(Keys.Turtle.ID).getAsLong();
        String name = data.get(Keys.Group.NAME).getAsString();

        JsonArray       userArr  = data.getAsJsonArray(Keys.Group.MEMBERS);
        TurtleSet<User> users    = new TurtleSet<>();
        for (JsonElement element : userArr) {
            User userElement = client.getTurtleById(element.getAsLong(), User.class);
            if (userElement == null)
                log(client, Level.FINE, "Group", id, "Could not link User:" + element.getAsLong() + ". Has it been deleted?");
            else
                users.add(userElement);
        }

        return new GroupImpl(client, id, name, users);
    }

    public static @NotNull JsonResourceImpl buildJsonResource(@NotNull JsonObject data, @NotNull TurtleClient client) throws NullPointerException, IllegalArgumentException, IllegalJsonException {
        Checks.nonNull(data, "JSON");

        long        id         = data.get(Keys.Turtle.ID).getAsLong();
        String      identifier = data.get(Keys.JsonResource.IDENTIFIER).getAsString();
        JsonElement content    = data.get(Keys.JsonResource.CONTENT);
        boolean     ephemeral  = data.get(Keys.Attribute.EphemeralType.EPHEMERAL).getAsBoolean();

        return new JsonResourceImpl(client, id, identifier, content, ephemeral);
    }

    public static @NotNull ProjectImpl buildProject(@NotNull JsonObject data, @NotNull TurtleClient client) throws NullPointerException, IllegalArgumentException, IllegalJsonException {
        Checks.nonNull(data, "JSON");

        long         id    = data.get(Keys.Turtle.ID).getAsLong();
        String       title = getOptional(() -> data.get(Keys.Project.TITLE).getAsString());
        String       code  = data.get(Keys.Project.CODE).getAsString();
        ProjectState state = ProjectState.of(data.get(Keys.Project.STATE).getAsByte());

        JsonArray       userArr  = data.getAsJsonArray(Keys.Project.MEMBERS);
        TurtleSet<User> users    = new TurtleSet<>();
        for (JsonElement element : userArr) {
            User userElement = client.getTurtleById(element.getAsLong(), User.class);
            if (userElement == null)
                log(client, Level.FINE, "Project", id, "Could not link User:" + element.getAsLong() + ". Has it been deleted?");
            else
                users.add(userElement);
        }

        TemplateForm applicationForm = client.getTurtleById(data.get(Keys.Project.APP_FORM).getAsLong(), TemplateForm.class);
        long         timeRelease     = data.get(Keys.Project.TIME_RELEASE).getAsLong();
        long         timeApply       = data.get(Keys.Project.TIME_APPLY).getAsLong();
        long         timeStart       = data.get(Keys.Project.TIME_START).getAsLong();
        long         timeEnd         = data.get(Keys.Project.TIME_END).getAsLong();

        return new ProjectImpl(client, id, title, code, state, users, applicationForm, timeRelease, timeApply, timeStart, timeEnd);
    }

    public static @NotNull Ticket buildTicket(@NotNull JsonObject data, @NotNull TurtleClient client) throws NullPointerException, IllegalArgumentException, IllegalJsonException {
        Checks.nonNull(data, "JSON");

        long   id             = data.get(Keys.Turtle.ID).getAsLong();
        byte   stateCode      = data.get(Keys.Ticket.STATE).getAsByte();
        String title          = getOptional(() -> data.get(Keys.Ticket.TITLE).getAsString());
        String category       = data.get(Keys.Ticket.CATEGORY).getAsString();
        long   discordChannel = data.get(Keys.Ticket.DISCORD_CHANNEL).getAsLong();

        TicketState state = TicketState.of(stateCode);

        JsonArray         tagArr = data.getAsJsonArray(Keys.Ticket.TAGS);
        ArrayList<String> tags   = new ArrayList<>();
        for (JsonElement element : tagArr)
            tags.add(element.getAsString());

        JsonArray       userArr  = data.getAsJsonArray(Keys.Ticket.USERS);
        TurtleSet<User> users    = new TurtleSet<>();
        for (JsonElement element : userArr) {
            User userElement = client.getTurtleById(element.getAsLong(), User.class);
            if (userElement == null)
                log(client, Level.FINE, "Ticket", id, "Could not link User:" + element.getAsLong() + ". Has it been deleted?");
            else
                users.add(userElement);
        }

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

        long   id   = data.get(Keys.Turtle.ID).getAsLong();
        String name = data.get(Keys.User.NAME).getAsString();

        JsonArray discordArr = data.getAsJsonArray(Keys.User.DISCORD);
        ArrayList<Long> discordList = new ArrayList<>();
        for (JsonElement element : discordArr)
            discordList.add(element.getAsLong());

        JsonArray minecraftArr = data.getAsJsonArray(Keys.User.MINECRAFT);
        ArrayList<UUID> minecraftList = new ArrayList<>();
        for (JsonElement element : minecraftArr)
            minecraftList.add(UUID.fromString(element.getAsString()));

        return new UserImpl(client, id, name, discordList, minecraftList);
    }

    /* - FORM - */

    public static @NotNull CompletedFormImpl buildCompletedForm(@NotNull JsonObject data, @NotNull TurtleClient client) throws NullPointerException, IllegalArgumentException, IllegalJsonException {
        Checks.nonNull(data, "JSON");

        long         id     = data.get(Keys.Turtle.ID).getAsLong();
        TemplateForm form   = client.getTurtleById(data.get(Keys.Form.CompletedForm.FORM).getAsLong(), TemplateForm.class);
        User         author = client.getTurtleById(data.get(Keys.Form.CompletedForm.AUTHOR).getAsLong(), User.class);
        long         time   = data.get(Keys.Form.CompletedForm.TIME_SUBMISSION).getAsLong();

        JsonArray queryResponseArr = data.getAsJsonArray(Keys.Form.CompletedForm.QUERY_RESPONSES);
        ArrayList<QueryResponse> queryResponses = new ArrayList<>();
        for (JsonElement element : queryResponseArr)
            queryResponses.add(client.getTurtleById(element.getAsLong(), QueryResponse.class));

        return new CompletedFormImpl(client, id, form, author, time, queryResponses);
    }

    public static @NotNull QueryElementImpl buildQueryElement(@NotNull JsonObject data, @NotNull TurtleClient client) throws NullPointerException, IllegalArgumentException, IllegalJsonException {
        Checks.nonNull(data, "JSON");

        long        id          = data.get(Keys.Turtle.ID).getAsLong();
        String      title       = getOptional(() -> data.get(Keys.Form.Element.TITLE).getAsString());
        String      description = getOptional(() -> data.get(Keys.Form.QueryElement.DESCRIPTION).getAsString());
        ContentType contentType = ContentType.of(data.get(Keys.Form.QueryElement.CONTENT_TYPE).getAsByte());
        boolean     required    = data.get(Keys.Form.QueryElement.REQUIRED).getAsBoolean();

        return new QueryElementImpl(client, id, title, description, contentType, required);
    }

    public static @NotNull QueryResponseImpl buildQueryResponse(@NotNull JsonObject data, @NotNull TurtleClient client) throws NullPointerException, IllegalArgumentException, IllegalJsonException {
        Checks.nonNull(data, "JSON");

        long         id      = data.get(Keys.Turtle.ID).getAsLong();
        QueryElement query   = client.getTurtleById(data.get(Keys.Form.QueryResponse.QUERY).getAsLong(), QueryElement.class);
        String       content = getOptional(() -> data.get(Keys.Form.QueryResponse.CONTENT).getAsString());

        return new QueryResponseImpl(client, id, query, content);
    }

    public static @NotNull TemplateFormImpl buildTemplateForm(@NotNull JsonObject data, @NotNull TurtleClient client) throws NullPointerException, IllegalArgumentException, IllegalJsonException {
        Checks.nonNull(data, "JSON");

        long   id    = data.get(Keys.Turtle.ID).getAsLong();
        String title = data.get(Keys.Form.TemplateForm.TITLE).getAsString();

        JsonArray queryArr = data.getAsJsonArray(Keys.Form.TemplateForm.QUERIES);
        ArrayList<QueryElement> queries = new ArrayList<>();
        for (JsonElement element : queryArr)
            queries.add(client.getTurtleById(element.getAsLong(), QueryElement.class));

        return new TemplateFormImpl(client, id, title, queries);
    }

    public static @NotNull TextElementImpl buildTextElement(@NotNull JsonObject data, @NotNull TurtleClient client) throws NullPointerException, IllegalArgumentException, IllegalJsonException {
        Checks.nonNull(data, "JSON");

        long   id      = data.get(Keys.Turtle.ID).getAsLong();
        String title   = getOptional(() -> data.get(Keys.Form.Element.TITLE).getAsString());
        String content = data.get(Keys.Form.TextElement.CONTENT).getAsString();

        return new TextElementImpl(client, id, title, content);
    }

    /* - MESSAGES - */

    public static @NotNull DiscordChannelImpl buildDiscordChannel(@NotNull JsonObject data, @NotNull TurtleClient client) throws NullPointerException, IllegalArgumentException, IllegalJsonException {
        Checks.nonNull(data, "JSON");

        long id          = data.get(Keys.Turtle.ID).getAsLong();
        long syncChannel = data.get(Keys.Messages.IChannel.SYNC_CHANNEL).getAsLong();
        long snowflake   = data.get(Keys.Messages.DiscordChannel.SNOWFLAKE).getAsLong();

        return new DiscordChannelImpl(client, id, syncChannel, snowflake);
    }

    public static @NotNull MinecraftChannelImpl buildMinecraftChannel(@NotNull JsonObject data, @NotNull TurtleClient client) throws NullPointerException, IllegalArgumentException, IllegalJsonException {
        Checks.nonNull(data, "JSON");

        long           id          = data.get(Keys.Turtle.ID).getAsLong();
        long           syncChannel = data.get(Keys.Messages.IChannel.SYNC_CHANNEL).getAsLong();
        MinecraftChannel.Type type = MinecraftChannel.Type.of(data.get(Keys.Messages.MinecraftChannel.TYPE).getAsByte());
        String         identifier  = data.get(Keys.Messages.MinecraftChannel.IDENTIFIER).getAsString();

        return new MinecraftChannelImpl(client, id, syncChannel, type, identifier);
    }

    public static @NotNull SyncChannelImpl buildSyncChannel(@NotNull JsonObject data, @NotNull TurtleClient client) throws NullPointerException, IllegalArgumentException, IllegalJsonException {
        Checks.nonNull(data, "JSON");

        long id = data.get(Keys.Turtle.ID).getAsLong();

        return new SyncChannelImpl(client, id);
    }

    public static @NotNull SyncMessageImpl buildMessage(@NotNull JsonObject data, @NotNull TurtleClient client) throws NullPointerException, IllegalArgumentException, IllegalJsonException {
        Checks.nonNull(data, "JSON");

        long          id        = data.get(Keys.Turtle.ID).getAsLong();
        MessageFormat format    = MessageFormat.of(data.get(Keys.Messages.SyncMessage.FORMAT).getAsByte());
        User          author    = client.getTurtleById(data.get(Keys.Messages.SyncMessage.AUTHOR).getAsLong(), User.class);
        String        content   = data.get(Keys.Messages.SyncMessage.CONTENT).getAsString();
        Long          reference = getOptional(() -> data.get(Keys.Messages.SyncMessage.REFERENCE).getAsLong());
        SyncChannel   channel   = client.getTurtleById(data.get(Keys.Messages.SyncMessage.CHANNEL).getAsLong(), SyncChannel.class);
        IChannel      source    = getOptional(() -> client.getTurtleById(data.get(Keys.Messages.SyncMessage.SOURCE).getAsLong(), IChannel.class));

        return new SyncMessageImpl(client, id, format, author, content, reference, channel, source);
    }

    /* - - - */

    private static <T> @Nullable T getOptional(@NotNull Callable<T> callable) {
        try {
            return callable.call();
        } catch (Exception e) {
            return null;
        }
    }
}
