package de.turtle_exception.client.api.request.entities;

import de.turtle_exception.client.api.entities.Project;
import de.turtle_exception.client.api.entities.User;
import de.turtle_exception.client.api.entities.attributes.ProjectState;
import de.turtle_exception.client.api.request.Action;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collection;

public interface ProjectAction extends Action<Project> {
    ProjectAction setTitle(String title);

    ProjectAction setCode(String code);

    ProjectAction setState(ProjectState state);

    ProjectAction setUserIds(@NotNull Collection<Long> users);

    default ProjectAction setUsers(@NotNull Collection<User> users) {
        return this.setUserIds(users.stream().map(User::getId).toList());
    }

    default ProjectAction setUserIds(@NotNull Long... users) {
        return this.setUserIds(Arrays.asList(users));
    }

    default ProjectAction setUsers(@NotNull User... users) {
        return this.setUsers(Arrays.asList(users));
    }

    ProjectAction addUserId(long user);

    default ProjectAction addUser(@NotNull User user) {
        return this.addUserId(user.getId());
    }

    ProjectAction removeUserId(long user);

    default ProjectAction removeUser(@NotNull User user) {
        return this.removeUserId(user.getId());
    }
}
