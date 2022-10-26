package de.turtle_exception.core.api.entitites;

import de.turtle_exception.core.internal.data.annotations.Key;
import de.turtle_exception.core.internal.data.annotations.Relation;
import de.turtle_exception.core.internal.data.annotations.Resource;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@Resource(name = "groups")
@SuppressWarnings("unused")
public interface Group extends Turtle {
    @Key(name = "name")
    @NotNull String getName();

    @Key(name = "group_users", relation = Relation.MANY_TO_MANY)
    @NotNull List<User> getUsers();
}
