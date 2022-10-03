package de.turtle_exception.core.client.internal.entities;

import com.google.common.collect.Sets;
import com.google.gson.JsonArray;
import de.turtle_exception.core.client.api.TurtleClient;
import de.turtle_exception.core.client.api.entities.Group;
import de.turtle_exception.core.client.api.entities.User;
import de.turtle_exception.core.client.api.requests.Action;
import de.turtle_exception.core.client.internal.ActionImpl;
import de.turtle_exception.core.core.net.route.Routes;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class UserImpl implements User {
    private final @NotNull TurtleClient client;
    private final long id;

    private String name;

    private final Set<Group> groups = Sets.newConcurrentHashSet();
    private ArrayList<Long> discord   = new ArrayList<>();
    private ArrayList<UUID> minecraft = new ArrayList<>();

    UserImpl(@NotNull TurtleClient client, long id, String name, Set<Group> groups) {
        this.client = client;
        this.id = id;
        this.name = name;

        this.groups.addAll(groups);
    }

    @Override
    public @NotNull TurtleClient getClient() {
        return this.client;
    }

    @Override
    public long getId() {
        return this.id;
    }

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
        return new ActionImpl<>(client, Routes.Content.User.MOD_NAME.setContent(getId() + " " + name), null);
    }

    @Override
    public @NotNull List<Group> getGroups() {
        return List.copyOf(groups);
    }

    // TODO: use this (route finalizer)
    /** Provides the underlying (modifiable) set of {@link Group Groups} this user is a part of. */
    public @NotNull Set<Group> getGroupSet() {
        return this.groups;
    }

    @Override
    public @NotNull Action<Void> joinGroup(long groupId) {
        return new ActionImpl<>(client, Routes.Content.User.GROUP_JOIN.setContent(getId() + " " + groupId), null);
    }

    @Override
    public @NotNull Action<Void> leaveGroup(long groupId) {
        return new ActionImpl<>(client, Routes.Content.User.GROUP_LEAVE.setContent(getId() + " " + groupId), null);
    }

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
        JsonArray json = new JsonArray();
        for (Long aLong : discord)
            json.add(aLong);
        json.add(discordId);

        return new ActionImpl<>(client, Routes.Content.User.DISCORD_SET.setContent(json.getAsString()), null);
    }

    @Override
    public @NotNull Action<Void> removeDiscordId(long discordId) {
        JsonArray json = new JsonArray();
        for (Long aLong : discord)
            if (!aLong.equals(discordId))
                json.add(aLong);

        return new ActionImpl<>(client, Routes.Content.User.DISCORD_SET.setContent(json.getAsString()), null);
    }

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
        JsonArray json = new JsonArray();
        for (UUID uuid : minecraft)
            json.add(uuid.toString());
        json.add(minecraftId.toString());

        return new ActionImpl<>(client, Routes.Content.User.MINECRAFT_SET.setContent(json.getAsString()), null);
    }

    @Override
    public @NotNull Action<Void> removeMinecraftId(@NotNull UUID minecraftId) {
        JsonArray json = new JsonArray();
        for (UUID uuid : minecraft)
            if (!uuid.equals(minecraftId))
                json.add(uuid.toString());

        return new ActionImpl<>(client, Routes.Content.User.MINECRAFT_SET.setContent(json.getAsString()), null);
    }
}
