package de.turtle_exception.core.client.api.entities;

import de.turtle_exception.core.client.api.requests.Action;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;

public interface User extends Turtle {
    @NotNull String getName();

    @NotNull Action<Void> modifyName(@NotNull String name);

    @NotNull List<Group> getGroups();

    @NotNull Action<Void> joinGroup(long groupId);

    default @NotNull Action<Void> joinGroup(@NotNull Group group) {
        return this.joinGroup(group.getId());
    }

    @NotNull Action<Void> leaveGroup(long groupId);

    default @NotNull Action<Void> leaveGroup(@NotNull Group group) {
        return this.leaveGroup(group.getId());
    }

    @NotNull List<Long> getDiscordIds();

    @NotNull Action<Void> addDiscordId(long discordId);

    @NotNull Action<Void> removeDiscordId(long discordId);

    @NotNull List<UUID> getMinecraftIds();

    @NotNull Action<Void> addMinecraftId(@NotNull UUID minecraftId);

    @NotNull Action<Void> removeMinecraftId(@NotNull UUID minecraftId);
}
