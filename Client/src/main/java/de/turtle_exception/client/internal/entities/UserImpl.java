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
    private final TurtleClient client;
    private final long id;

    private String name;

    private final ArrayList<Long> discord;
    private final ArrayList<UUID> minecraft;

    UserImpl(TurtleClient client, long id, String name, ArrayList<Long> discord, ArrayList<UUID> minecraft) {
        this.client = client;
        this.id = id;
        this.name = name;

        this.discord   = discord;
        this.minecraft = minecraft;
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
        return new ActionImpl<Void>(client, Routes.User.MODIFY, null)
                .setRouteArgs(this.id)
                .setContent(json);
    }

    /* - DISCORD - */

    @Override
    public @NotNull List<Long> getDiscordIds() {
        return List.copyOf(discord);
    }

    @Override
    public @NotNull Action<Void> addDiscordId(long discordId) {
        return new ActionImpl<Void>(client, Routes.User.ADD_DISCORD, null)
                .setRouteArgs(this.id, discordId);
    }

    @Override
    public @NotNull Action<Void> removeDiscordId(long discordId) {
        return new ActionImpl<Void>(client, Routes.User.DEL_DISCORD, null)
                .setRouteArgs(this.id, discordId);
    }

    /* - MINECRAFT - */

    @Override
    public @NotNull List<UUID> getMinecraftIds() {
        return List.copyOf(minecraft);
    }

    @Override
    public @NotNull Action<Void> addMinecraftId(@NotNull UUID minecraftId) {
        return new ActionImpl<Void>(client, Routes.User.ADD_MINECRAFT, null)
                .setRouteArgs(this.id, minecraftId);
    }

    @Override
    public @NotNull Action<Void> removeMinecraftId(@NotNull UUID minecraftId) {
        return new ActionImpl<Void>(client, Routes.User.DEL_MINECRAFT, null)
                .setRouteArgs(this.id, minecraftId);
    }
}
