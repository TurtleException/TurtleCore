package de.turtle_exception.client.api.entities;

import de.turtle_exception.client.api.entities.attributes.ProjectState;
import de.turtle_exception.client.internal.data.annotations.Key;
import de.turtle_exception.client.internal.data.annotations.Relation;
import de.turtle_exception.client.internal.data.annotations.Relational;
import de.turtle_exception.client.internal.data.annotations.Resource;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@Resource(path = "projects", builder = "buildProject")
@SuppressWarnings("unused")
public interface Project extends Turtle {
    @Key(name = "title", sqlType = "TINYTEXT")
    @Nullable String getTitle();

    @Key(name = "code", sqlType = "TINYTEXT")
    @NotNull String getCode();

    @NotNull ProjectState getState();

    @Key(name = "state", sqlType = "TINYINT")
    default byte getStateCode() {
        return this.getState().getCode();
    }

    @Key(name = "users", relation = Relation.MANY_TO_MANY, type = User.class, sqlType = "TURTLE")
    @Relational(table = "project_members", self = "project", foreign = "user")
    @NotNull List<User> getMembers();

    // TODO: times
}
