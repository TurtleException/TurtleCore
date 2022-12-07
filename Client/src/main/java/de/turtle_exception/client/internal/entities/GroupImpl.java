package de.turtle_exception.client.internal.entities;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import de.turtle_exception.client.internal.data.annotations.Keys;
import de.turtle_exception.client.api.TurtleClient;
import de.turtle_exception.client.api.entities.Group;
import de.turtle_exception.client.api.entities.User;
import de.turtle_exception.client.api.event.entities.group.GroupUpdateNameEvent;
import de.turtle_exception.client.api.request.Action;
import de.turtle_exception.client.internal.event.UpdateHelper;
import de.turtle_exception.client.internal.util.TurtleSet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class GroupImpl extends TurtleImpl implements Group {
    private String name;
    private ArrayList<Long> users;

    GroupImpl(@NotNull TurtleClient client, long id, String name, ArrayList<Long> users) {
        super(client, id);
        this.name = name;
        this.users = users;
    }

    @Override
    public synchronized @NotNull GroupImpl handleUpdate(@NotNull JsonObject json) {
        this.apply(json, Keys.Group.NAME, element -> {
            String old = this.name;
            this.name = element.getAsString();
            this.fireEvent(new GroupUpdateNameEvent(this, old, this.name));
        });
        this.apply(json, Keys.Group.MEMBERS, element -> {
            ArrayList<Long> old  = this.users;
            ArrayList<Long> list = new ArrayList<>();
            for (JsonElement entry : element.getAsJsonArray())
                list.add(entry.getAsLong());
            this.users = list;
            UpdateHelper.ofGroupMembers(this, old, list);
        });
        return this;
    }

    @Override
    public @Nullable User getTurtleById(long id) {
        return this.getClient().getTurtleById(id, User.class);
    }

    /* - NAME - */

    @Override
    public @NotNull String getName() {
        return this.name;
    }

    @Override
    public @NotNull Action<Group> modifyName(@NotNull String name) {
        return getClient().getProvider().patch(this, Keys.Group.NAME, name).andThenParse(Group.class);
    }

    /* - MEMBERS - */

    @Override
    public @NotNull List<Long> getUserIds() {
        return List.copyOf(users);
    }

    @Override
    public @NotNull Action<Group> addUser(long user) {
        return getClient().getProvider().patchEntryAdd(this, Keys.Group.MEMBERS, user).andThenParse(Group.class);
    }

    @Override
    public @NotNull Action<Group> removeUser(long user) {
        return getClient().getProvider().patchEntryDel(this, Keys.Group.MEMBERS, user).andThenParse(Group.class);
    }
}
