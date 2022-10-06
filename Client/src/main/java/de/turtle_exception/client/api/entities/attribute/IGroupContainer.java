package de.turtle_exception.client.api.entities.attribute;

import de.turtle_exception.client.api.entities.Group;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface IGroupContainer {
    @NotNull List<Group> getGroups();

    @Nullable Group getGroupById(long id);
}
