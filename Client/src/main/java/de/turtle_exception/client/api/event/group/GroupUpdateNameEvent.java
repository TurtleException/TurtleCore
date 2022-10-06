package de.turtle_exception.client.api.event.group;

import de.turtle_exception.client.api.entities.Group;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public class GroupUpdateNameEvent extends GroupEvent {
    private final String oldName;
    private final String newName;

    public GroupUpdateNameEvent(@NotNull Group group, @NotNull String oldName, @NotNull String newName) {
        super(group);
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
