package de.turtle_exception.client.api.event.group;

import de.turtle_exception.client.api.entities.Group;
import org.jetbrains.annotations.NotNull;

public class GroupCreateEvent extends GroupEvent {
    public GroupCreateEvent(@NotNull Group group) {
        super(group);
    }
}
