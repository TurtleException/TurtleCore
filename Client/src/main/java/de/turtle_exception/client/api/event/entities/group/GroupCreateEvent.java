package de.turtle_exception.client.api.event.entities.group;

import de.turtle_exception.client.api.entities.Group;
import de.turtle_exception.client.api.event.entities.EntityCreateEvent;
import org.jetbrains.annotations.NotNull;

public class GroupCreateEvent extends GroupEvent implements EntityCreateEvent<Group> {
    public GroupCreateEvent(@NotNull Group group) {
        super(group);
    }
}
