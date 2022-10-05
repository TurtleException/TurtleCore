package de.turtle_exception.client.api.entities.attribute;

import de.turtle_exception.client.api.entities.Group;
import org.jetbrains.annotations.Nullable;

public interface IGroupContainer {
    @Nullable Group getGroupById(long id);
}
