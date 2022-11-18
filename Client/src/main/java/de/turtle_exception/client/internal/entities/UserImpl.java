package de.turtle_exception.client.internal.entities;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import de.turtle_exception.client.api.TurtleClient;
import de.turtle_exception.client.api.entities.User;
import de.turtle_exception.client.api.event.user.UserUpdateNameEvent;
import de.turtle_exception.client.api.request.Action;
import de.turtle_exception.client.internal.event.UpdateHelper;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UserImpl extends TurtleImpl implements User {
    private String name;

    private ArrayList<Long> discord;
    private ArrayList<UUID> minecraft;

    UserImpl(@NotNull TurtleClient client, long id, String name, ArrayList<Long> discord, ArrayList<UUID> minecraft) {
        super(client, id);
        this.name = name;

        this.discord   = discord;
        this.minecraft = minecraft;
    }

    @Override
    public synchronized @NotNull UserImpl handleUpdate(@NotNull JsonObject json) {
        this.apply(json, "name", element -> {
            String old = this.name;
            this.name = element.getAsString();
            this.fireEvent(new UserUpdateNameEvent(this, old, this.name));
        });
        this.apply(json, "discord", element -> {
            ArrayList<Long> old = this.discord;
            ArrayList<Long> list = new ArrayList<>();
            for (JsonElement entry : element.getAsJsonArray())
                list.add(entry.getAsLong());
            this.discord = list;
            UpdateHelper.ofUserDiscord(this, old, list);
        });
        this.apply(json, "minecraft", element -> {
            ArrayList<UUID> old = this.minecraft;
            ArrayList<UUID> list = new ArrayList<>();
            for (JsonElement entry : element.getAsJsonArray())
                list.add(UUID.fromString(entry.getAsString()));
            this.minecraft = list;
            UpdateHelper.ofUserMinecraft(this, old, list);
        });
        return this;
    }

    /* - NAME - */

    @Override
    public @NotNull String getName() {
        return this.name;
    }

    @Override
    public @NotNull Action<User> modifyName(@NotNull String name) {
        return getClient().getProvider().patch(this, "name", name).andThenParse(User.class);
    }

    /* - DISCORD - */

    @Override
    public @NotNull List<Long> getDiscordIds() {
        return List.copyOf(discord);
    }

    @Override
    public @NotNull Action<User> addDiscordId(long discordId) {
        return getClient().getProvider().patchEntryAdd(this, "discord", discordId).andThenParse(User.class);
    }

    @Override
    public @NotNull Action<User> removeDiscordId(long discordId) {
        return getClient().getProvider().patchEntryDel(this, "discord", discordId).andThenParse(User.class);
    }

    /* - MINECRAFT - */

    @Override
    public @NotNull List<UUID> getMinecraftIds() {
        return List.copyOf(minecraft);
    }

    @Override
    public @NotNull Action<User> addMinecraftId(@NotNull UUID minecraftId) {
        return getClient().getProvider().patchEntryAdd(this, "minecraft", minecraftId.toString()).andThenParse(User.class);
    }

    @Override
    public @NotNull Action<User> removeMinecraftId(@NotNull UUID minecraftId) {
        return getClient().getProvider().patchEntryDel(this, "minecraft", minecraftId.toString()).andThenParse(User.class);
    }
}
