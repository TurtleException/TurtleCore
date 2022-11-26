package de.turtle_exception.client.api.event.entities.group;

import de.turtle_exception.client.api.entities.Group;
import de.turtle_exception.client.api.entities.Turtle;
import de.turtle_exception.client.api.entities.User;
import de.turtle_exception.client.api.event.entities.EntityUpdateEntryEvent;
import de.turtle_exception.client.internal.data.annotations.Keys;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.function.Function;

public class GroupMemberJoinEvent extends GroupEvent implements EntityUpdateEntryEvent<Group, User> {
    protected final User user;

    public GroupMemberJoinEvent(@NotNull Group group, @NotNull User user) {
        super(group);
        this.user = user;
    }

    public @NotNull User getUser() {
        return user;
    }

    /* - EntityUpdateEntryEvent - */

    @Override
    public final @NotNull String getKey() {
        return Keys.Group.MEMBERS;
    }

    @Override
    public final @NotNull Collection<User> getCollection() {
        return getGroup().getUsers();
    }

    @Override
    public final @NotNull Function<User, Object> getMutator() {
        return Turtle::getId;
    }
}
