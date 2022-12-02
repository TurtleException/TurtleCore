package de.turtle_exception.client.internal.entities;

import com.google.gson.JsonObject;
import de.turtle_exception.client.api.TurtleClient;
import de.turtle_exception.client.api.entities.JsonResource;
import de.turtle_exception.client.api.entities.Project;
import de.turtle_exception.client.api.entities.User;
import de.turtle_exception.client.api.entities.attributes.ProjectState;
import de.turtle_exception.client.api.request.Action;
import de.turtle_exception.client.internal.data.annotations.Keys;
import de.turtle_exception.client.internal.util.TurtleSet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ProjectImpl extends TurtleImpl implements Project {
    private String title;
    private String code;
    private ProjectState state;

    private final TurtleSet<User> users;

    protected ProjectImpl(@NotNull TurtleClient client, long id, String title, String code, ProjectState state, TurtleSet<User> users) {
        super(client, id);

        this.title = title;
        this.code  = code;
        this.state = state;

        this.users = users;
    }

    @Override
    public @NotNull ProjectImpl handleUpdate(@NotNull JsonObject json) {
        // TODO
        return null;
    }

    /* - TITLE - */

    @Override
    public @Nullable String getTitle() {
        return this.title;
    }

    @Override
    public @NotNull Action<Project> modifyTitle(@Nullable String title) {
        return getClient().getProvider().patch(this, Keys.Project.TITLE, title).andThenParse(Project.class);
    }

    /* - CODE - */

    @Override
    public @NotNull String getCode() {
        return this.code;
    }

    @Override
    public @NotNull Action<Project> modifyCode(@NotNull String code) {
        return getClient().getProvider().patch(this, Keys.Project.CODE, code).andThenParse(Project.class);
    }

    /* - STATE - */

    @Override
    public @NotNull ProjectState getState() {
        return this.state;
    }

    @Override
    public @NotNull Action<Project> modifyState(@NotNull ProjectState state) {
        return getClient().getProvider().patch(this, Keys.Project.STATE, state).andThenParse(Project.class);
    }

    /* - USERS - */

    @Override
    public @NotNull List<User> getUsers() {
        return List.copyOf(this.users);
    }

    public @NotNull TurtleSet<User> getUserSet() {
        return users;
    }

    @Override
    public @Nullable User getUserById(long id) {
        return users.get(id);
    }

    @Override
    public @NotNull Action<Project> addUser(long user) {
        return getClient().getProvider().patchEntryAdd(this, Keys.Project.MEMBERS, user).andThenParse(Project.class);
    }

    @Override
    public @NotNull Action<Project> removeUser(long user) {
        return getClient().getProvider().patchEntryDel(this, Keys.Project.MEMBERS, user).andThenParse(Project.class);
    }
}
