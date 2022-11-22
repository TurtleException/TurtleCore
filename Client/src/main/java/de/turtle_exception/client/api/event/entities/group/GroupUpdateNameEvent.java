package de.turtle_exception.client.api.event.entities.group;

import de.turtle_exception.client.api.entities.Group;
import de.turtle_exception.client.internal.data.annotations.Keys;
import org.jetbrains.annotations.NotNull;

public class GroupUpdateNameEvent extends GroupUpdateEvent<String> {
    public GroupUpdateNameEvent(@NotNull Group group, @NotNull String oldName, @NotNull String newName) {
        super(group, Keys.Group.NAME, oldName, newName);
    }
}
