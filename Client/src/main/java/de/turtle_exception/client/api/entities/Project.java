package de.turtle_exception.client.api.entities;

import de.turtle_exception.client.api.entities.attributes.ProjectState;
import de.turtle_exception.client.api.entities.containers.TurtleContainer;
import de.turtle_exception.client.api.entities.form.CompletedForm;
import de.turtle_exception.client.api.entities.form.TemplateForm;
import de.turtle_exception.client.api.request.Action;
import de.turtle_exception.client.internal.data.annotations.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * A Project is an Event within a community where users play Minecraft together. Each project has its unique attributes
 * and conditions that influence that gameplay.
 */
@Resource(path = "projects", builder = "buildProject")
@SuppressWarnings("unused")
public interface Project extends Turtle, TurtleContainer<User> {
    @Override
    default @NotNull Action<Project> update() {
        return this.getClient().retrieveTurtle(this.getId(), Project.class);
    }

    /* - TITLE - */

    /**
     * Provides the title of this Project. Project titles are not guaranteed to be unique and rather function as a
     * description. Uniqueness can only be checked by {@link Group#getId()}.
     * @return The Project title.
     */
    @Key(name = Keys.Project.TITLE, sqlType = Types.Project.TITLE)
    @Nullable String getTitle();

    /**
     * Creates an Action with the instruction to modify this Project's title and change it to the provided String.
     * @param title New Project title.
     * @return Action that provides the modified {@link Project} on completion.
     */
    @NotNull Action<Project> modifyTitle(@Nullable String title);

    /* - CODE - */

    /**
     * Provides the unique code of this Project. Project codes should be a short (and unique) representation of one
     * specific Project.
     * @return The Project code.
     */
    @Key(name = Keys.Project.CODE, sqlType = Types.Project.CODE)
    @NotNull String getCode();

    /**
     * Creates an Action with the instruction to modify this Project's code and change it to the provided String.
     * @param code New Project code.
     * @return Action that provides the modified {@link Project} on completion.
     */
    @NotNull Action<Project> modifyCode(@NotNull String code);

    /* - STATE - */

    /**
     * Provides the state of this Project. See {@link ProjectState} documentation for more information.
     * @return The Project state.
     */
    @NotNull ProjectState getState();

    /**
     * Creates an Action with the instruction to modify this Project's state and change it to the provided ProjectState.
     * @param state New Project state.
     * @return Action that provides the modified {@link Project} on completion.
     */
    @NotNull Action<Project> modifyState(@NotNull ProjectState state);

    /**
     * Provides the state of this Project in its {@code byte} representation. This method exists mainly for serialization
     * purposes, as it is a rather inefficient shortcut for {@code Project.getState().getCode()}.
     * @return The Project state as a {@code byte}.
     */
    @Key(name = Keys.Project.STATE, sqlType = Types.Project.STATE)
    default byte getStateCode() {
        return this.getState().getCode();
    }

    /* - USERS - */

    @Override
    default @NotNull List<User> getTurtles() {
        return this.getUsers();
    }

    /**
     * Provides a List of all {@link User Users} that are a member of this Project.
     * <p> A Project can have multiple Users; A User can also be part of multiple Projects.
     * @return List of members.
     */
    @Key(name = Keys.Project.MEMBERS, relation = Relation.MANY_TO_MANY, sqlType = Types.Project.MEMBERS)
    @Relational(table = "project_members", self = "project", foreign = "user", type = User.class)
    @NotNull List<User> getUsers();

    /**
     * Creates an Action with the instruction to add the provided id to the list of Project members.
     * <p> The provided {@code long} should be a representation of a {@link User} id.
     * @param user Turtle ID of a User.
     * @return Action that provides the modified {@link Project} on completion.
     */
    @NotNull Action<Project> addUser(long user);

    /**
     * Creates an Action with the instruction to add the provided {@link User} to the list of Project members.
     * <p> This is a shortcut for {@code Project.addUser(user.getId())}.
     * @param user A User.
     * @return Action that provides the modified {@link Project} on completion.
     */
    default @NotNull Action<Project> addUser(@NotNull User user) {
        return this.addUser(user.getId());
    }

    /**
     * Creates an Action with the instruction to remove the provided id from the list of Project members.
     * <p> The provided {@code long} should be a representation of a {@link User} id.
     * @param user Turtle ID of a User.
     * @return Action that provides the modified {@link Project} on completion.
     */
    @NotNull Action<Project> removeUser(long user);

    /**
     * Creates an Action with the instruction to remove the provided {@link User} from the list of Project members.
     * <p> This is a shortcut for {@code Project.removeUser(user.getId())}.
     * @param user A User.
     * @return Action that provides the modified {@link Project} on completion.
     */
    default @NotNull Action<Project> removeUser(@NotNull User user) {
        return this.removeUser(user.getId());
    }

    /* - APPLICATIONS - */

    /**
     * Provides the application form of this Project.
     * @return The Project application form.
     */
    @Key(name = "application_form", sqlType = "TURTLE")
    @NotNull TemplateForm getApplicationForm();

    /**
     * Creates an Action with the instruction to modify this Project's application form id and change it to the provided id.
     * @param form New application form id.
     * @return Action that provides the modified {@link Project} on completion.
     */
    @NotNull Action<Project> modifyApplicationForm(long form);

    /**
     * Creates an Action with the instruction to modify this Project's application form and change it to the provided
     * TemplateForm.
     * @param form New application form.
     * @return Action that provides the modified {@link Project} on completion.
     */
    default @NotNull Action<Project> modifyApplicationForm(@NotNull TemplateForm form) {
        return this.modifyApplicationForm(form.getId());
    }

    default @NotNull List<CompletedForm> getApplicationRequests() {
        return this.getClient().getTurtles(CompletedForm.class).stream()
                .filter(form -> form.getForm().getId() == this.getApplicationForm().getId())
                .toList();
    }

    /* - TIMES - */

    // TODO: times
}
