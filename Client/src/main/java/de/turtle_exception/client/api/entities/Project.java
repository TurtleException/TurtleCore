package de.turtle_exception.client.api.entities;

import de.turtle_exception.client.api.entities.attributes.ProjectState;
import de.turtle_exception.client.internal.data.annotations.Key;
import de.turtle_exception.client.internal.data.annotations.Relation;
import de.turtle_exception.client.internal.data.annotations.Resource;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@Resource(path = "projects", builder = "buildProject")
@SuppressWarnings("unused")
public interface Project extends Turtle {
    @Key(name = "title")
    @NotNull String getTitle();

    @Key(name = "code")
    @NotNull String getCode();

    @Key(name = "state")
    @NotNull ProjectState getState();

    @Key(name = "users", relation = Relation.MANY_TO_MANY, type = User.class)
    @NotNull List<User> getMembers();

    // TODO: times
}
