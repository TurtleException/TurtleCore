package de.turtle_exception.client.api.event.entities.project;

import de.turtle_exception.client.api.entities.Project;
import de.turtle_exception.client.api.entities.Turtle;
import de.turtle_exception.client.api.entities.User;
import de.turtle_exception.client.api.event.entities.EntityUpdateEntryEvent;
import de.turtle_exception.client.internal.data.annotations.Keys;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.function.Function;

public class ProjectMemberLeaveEvent extends ProjectEvent implements EntityUpdateEntryEvent<Project, User> {
    protected final User user;

    public ProjectMemberLeaveEvent(@NotNull Project project, @NotNull User user) {
        super(project);
        this.user = user;
    }

    public @NotNull User getUser() {
        return user;
    }

    /* - EntityUpdateEntryEvent - */

    @Override
    public final @NotNull String getKey() {
        return Keys.Project.MEMBERS;
    }

    @Override
    public final @NotNull Collection<User> getCollection() {
        return getProject().getUsers();
    }

    @Override
    public final @NotNull Function<User, Object> getMutator() {
        return Turtle::getId;
    }
}
