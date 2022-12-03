package de.turtle_exception.client.api.entities;

import de.turtle_exception.client.api.entities.attributes.ProjectState;
import de.turtle_exception.client.api.entities.containers.TurtleContainer;
import de.turtle_exception.client.api.request.Action;
import de.turtle_exception.client.internal.data.annotations.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@Resource(path = "projects", builder = "buildProject")
@SuppressWarnings("unused")
public interface Project extends Turtle, TurtleContainer<User> {
    @Override
    @NotNull
    default Action<Project> update() {
        return this.getClient().retrieveProject(this.getId());
    }

    @Key(name = Keys.Project.TITLE, sqlType = Types.Project.TITLE)
    @Nullable String getTitle();

    @NotNull Action<Project> modifyTitle(@Nullable String title);

    @Key(name = Keys.Project.CODE, sqlType = Types.Project.CODE)
    @NotNull String getCode();

    @NotNull Action<Project> modifyCode(@NotNull String code);

    @NotNull ProjectState getState();

    @NotNull Action<Project> modifyState(@NotNull ProjectState state);

    @Key(name = Keys.Project.STATE, sqlType = Types.Project.STATE)
    default byte getStateCode() {
        return this.getState().getCode();
    }

    @Override
    default @NotNull List<User> getTurtles() {
        return this.getUsers();
    }

    @Key(name = Keys.Project.MEMBERS, relation = Relation.MANY_TO_MANY, sqlType = Types.Project.MEMBERS)
    @Relational(table = "project_members", self = "project", foreign = "user", type = User.class)
    @NotNull List<User> getUsers();

    @NotNull Action<Project> addUser(long user);

    default @NotNull Action<Project> addUser(@NotNull User user) {
        return this.addUser(user.getId());
    }

    @NotNull Action<Project> removeUser(long user);

    default @NotNull Action<Project> removeUser(@NotNull User user) {
        return this.removeUser(user.getId());
    }

    // TODO: times

    @Resource(path = "project_application_forms", builder = "buildProjectApplicationForm")
    interface ApplicationForm extends Turtle {
        @Key(name = Keys.Project.ApplicationForm.PROJECT, sqlType = Types.Project.ApplicationForm.PROJECT)
        @NotNull Project getProject();

        // TODO: queries + datatypes
    }

    @Resource(path = "project_applications", builder = "buildProjectApplication")
    interface Application extends Turtle {
        @Key(name = Keys.Project.Application.PROJECT, sqlType = Types.Project.Application.PROJECT)
        @NotNull Project getProject();

        @Key(name = Keys.Project.Application.USER, sqlType = Types.Project.Application.USER)
        @NotNull User getUser();

        // TODO: times

        // TODO: content (queries)
    }
}
