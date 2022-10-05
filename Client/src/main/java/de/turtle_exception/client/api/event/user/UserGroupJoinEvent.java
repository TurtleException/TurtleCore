package de.turtle_exception.client.api.event.user;

import de.turtle_exception.client.api.TurtleClient;
import de.turtle_exception.client.api.entities.Group;
import de.turtle_exception.client.api.entities.User;
import org.jetbrains.annotations.NotNull;

public class UserGroupJoinEvent extends UserEvent {
    protected final Group group;

    public UserGroupJoinEvent(@NotNull TurtleClient client, @NotNull User user, @NotNull Group group) {
        super(client, user);
        this.group = group;
    }

    public @NotNull Group getGroup() {
        return group;
    }
}
