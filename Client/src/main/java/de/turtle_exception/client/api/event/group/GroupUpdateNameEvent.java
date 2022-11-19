package de.turtle_exception.client.api.event.group;

import de.turtle_exception.client.api.entities.Group;
import org.jetbrains.annotations.NotNull;

public class GroupUpdateNameEvent extends GroupUpdateEvent<String> {
    public GroupUpdateNameEvent(@NotNull Group group, @NotNull String oldName, @NotNull String newName) {
        super(group, "name", oldName, newName);
    }
}
