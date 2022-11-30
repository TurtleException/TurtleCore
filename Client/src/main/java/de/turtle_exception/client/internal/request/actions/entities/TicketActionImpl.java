package de.turtle_exception.client.internal.request.actions.entities;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import de.turtle_exception.client.api.entities.Ticket;
import de.turtle_exception.client.api.entities.attributes.TicketState;
import de.turtle_exception.client.api.request.entities.TicketAction;
import de.turtle_exception.client.internal.Provider;
import de.turtle_exception.client.internal.request.actions.EntityAction;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;

public class TicketActionImpl extends EntityAction<Ticket> implements TicketAction {
    protected TicketState state = TicketState.UNDEFINED;
    protected String title;
    protected String category;
    protected ArrayList<String> tags = new ArrayList<>();
    protected Long discordChannel;
    protected ArrayList<Long> users = new ArrayList<>();

    @SuppressWarnings("CodeBlock2Expr")
    public TicketActionImpl(@NotNull Provider provider) {
        super(provider, Ticket.class);

        this.checks.add(json -> { TicketState.of(json.get("state").getAsByte()); });
        this.checks.add(json -> { json.get("title").getAsString(); });
        this.checks.add(json -> { json.get("category").getAsString(); });
        this.checks.add(json -> {
            JsonArray arr = json.get("tags").getAsJsonArray();
            for (JsonElement entry : arr)
                entry.getAsString();
        });
        this.checks.add(json -> { json.get("discord_channel").getAsLong(); });
        this.checks.add(json -> {
            JsonArray arr = json.get("users").getAsJsonArray();
            for (JsonElement entry : arr)
                entry.getAsLong();
        });
    }

    @Override
    protected void updateContent() {
        this.content = new JsonObject();
        this.content.addProperty("state", state.getCode());
        this.content.addProperty("title", title);
        this.content.addProperty("category", category);

        JsonArray tags = new JsonArray();
        for (String tag : this.tags)
            tags.add(tag);
        this.content.add("tags", tags);

        this.content.addProperty("discord_channel", discordChannel);

        JsonArray users = new JsonArray();
        for (Long user : this.users)
            users.add(user);
        this.content.add("users", users);
    }

    /* - - - */

    @Override
    public TicketAction setState(TicketState state) {
        this.state = state;
        return this;
    }

    @Override
    public TicketAction setTitle(String title) {
        this.title = title;
        return this;
    }

    @Override
    public TicketAction setCategory(String category) {
        this.category = category;
        return this;
    }

    @Override
    public TicketAction setTags(@NotNull Collection<String> tags) {
        this.tags = new ArrayList<>(tags);
        return this;
    }

    @Override
    public TicketAction addTag(String tag) {
        this.tags.add(tag);
        return this;
    }

    @Override
    public TicketAction removeTag(String tag) {
        this.tags.remove(tag);
        return this;
    }

    @Override
    public TicketAction setDiscordChannelId(long channel) {
        this.discordChannel = channel;
        return this;
    }

    @Override
    public TicketAction setUserIds(@NotNull Collection<Long> users) {
        this.users = new ArrayList<>(users);
        return this;
    }

    @Override
    public TicketAction addUser(long user) {
        this.users.add(user);
        return this;
    }

    @Override
    public TicketAction removeUser(long user) {
        this.users.remove(user);
        return this;
    }
}
