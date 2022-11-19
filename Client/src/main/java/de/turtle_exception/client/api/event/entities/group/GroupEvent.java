package de.turtle_exception.client.api.event.entities.group;

import de.turtle_exception.client.api.entities.Group;
import de.turtle_exception.client.api.event.entities.EntityEvent;
import org.jetbrains.annotations.NotNull;

public abstract class GroupEvent extends EntityEvent<Group> {
    public GroupEvent(@NotNull Group entity) {
        super(entity);
    }

    public @NotNull Group getGroup() {
        return this.getEntity();
    }
}
