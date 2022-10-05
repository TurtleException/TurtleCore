package de.turtle_exception.client.api.event.group;

import de.turtle_exception.client.api.TurtleClient;
import de.turtle_exception.client.api.entities.Group;
import de.turtle_exception.client.api.event.Event;
import org.jetbrains.annotations.NotNull;

public abstract class GroupEvent extends Event {
    protected final Group group;

    public GroupEvent(@NotNull TurtleClient client, @NotNull Group group) {
        super(client);
        this.group = group;
    }

    public @NotNull Group getGroup() {
        return group;
    }
}
