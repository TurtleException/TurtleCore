package de.turtle_exception.core.client.internal.entities;

import com.google.common.collect.Sets;
import de.turtle_exception.core.client.api.TurtleClient;
import de.turtle_exception.core.client.api.entities.Group;
import de.turtle_exception.core.client.api.entities.User;
import de.turtle_exception.core.client.api.requests.Action;
import de.turtle_exception.core.client.internal.ActionImpl;
import de.turtle_exception.core.netcore.net.route.Routes;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Set;

public class UserImpl implements User {
    private final @NotNull TurtleClient client;
    private final long id;

    private String name;

    private final Set<Group> groups = Sets.newConcurrentHashSet();

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

    public void setName(@NotNull String name) {
        this.name = name;
    }

    @Override
    public @NotNull Action<Void> modifyName(@NotNull String name) {
        return new ActionImpl<>(client, Routes.Content.User.MODIFY_NAME.setContent(name), null);
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
        return new ActionImpl<>(client, Routes.Content.User.GROUP_JOIN.setContent(String.valueOf(groupId)), null);
    }

    @Override
    public @NotNull Action<Void> leaveGroup(long groupId) {
        return new ActionImpl<>(client, Routes.Content.User.GROUP_LEAVE.setContent(String.valueOf(groupId)), null);
    }
}
