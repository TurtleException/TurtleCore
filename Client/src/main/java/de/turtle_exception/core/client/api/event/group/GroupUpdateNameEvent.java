package de.turtle_exception.core.client.api.event.group;

import de.turtle_exception.core.client.api.TurtleClient;
import de.turtle_exception.core.client.api.entities.Group;
import org.jetbrains.annotations.NotNull;

public class GroupUpdateNameEvent extends GroupEvent {
    private final String oldName;
    private final String newName;

    public GroupUpdateNameEvent(@NotNull TurtleClient client, @NotNull Group group, @NotNull String oldName, @NotNull String newName) {
        super(client, group);
        this.oldName = oldName;
        this.newName = newName;
    }

    public @NotNull String getOldName() {
        return oldName;
    }

    public @NotNull String getNewName() {
        return newName;
    }
}
