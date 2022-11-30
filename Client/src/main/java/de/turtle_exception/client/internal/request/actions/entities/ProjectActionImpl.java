package de.turtle_exception.client.internal.request.actions.entities;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import de.turtle_exception.client.api.entities.Group;
import de.turtle_exception.client.api.entities.Project;
import de.turtle_exception.client.api.entities.attributes.ProjectState;
import de.turtle_exception.client.api.request.entities.GroupAction;
import de.turtle_exception.client.api.request.entities.ProjectAction;
import de.turtle_exception.client.internal.Provider;
import de.turtle_exception.client.internal.request.actions.EntityAction;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;

public class ProjectActionImpl extends EntityAction<Project> implements ProjectAction {
    protected String title;
    protected String code;
    protected ProjectState state;
    protected ArrayList<Long> users = new ArrayList<>();

    @SuppressWarnings("CodeBlock2Expr")
    public ProjectActionImpl(@NotNull Provider provider) {
        super(provider, Project.class);

        this.checks.add(json -> { json.get("title").getAsString(); });
        this.checks.add(json -> { json.get("code").getAsString(); });
        this.checks.add(json -> { ProjectState.of(json.get("state").getAsByte()); });
        this.checks.add(json -> {
            JsonArray arr = json.get("users").getAsJsonArray();
            for (JsonElement entry : arr)
                entry.getAsLong();
        });
    }

    @Override
    protected void updateContent() {
        this.content = new JsonObject();
        this.content.addProperty("title", title);
        this.content.addProperty("code", code);
        this.content.addProperty("state", state.getCode());

        JsonArray arr = new JsonArray();
        for (Long user : this.users)
            arr.add(user);
        this.content.add("users", arr);
    }

    /* - - - */

    @Override
    public ProjectAction setTitle(String title) {
        this.title = title;
        return this;
    }

    @Override
    public ProjectAction setCode(String code) {
        this.code = code;
        return this;
    }

    @Override
    public ProjectAction setState(ProjectState state) {
        this.state = state;
        return this;
    }

    @Override
    public ProjectAction setUserIds(@NotNull Collection<Long> users) {
        this.users = new ArrayList<>(users);
        return this;
    }

    @Override
    public ProjectAction addUserId(long user) {
        this.users.add(user);
        return this;
    }

    @Override
    public ProjectAction removeUserId(long user) {
        this.users.remove(user);
        return this;
    }
}
