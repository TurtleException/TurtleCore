package de.turtle_exception.client.internal.entities;

import com.google.gson.JsonArray;
import de.turtle_exception.client.api.TurtleClient;
import de.turtle_exception.client.api.entities.User;
import de.turtle_exception.client.api.request.Action;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UserImpl extends TurtleImpl implements User {
    private String name;

    private final ArrayList<Long> discord;
    private final ArrayList<UUID> minecraft;

    UserImpl(@NotNull TurtleClient client, long id, String name, ArrayList<Long> discord, ArrayList<UUID> minecraft) {
        super(client, id);
        this.name = name;

        this.discord   = discord;
        this.minecraft = minecraft;
    }

    @Override
    public @NotNull Action<User> update() {
        return getClient().getProvider().get(this.getClass(), getId()).andThenParse(User.class);
    }

    /* - NAME - */

    @Override
    public @NotNull String getName() {
        return this.name;
    }

    // TODO: use this (route finalizer)
    public void setName(@NotNull String name) {
        this.name = name;
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
        JsonArray arr = new JsonArray();
        for (Long aDiscordId : discord)
            arr.add(aDiscordId);
        arr.add(discordId);
        return getClient().getProvider().patch(this, "discord", arr).andThenParse(User.class);
    }

    @Override
    public @NotNull Action<User> removeDiscordId(long discordId) {
        JsonArray arr = new JsonArray();
        for (Long aDiscordId : discord)
            if (!aDiscordId.equals(discordId))
                arr.add(aDiscordId);
        return getClient().getProvider().patch(this, "discord", arr).andThenParse(User.class);
    }

    /* - MINECRAFT - */

    @Override
    public @NotNull List<UUID> getMinecraftIds() {
        return List.copyOf(minecraft);
    }

    @Override
    public @NotNull Action<User> addMinecraftId(@NotNull UUID minecraftId) {
        JsonArray arr = new JsonArray();
        for (UUID aMinecraftId : minecraft)
            arr.add(String.valueOf(aMinecraftId));
        arr.add(String.valueOf(minecraftId));
        return getClient().getProvider().patch(this, "minecraft", arr).andThenParse(User.class);
    }

    @Override
    public @NotNull Action<User> removeMinecraftId(@NotNull UUID minecraftId) {
        JsonArray arr = new JsonArray();
        for (UUID aMinecraftId : minecraft)
            if (!aMinecraftId.equals(minecraftId))
                arr.add(String.valueOf(aMinecraftId));
        return getClient().getProvider().patch(this, "minecraft", arr).andThenParse(User.class);
    }
}
