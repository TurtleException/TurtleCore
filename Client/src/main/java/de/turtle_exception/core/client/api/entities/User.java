package de.turtle_exception.core.client.api.entities;

import de.turtle_exception.core.client.api.requests.Action;
import org.jetbrains.annotations.NotNull;

import java.util.List;

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
}
