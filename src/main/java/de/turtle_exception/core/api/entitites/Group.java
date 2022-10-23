package de.turtle_exception.core.api.entitites;

import de.turtle_exception.core.internal.data.annotations.Key;
import de.turtle_exception.core.internal.data.annotations.Reference;
import de.turtle_exception.core.internal.data.annotations.Relation;
import de.turtle_exception.core.internal.data.annotations.Resource;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@Resource(name = "groups")
public interface Group extends Turtle {
    @Key(name = "name")
    @NotNull String getName();

    @Reference(name = "group_users", relation = Relation.MANY_TO_MANY)
    @NotNull List<User> getUsers();
}
