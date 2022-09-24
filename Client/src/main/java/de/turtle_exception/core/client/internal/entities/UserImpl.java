package de.turtle_exception.core.client.internal.entities;

import com.google.common.collect.Sets;
import de.turtle_exception.core.client.api.entities.Group;
import de.turtle_exception.core.client.api.entities.User;
import de.turtle_exception.core.client.api.net.Action;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Set;

public class UserImpl implements User {
    private final long id;

    private String name;

    private final Set<Group> groups = Sets.newConcurrentHashSet();

    UserImpl(long id, String name, Set<Group> groups) {
        this.id = id;
        this.name = name;

        this.groups.addAll(groups);
    }

    @Override
    public long getId() {
        return this.id;
    }

    @Override
    public @NotNull String getName() {
        return this.name;
    }

    public void setName(@NotNull String name) {
        this.name = name;
    }

    @Override
    public @NotNull Action<Void> modifyName(@NotNull String name) {
        // TODO
    }

    @Override
    public @NotNull List<Group> getGroups() {
        return List.copyOf(groups);
    }

    public @NotNull Set<Group> getGroupSet() {
        return this.groups;
    }

    @Override
    public @NotNull Action<Void> joinGroup(long groupId) {
        // TODO
    }

    @Override
    public @NotNull Action<Void> leaveGroup(long groupId) {
        // TODO
    }
}
