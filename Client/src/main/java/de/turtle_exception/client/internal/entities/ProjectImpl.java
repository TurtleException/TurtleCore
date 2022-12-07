package de.turtle_exception.client.internal.entities;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import de.turtle_exception.client.api.TurtleClient;
import de.turtle_exception.client.api.entities.Project;
import de.turtle_exception.client.api.entities.User;
import de.turtle_exception.client.api.entities.attributes.ProjectState;
import de.turtle_exception.client.api.entities.form.TemplateForm;
import de.turtle_exception.client.api.event.entities.project.*;
import de.turtle_exception.client.api.request.Action;
import de.turtle_exception.client.internal.data.annotations.Keys;
import de.turtle_exception.client.internal.event.UpdateHelper;
import de.turtle_exception.client.internal.util.TurtleSet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ProjectImpl extends TurtleImpl implements Project {
    private String title;
    private String code;
    private ProjectState state;
    private TurtleSet<User> users;
    private TemplateForm applicationForm;
    private long timeRelease;
    private long timeApply;
    private long timeStart;
    private long timeEnd;

    protected ProjectImpl(@NotNull TurtleClient client, long id, String title, String code, ProjectState state, TurtleSet<User> users, TemplateForm applicationForm, long timeRelease, long timeApply, long timeStart, long timeEnd) {
        super(client, id);
        this.title = title;
        this.code  = code;
        this.state = state;
        this.users = users;
        this.applicationForm = applicationForm;
        this.timeRelease = timeRelease;
        this.timeApply   = timeApply;
        this.timeStart   = timeStart;
        this.timeEnd     = timeEnd;
    }

    @Override
    public @NotNull ProjectImpl handleUpdate(@NotNull JsonObject json) {
        this.apply(json, Keys.Project.TITLE, element -> {
            String old = this.title;
            this.title = element.getAsString();
            this.fireEvent(new ProjectUpdateTitleEvent(this, old, this.title));
        });
        this.apply(json, Keys.Project.CODE, element -> {
            String old = this.code;
            this.code = element.getAsString();
            this.fireEvent(new ProjectUpdateCodeEvent(this, old, this.code));
        });
        this.apply(json, Keys.Project.STATE, element -> {
            ProjectState old = this.state;
            this.state = ProjectState.of(element.getAsByte());
            this.fireEvent(new ProjectUpdateStateEvent(this, old, this.state));
        });
        this.apply(json, Keys.Project.MEMBERS, element -> {
            TurtleSet<User> old = this.users;
            TurtleSet<User> set = new TurtleSet<>();
            for (JsonElement entry : element.getAsJsonArray())
                set.add(client.getTurtleById(entry.getAsLong(), User.class));
            this.users = set;
            UpdateHelper.ofProjectMembers(this, old, set);
        });
        this.apply(json, Keys.Project.APP_FORM, element -> {
            TemplateForm old = this.applicationForm;
            this.applicationForm = this.getClient().getTurtleById(element.getAsLong(), TemplateForm.class);
            this.fireEvent(new ProjectUpdateApplicationFormEvent(this, old, this.applicationForm));
        });
        this.apply(json, Keys.Project.TIME_RELEASE, element -> {
            long old = this.timeRelease;
            this.timeRelease = element.getAsLong();
            this.fireEvent(new ProjectUpdateTimeReleaseEvent(this, old ,this.timeRelease));
        });
        this.apply(json, Keys.Project.TIME_APPLY, element -> {
            long old = this.timeApply;
            this.timeApply = element.getAsLong();
            this.fireEvent(new ProjectUpdateTimeApplyEvent(this, old ,this.timeApply));
        });
        this.apply(json, Keys.Project.TIME_START, element -> {
            long old = this.timeStart;
            this.timeStart = element.getAsLong();
            this.fireEvent(new ProjectUpdateTimeStartEvent(this, old ,this.timeStart));
        });
        this.apply(json, Keys.Project.TIME_END, element -> {
            long old = this.timeEnd;
            this.timeEnd = element.getAsLong();
            this.fireEvent(new ProjectUpdateTimeEndEvent(this, old ,this.timeEnd));
        });
        return this;
    }

    @Override
    public @Nullable User getTurtleById(long id) {
        return this.users.get(id);
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

    @Override
    public @NotNull Action<Project> addUser(long user) {
        return getClient().getProvider().patchEntryAdd(this, Keys.Project.MEMBERS, user).andThenParse(Project.class);
    }

    @Override
    public @NotNull Action<Project> removeUser(long user) {
        return getClient().getProvider().patchEntryDel(this, Keys.Project.MEMBERS, user).andThenParse(Project.class);
    }

    /* - APPLICATIONS - */

    @Override
    public @NotNull TemplateForm getApplicationForm() {
        return this.applicationForm;
    }

    @Override
    public @NotNull Action<Project> modifyApplicationForm(long form) {
        return getClient().getProvider().patch(this, Keys.Project.APP_FORM, form).andThenParse(Project.class);
    }

    /* - TIMES - */

    @Override
    public long getMilliTimeRelease() {
        return this.timeRelease;
    }

    @Override
    public @NotNull Action<Project> modifyTimeRelease(long millis) {
        return getClient().getProvider().patch(this, Keys.Project.TIME_RELEASE, millis).andThenParse(Project.class);
    }

    @Override
    public long getMilliTimeApply() {
        return this.timeApply;
    }

    @Override
    public @NotNull Action<Project> modifyTimeApply(long millis) {
        return getClient().getProvider().patch(this, Keys.Project.TIME_APPLY, millis).andThenParse(Project.class);
    }

    @Override
    public long getMilliTimeStart() {
        return this.timeStart;
    }

    @Override
    public @NotNull Action<Project> modifyTimeStart(long millis) {
        return getClient().getProvider().patch(this, Keys.Project.TIME_START, millis).andThenParse(Project.class);
    }

    @Override
    public long getMilliTimeEnd() {
        return timeEnd;
    }

    @Override
    public @NotNull Action<Project> modifyTimeEnd(long millis) {
        return getClient().getProvider().patch(this, Keys.Project.TIME_END, millis).andThenParse(Project.class);
    }
}
