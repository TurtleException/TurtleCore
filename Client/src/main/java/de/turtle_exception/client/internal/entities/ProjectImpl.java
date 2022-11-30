package de.turtle_exception.client.internal.entities;

import com.google.gson.JsonObject;
import de.turtle_exception.client.api.TurtleClient;
import de.turtle_exception.client.api.entities.Project;
import de.turtle_exception.client.api.entities.User;
import de.turtle_exception.client.api.entities.attributes.ProjectState;
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

    /* - - - */

    @Override
    public @Nullable String getTitle() {
        return this.title;
    }

    @Override
    public @NotNull String getCode() {
        return this.code;
    }

    @Override
    public @NotNull ProjectState getState() {
        return this.state;
    }

    @Override
    public @NotNull List<User> getUsers() {
        return List.copyOf(this.users);
    }

    @Override
    public @Nullable User getUserById(long id) {
        return this.users.get(id);
    }
}
