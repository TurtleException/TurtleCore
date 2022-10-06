package de.turtle_exception.client.api.event.group;

import de.turtle_exception.client.api.entities.Group;
import org.jetbrains.annotations.NotNull;

public class GroupDeleteEvent extends GroupEvent {
    public GroupDeleteEvent(@NotNull Group group) {
        super(group);
    }
}
