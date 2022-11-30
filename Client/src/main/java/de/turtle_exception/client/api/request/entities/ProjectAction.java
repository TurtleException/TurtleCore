package de.turtle_exception.client.api.request.entities;

import de.turtle_exception.client.api.TurtleClient;
import de.turtle_exception.client.api.entities.Project;
import de.turtle_exception.client.api.entities.User;
import de.turtle_exception.client.api.entities.attributes.ProjectState;
import de.turtle_exception.client.api.request.Action;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collection;

/**
 * A ProjectAction is an Action that requests the creation of a new {@link Project}, according to the arguments this
 * Action holds. If any required field is missing the server will reject the request and respond with an error. Required
 * fields are all attributes that are not a subclass of {@link Collection}, as these are set to an empty Collection by
 * default.
 * @see TurtleClient#createProject()
 */
@SuppressWarnings("unused")
public interface ProjectAction extends Action<Project> {
    /**
     * Sets the title of this Project to the provided String.
     * @param title Project title.
     * @return This ProjectAction for chaining convenience.
     */
    ProjectAction setTitle(String title);

    /**
     * Sets the code of this Project to the provided String.
     * @param code Project code.
     * @return This ProjectAction for chaining convenience.
     */
    ProjectAction setCode(String code);

    /**
     * Sets the state of this Project to the provided ProjectState.
     * @param state Project state.
     * @return This ProjectAction for chaining convenience.
     */
    ProjectAction setState(ProjectState state);

    /**
     * Sets the List of ids that each represent a {@link User} as members of this Project.
     * The existing List will be overwritten.
     * @param users Collection of User ids.
     * @return This ProjectAction for chaining convenience.
     */
    ProjectAction setUserIds(@NotNull Collection<Long> users);

    /**
     * Sets the List of {@link User Users} that are members of this Project.
     * The existing List will be overwritten.
     * @param users Collection of Users.
     * @return This ProjectAction for chaining convenience.
     */
    default ProjectAction setUsers(@NotNull Collection<User> users) {
        return this.setUserIds(users.stream().map(User::getId).toList());
    }

    /**
     * Sets the List of ids that each represent a {@link User} as members of this Project.
     * The existing List will be overwritten.
     * @param users Array of User ids.
     * @return This ProjectAction for chaining convenience.
     */
    default ProjectAction setUserIds(@NotNull Long... users) {
        return this.setUserIds(Arrays.asList(users));
    }

    /**
     * Sets the List of {@link User Users} that are members of this Project.
     * The existing List will be overwritten.
     * @param users Array of Users.
     * @return This ProjectAction for chaining convenience.
     */
    default ProjectAction setUsers(@NotNull User... users) {
        return this.setUsers(Arrays.asList(users));
    }

    /**
     * Adds the provided {@code long} to the List of ids that each represent a {@link User} as a member of this Project.
     * @param user User id.
     * @return This ProjectAction for chaining convenience.
     */
    ProjectAction addUserId(long user);

    /**
     * Adds the provided {@link User} to the List Users that are members of this Project.
     * @param user Some User.
     * @return This ProjectAction for chaining convenience.
     */
    default ProjectAction addUser(@NotNull User user) {
        return this.addUserId(user.getId());
    }

    /**
     * Removes the provided {@code long} from the List of ids that each represent a {@link User} as a member of this Project.
     * @param user User id.
     * @return This ProjectAction for chaining convenience.
     */
    ProjectAction removeUserId(long user);

    /**
     * Removes the provided {@link User} from the List Users that are members of this Project.
     * @param user Some User.
     * @return This ProjectAction for chaining convenience.
     */
    default ProjectAction removeUser(@NotNull User user) {
        return this.removeUserId(user.getId());
    }
}
