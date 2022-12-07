package de.turtle_exception.client.internal.request.actions.entities;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import de.turtle_exception.client.api.entities.Project;
import de.turtle_exception.client.api.entities.attributes.ProjectState;
import de.turtle_exception.client.api.request.entities.ProjectAction;
import de.turtle_exception.client.internal.Provider;
import de.turtle_exception.client.internal.data.annotations.Keys;
import de.turtle_exception.client.internal.request.actions.EntityAction;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;

public class ProjectActionImpl extends EntityAction<Project> implements ProjectAction {
    protected String title;
    protected String code;
    protected ProjectState state;
    protected ArrayList<Long> users = new ArrayList<>();
    protected Long applicationForm;
    protected Long timeRelease;
    protected Long timeApply;
    protected Long timeStart;
    protected Long timeEnd;

    @SuppressWarnings("CodeBlock2Expr")
    public ProjectActionImpl(@NotNull Provider provider) {
        super(provider, Project.class);

        this.checks.add(json -> { json.get(Keys.Project.TITLE).getAsString(); });
        this.checks.add(json -> { json.get(Keys.Project.CODE).getAsString(); });
        this.checks.add(json -> { json.get(Keys.Project.STATE).getAsByte(); });
        this.checks.add(json -> {
            JsonArray arr = json.get(Keys.Project.MEMBERS).getAsJsonArray();
            for (JsonElement entry : arr)
                entry.getAsLong();
        });
        this.checks.add(json -> { json.get(Keys.Project.APP_FORM).getAsLong(); });
        this.checks.add(json -> { json.get(Keys.Project.TIME_RELEASE).getAsLong(); });
        this.checks.add(json -> { json.get(Keys.Project.TIME_APPLY).getAsLong(); });
        this.checks.add(json -> { json.get(Keys.Project.TIME_START).getAsLong(); });
        this.checks.add(json -> { json.get(Keys.Project.TIME_END).getAsLong(); });
    }

    @Override
    protected void updateContent() {
        this.content = new JsonObject();
        this.content.addProperty(Keys.Project.TITLE, title);
        this.content.addProperty(Keys.Project.CODE, code);
        this.content.addProperty(Keys.Project.STATE, state.getCode());

        JsonArray arr = new JsonArray();
        for (Long user : this.users)
            arr.add(user);
        this.content.add(Keys.Project.MEMBERS, arr);

        this.content.addProperty(Keys.Project.APP_FORM, applicationForm);
        this.content.addProperty(Keys.Project.TIME_RELEASE, timeRelease);
        this.content.addProperty(Keys.Project.TIME_APPLY, timeApply);
        this.content.addProperty(Keys.Project.TIME_START, timeStart);
        this.content.addProperty(Keys.Project.TIME_END, timeEnd);
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

    @Override
    public ProjectAction setApplicationForm(long form) {
        this.applicationForm = form;
        return this;
    }

    @Override
    public ProjectAction setTimeRelease(long millis) {
        this.timeRelease = millis;
        return this;
    }

    @Override
    public ProjectAction setTimeApply(long millis) {
        this.timeApply = millis;
        return this;
    }

    @Override
    public ProjectAction setTimeStart(long millis) {
        this.timeStart = millis;
        return this;
    }

    @Override
    public ProjectAction setTimeEnd(long millis) {
        this.timeEnd = millis;
        return this;
    }


}
