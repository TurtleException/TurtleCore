package de.turtle_exception.client.internal.entities;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import de.turtle_exception.client.api.TurtleClient;
import de.turtle_exception.client.api.entities.Group;
import de.turtle_exception.client.api.entities.Ticket;
import de.turtle_exception.client.api.entities.User;
import de.turtle_exception.client.api.entities.attributes.ProjectState;
import de.turtle_exception.client.api.entities.attributes.TicketState;
import de.turtle_exception.client.api.entities.form.ContentType;
import de.turtle_exception.client.api.entities.messages.MinecraftChannel;
import de.turtle_exception.client.internal.data.IllegalJsonException;
import de.turtle_exception.client.internal.data.annotations.Keys;
import de.turtle_exception.client.internal.entities.form.*;
import de.turtle_exception.client.internal.entities.messages.*;
import de.turtle_exception.client.internal.util.Checks;
import de.turtle_exception.fancyformat.Format;
import de.turtle_exception.fancyformat.FormatText;
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
        ArrayList<Long> users    = new ArrayList<>();
        for (JsonElement element : userArr)
            users.add(element.getAsLong());

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
        ArrayList<Long> users    = new ArrayList<>();
        for (JsonElement element : userArr)
            users.add(element.getAsLong());

        long applicationForm = data.get(Keys.Project.APP_FORM).getAsLong();
        long timeRelease     = data.get(Keys.Project.TIME_RELEASE).getAsLong();
        long timeApply       = data.get(Keys.Project.TIME_APPLY).getAsLong();
        long timeStart       = data.get(Keys.Project.TIME_START).getAsLong();
        long timeEnd         = data.get(Keys.Project.TIME_END).getAsLong();

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
        ArrayList<Long> users    = new ArrayList<>();
        for (JsonElement element : userArr)
            users.add(element.getAsLong());

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

        long id     = data.get(Keys.Turtle.ID).getAsLong();
        long form   = data.get(Keys.Form.CompletedForm.FORM).getAsLong();
        long author = data.get(Keys.Form.CompletedForm.AUTHOR).getAsLong();
        long time   = data.get(Keys.Form.CompletedForm.TIME_SUBMISSION).getAsLong();

        JsonArray queryResponseArr = data.getAsJsonArray(Keys.Form.CompletedForm.QUERY_RESPONSES);
        ArrayList<Long> queryResponses = new ArrayList<>();
        for (JsonElement element : queryResponseArr)
            queryResponses.add(element.getAsLong());

        return new CompletedFormImpl(client, id, form, author, time, queryResponses);
    }

    public static @NotNull QueryElementImpl buildQueryElement(@NotNull JsonObject data, @NotNull TurtleClient client) throws NullPointerException, IllegalArgumentException, IllegalJsonException {
        Checks.nonNull(data, "JSON");

        long        id          = data.get(Keys.Turtle.ID).getAsLong();
        String      title       = getOptional(() -> data.get(Keys.Form.Element.TITLE).getAsString());
        FormatText  description = getOptional(() -> client.getFormatter().newText(data.get(Keys.Form.QueryElement.DESCRIPTION).getAsString(), Format.TURTLE));
        ContentType contentType = ContentType.of(data.get(Keys.Form.QueryElement.CONTENT_TYPE).getAsByte());
        boolean     required    = data.get(Keys.Form.QueryElement.REQUIRED).getAsBoolean();

        return new QueryElementImpl(client, id, title, description, contentType, required);
    }

    public static @NotNull QueryResponseImpl buildQueryResponse(@NotNull JsonObject data, @NotNull TurtleClient client) throws NullPointerException, IllegalArgumentException, IllegalJsonException {
        Checks.nonNull(data, "JSON");

        long   id      = data.get(Keys.Turtle.ID).getAsLong();
        long   query   = data.get(Keys.Form.QueryResponse.QUERY).getAsLong();
        String content = getOptional(() -> data.get(Keys.Form.QueryResponse.CONTENT).getAsString());

        return new QueryResponseImpl(client, id, query, content);
    }

    public static @NotNull TemplateFormImpl buildTemplateForm(@NotNull JsonObject data, @NotNull TurtleClient client) throws NullPointerException, IllegalArgumentException, IllegalJsonException {
        Checks.nonNull(data, "JSON");

        long   id    = data.get(Keys.Turtle.ID).getAsLong();
        String title = data.get(Keys.Form.TemplateForm.TITLE).getAsString();

        JsonArray queryArr = data.getAsJsonArray(Keys.Form.TemplateForm.ELEMENTS);
        ArrayList<Long> elements = new ArrayList<>();
        for (JsonElement element : queryArr)
            elements.add(element.getAsLong());

        return new TemplateFormImpl(client, id, title, elements);
    }

    public static @NotNull TextElementImpl buildTextElement(@NotNull JsonObject data, @NotNull TurtleClient client) throws NullPointerException, IllegalArgumentException, IllegalJsonException {
        Checks.nonNull(data, "JSON");

        long       id      = data.get(Keys.Turtle.ID).getAsLong();
        String     title   = getOptional(() -> data.get(Keys.Form.Element.TITLE).getAsString());
        FormatText content = client.getFormatter().newText(data.get(Keys.Form.TextElement.CONTENT).getAsString(), Format.TURTLE);

        return new TextElementImpl(client, id, title, content);
    }

    /* - MESSAGES - */

    public static @NotNull AttachmentImpl buildAttachment(@NotNull JsonObject data, @NotNull TurtleClient client) throws NullPointerException, IllegalArgumentException, IllegalJsonException {
        Checks.nonNull(data, "JSON");

        long id            = data.get(Keys.Turtle.ID).getAsLong();
        long snowflake     = data.get(Keys.Messages.Attachment.SNOWFLAKE).getAsLong();
        String url         = data.get(Keys.Messages.Attachment.URL).getAsString();
        String proxyUrl    = data.get(Keys.Messages.Attachment.PROXY_URL).getAsString();
        String fileName    = data.get(Keys.Messages.Attachment.FILE_NAME).getAsString();
        String contentType = data.get(Keys.Messages.Attachment.CONTENT_TYPE).getAsString();
        String description = data.get(Keys.Messages.Attachment.DESCRIPTION).getAsString();
        long size          = data.get(Keys.Messages.Attachment.SIZE).getAsLong();
        int height         = data.get(Keys.Messages.Attachment.HEIGHT).getAsInt();
        int width          = data.get(Keys.Messages.Attachment.WIDTH).getAsInt();
        boolean ephemeral  = data.get(Keys.Messages.Attachment.EPHEMERAL).getAsBoolean();

        return new AttachmentImpl(id, client, snowflake, url, proxyUrl, fileName, contentType, description, size, height, width, ephemeral);
    }

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

    public static @NotNull SyncMessageImpl buildSyncMessage(@NotNull JsonObject data, @NotNull TurtleClient client) throws NullPointerException, IllegalArgumentException, IllegalJsonException {
        Checks.nonNull(data, "JSON");

        long       id        = data.get(Keys.Turtle.ID).getAsLong();
        long       author    = data.get(Keys.Messages.SyncMessage.AUTHOR).getAsLong();
        FormatText content   = client.getFormatter().newText(data.get(Keys.Messages.SyncMessage.CONTENT).getAsString(), Format.TURTLE);
        Long       reference = getOptional(() -> data.get(Keys.Messages.SyncMessage.REFERENCE).getAsLong());
        long       channel   = data.get(Keys.Messages.SyncMessage.CHANNEL).getAsLong();
        long       source    = data.get(Keys.Messages.SyncMessage.SOURCE).getAsLong();

        JsonArray attachmentArr = data.getAsJsonArray(Keys.Messages.SyncMessage.ATTACHMENTS);
        ArrayList<Long> attachments = new ArrayList<>();
        for (JsonElement element : attachmentArr)
            attachments.add(element.getAsLong());

        return new SyncMessageImpl(client, id, author, content, reference, channel, source, attachments);
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
