package de.turtle_exception.client.internal.entities;

import com.google.gson.JsonObject;
import de.turtle_exception.client.api.TurtleClient;
import de.turtle_exception.client.api.entities.User;
import de.turtle_exception.client.api.requests.Action;
import de.turtle_exception.client.internal.ActionImpl;
import de.turtle_exception.core.net.route.Routes;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UserImpl implements User {
    private final @NotNull TurtleClient client;
    private final long id;

    private String name;

    private ArrayList<Long> discord   = new ArrayList<>();
    private ArrayList<UUID> minecraft = new ArrayList<>();

    UserImpl(@NotNull TurtleClient client, long id, String name) {
        this.client = client;
        this.id = id;
        this.name = name;
    }

    @Override
    public @NotNull TurtleClient getClient() {
        return this.client;
    }

    @Override
    public long getId() {
        return this.id;
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
    public @NotNull Action<Void> modifyName(@NotNull String name) {
        JsonObject json = new JsonObject();
        json.addProperty("name", name);
        return new ActionImpl<>(client, Routes.User.MODIFY.compile(json, this.id), null);
    }

    /* - DISCORD - */

    @Override
    public @NotNull List<Long> getDiscordIds() {
        return List.copyOf(discord);
    }

    /** Provides the underlying (modifiable) set of Discord ids this user is associated with. */
    public @NotNull ArrayList<Long> getDiscordIdSet() {
        return discord;
    }

    /** Changes the underlying set of Discord ids this user is associated with. */
    public void setDiscordIdSet(@NotNull List<Long> list) {
        this.discord = new ArrayList<>(list);
    }

    @Override
    public @NotNull Action<Void> addDiscordId(long discordId) {
        return new ActionImpl<>(client, Routes.User.ADD_DISCORD.compile(null, this.id, discordId), null);
    }

    @Override
    public @NotNull Action<Void> removeDiscordId(long discordId) {
        return new ActionImpl<>(client, Routes.User.DEL_DISCORD.compile(null, this.id, discordId), null);
    }

    /* - MINECRAFT - */

    @Override
    public @NotNull List<UUID> getMinecraftIds() {
        return List.copyOf(minecraft);
    }

    /** Provides the underlying (modifiable) set of Minecraft ids this user is associated with. */
    public @NotNull ArrayList<UUID> getMinecraftIdSet() {
        return minecraft;
    }

    /** Changes the underlying set of Discord ids this user is associated with. */
    public void setMinecraftIdSet(@NotNull List<UUID> list) {
        this.minecraft = new ArrayList<>(list);
    }

    @Override
    public @NotNull Action<Void> addMinecraftId(@NotNull UUID minecraftId) {
        return new ActionImpl<>(client, Routes.User.ADD_MINECRAFT.compile(null, this.id, minecraftId), null);
    }

    @Override
    public @NotNull Action<Void> removeMinecraftId(@NotNull UUID minecraftId) {
        return new ActionImpl<>(client, Routes.User.DEL_MINECRAFT.compile(null, this.id, minecraftId), null);
    }
}
