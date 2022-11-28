package de.turtle_exception.client.internal.request.actions;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import de.turtle_exception.client.api.entities.User;
import de.turtle_exception.client.api.request.UserAction;
import de.turtle_exception.client.internal.Provider;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

public class UserActionImpl extends EntityAction<User> implements UserAction {
    protected String name;
    protected ArrayList<Long> discord = new ArrayList<>();
    protected ArrayList<UUID> minecraft = new ArrayList<>();

    @SuppressWarnings({"CodeBlock2Expr", "ResultOfMethodCallIgnored"})
    public UserActionImpl(@NotNull Provider provider) {
        super(provider, User.class);

        this.checks.add(json -> { json.get("name").getAsString(); });
        this.checks.add(json -> {
            JsonArray arr = json.get("discord").getAsJsonArray();
            for (JsonElement entry : arr)
                entry.getAsLong();
        });
        this.checks.add(json -> {
            JsonArray arr = json.get("minecraft").getAsJsonArray();
            for (JsonElement entry : arr)
                UUID.fromString(entry.getAsString());
        });
    }

    @Override
    protected void updateContent() {
        this.content = new JsonObject();
        this.content.addProperty("name", name);

        JsonArray discord = new JsonArray();
        for (Long id : this.discord)
            discord.add(id);
        this.content.add("discord", discord);

        JsonArray minecraft = new JsonArray();
        for (UUID id : this.minecraft)
            minecraft.add(id.toString());
        this.content.add("minecraft", minecraft);
    }

    /* - - - */

    @Override
    public UserAction setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public UserAction setDiscordIds(@NotNull Collection<Long> ids) {
        this.discord = new ArrayList<>(ids);
        return this;
    }

    @Override
    public UserAction addDiscordId(long id) {
        this.discord.add(id);
        return this;
    }

    @Override
    public UserAction removeDiscordId(long id) {
        this.discord.remove(id);
        return this;
    }

    @Override
    public UserAction setMinecraftIds(@NotNull Collection<UUID> ids) {
        this.minecraft = new ArrayList<>(ids);
        return this;
    }

    @Override
    public UserAction addMinecraftId(UUID id) {
        this.minecraft.add(id);
        return this;
    }

    @Override
    public UserAction removeMinecraftId(UUID id) {
        this.minecraft.remove(id);
        return this;
    }
}
