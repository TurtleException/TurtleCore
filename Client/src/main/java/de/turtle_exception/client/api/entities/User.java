package de.turtle_exception.client.api.entities;

import de.turtle_exception.client.api.requests.Action;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@SuppressWarnings("unused")
public interface User extends Turtle {
    @NotNull String getName();

    @NotNull Action<Void> modifyName(@NotNull String name);

    /* - GROUPS - */

    default @NotNull List<Group> getGroups() {
        ArrayList<Group> groups = new ArrayList<>();
        for (Group group : getClient().getGroups())
            if (group.getUserById(this.getId()) != null)
                groups.add(group);
        return List.copyOf(groups);
    }

    default @NotNull Action<Void> joinGroup(@NotNull Group group) {
        return group.addUser(this);
    }

    default @NotNull Action<Void> leaveGroup(@NotNull Group group) {
        return group.removeUser(this);
    }

    /* - DISCORD - */

    @NotNull List<Long> getDiscordIds();

    @NotNull Action<Void> addDiscordId(long discordId);

    @NotNull Action<Void> removeDiscordId(long discordId);

    /* - MINECRAFT - */

    @NotNull List<UUID> getMinecraftIds();

    @NotNull Action<Void> addMinecraftId(@NotNull UUID minecraftId);

    @NotNull Action<Void> removeMinecraftId(@NotNull UUID minecraftId);
}
