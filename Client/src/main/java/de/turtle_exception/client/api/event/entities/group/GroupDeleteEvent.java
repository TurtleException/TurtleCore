package de.turtle_exception.client.api.event.entities.group;

import de.turtle_exception.client.api.entities.Group;
import de.turtle_exception.client.api.event.entities.EntityDeleteEvent;
import org.jetbrains.annotations.NotNull;

public class GroupDeleteEvent extends GroupEvent implements EntityDeleteEvent<Group> {
    public GroupDeleteEvent(@NotNull Group group) {
        super(group);
    }
}
