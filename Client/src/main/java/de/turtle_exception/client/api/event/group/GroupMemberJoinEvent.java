package de.turtle_exception.client.api.event.group;

import de.turtle_exception.client.api.entities.Group;
import de.turtle_exception.client.api.entities.User;
import org.jetbrains.annotations.NotNull;

public class GroupMemberJoinEvent extends GroupEvent {
    protected final User user;

    public GroupMemberJoinEvent(@NotNull Group group, @NotNull User user) {
        super(group);
        this.user = user;
    }

    public @NotNull User getUser() {
        return user;
    }
}
