package de.turtle_exception.client.api.event.group;

import de.turtle_exception.client.api.entities.Group;
import de.turtle_exception.client.api.event.Event;
import org.jetbrains.annotations.NotNull;

public abstract class GroupEvent extends Event {
    protected final Group group;

    public GroupEvent(@NotNull Group group) {
        super(group.getClient());
        this.group = group;
    }

    public @NotNull Group getGroup() {
        return group;
    }
}
