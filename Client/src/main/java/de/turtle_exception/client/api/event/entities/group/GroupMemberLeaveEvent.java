package de.turtle_exception.client.api.event.entities.group;

import de.turtle_exception.client.api.entities.Group;
import de.turtle_exception.client.api.entities.Turtle;
import de.turtle_exception.client.api.entities.User;
import de.turtle_exception.client.api.event.entities.EntityUpdateEntryEvent;
import de.turtle_exception.client.internal.data.annotations.Keys;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.function.Function;

public class GroupMemberLeaveEvent extends GroupEvent implements EntityUpdateEntryEvent<Group, Long> {
    protected final Long user;

    public GroupMemberLeaveEvent(@NotNull Group group, @NotNull Long user) {
        super(group);
        this.user = user;
    }

    public Long getUserId() {
        return user;
    }

    /* - EntityUpdateEntryEvent - */

    @Override
    public final @NotNull String getKey() {
        return Keys.Group.MEMBERS;
    }

    @Override
    public final @NotNull Collection<Long> getCollection() {
        return getGroup().getUserIds();
    }

    @Override
    public final @NotNull Function<Long, Object> getMutator() {
        return l -> l;
    }
}
