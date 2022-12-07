package de.turtle_exception.client.api.request.entities;

import de.turtle_exception.client.api.TurtleClient;
import de.turtle_exception.client.api.entities.Project;
import de.turtle_exception.client.api.entities.User;
import de.turtle_exception.client.api.entities.attributes.ProjectState;
import de.turtle_exception.client.api.entities.form.TemplateForm;
import de.turtle_exception.client.api.request.Action;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;
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

    /**
     * Sets the application form id of this Project to the provided id.
     * @param form Project application form id.
     * @return This ProjectAction for chaining convenience.
     */
    ProjectAction setApplicationForm(long form);

    /**
     * Sets the application form of this Project to the provided TemplateForm.
     * @param form Project application form.
     * @return This ProjectAction for chaining convenience.
     */
    default ProjectAction setApplicationForm(@NotNull TemplateForm form) {
        return this.setApplicationForm(form.getId());
    }

    /**
     * Sets the RELEASE time of this Project to the provided unix timestamp (milliseconds).
     * @param millis Project RELEASE time as unix timestamp.
     * @return This ProjectAction for chaining convenience.
     */
    ProjectAction setTimeRelease(long millis);

    /**
     * Sets the RELEASE time of this Project to the provided Instant.
     * @param instant Project RELEASE time.
     * @return This ProjectAction for chaining convenience.
     */
    default ProjectAction setTimeRelease(@NotNull Instant instant) {
        return this.setTimeRelease(instant.toEpochMilli());
    }

    /**
     * Sets the APPLY time of this Project to the provided unix timestamp (milliseconds).
     * @param millis Project APPLY time as unix timestamp.
     * @return This ProjectAction for chaining convenience.
     */
    ProjectAction setTimeApply(long millis);

    /**
     * Sets the APPLY time of this Project to the provided Instant.
     * @param instant Project APPLY time.
     * @return This ProjectAction for chaining convenience.
     */
    default ProjectAction setTimeApply(@NotNull Instant instant) {
        return this.setTimeApply(instant.toEpochMilli());
    }

    /**
     * Sets the START time of this Project to the provided unix timestamp (milliseconds).
     * @param millis Project START time as unix timestamp.
     * @return This ProjectAction for chaining convenience.
     */
    ProjectAction setTimeStart(long millis);

    /**
     * Sets the START time of this Project to the provided Instant.
     * @param instant Project START time.
     * @return This ProjectAction for chaining convenience.
     */
    default ProjectAction setTimeStart(@NotNull Instant instant) {
        return this.setTimeStart(instant.toEpochMilli());
    }

    /**
     * Sets the END time of this Project to the provided unix timestamp (milliseconds).
     * @param millis Project END time as unix timestamp.
     * @return This ProjectAction for chaining convenience.
     */
    ProjectAction setTimeEnd(long millis);

    /**
     * Sets the END time of this Project to the provided Instant.
     * @param instant Project END time.
     * @return This ProjectAction for chaining convenience.
     */
    default ProjectAction setTimeEnd(@NotNull Instant instant) {
        return this.setTimeEnd(instant.toEpochMilli());
    }
}
