package de.turtle_exception.client.internal.request.actions.entities;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import de.turtle_exception.client.api.entities.Group;
import de.turtle_exception.client.api.request.entities.GroupAction;
import de.turtle_exception.client.internal.Provider;
import de.turtle_exception.client.internal.data.annotations.Keys;
import de.turtle_exception.client.internal.request.actions.EntityAction;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;

public class GroupActionImpl extends EntityAction<Group> implements GroupAction {
    protected String name;
    protected ArrayList<Long> users = new ArrayList<>();

    @SuppressWarnings("CodeBlock2Expr")
    public GroupActionImpl(@NotNull Provider provider) {
        super(provider, Group.class);

        this.checks.add(json -> { json.get(Keys.Group.NAME).getAsString(); });
        this.checks.add(json -> {
            JsonArray arr = json.get(Keys.Group.MEMBERS).getAsJsonArray();
            for (JsonElement entry : arr)
                entry.getAsLong();
        });
    }

    @Override
    protected void updateContent() {
        this.content = new JsonObject();
        this.content.addProperty(Keys.Group.NAME, name);

        JsonArray arr = new JsonArray();
        for (Long user : this.users)
            arr.add(user);
        this.content.add(Keys.Group.MEMBERS, arr);
    }

    /* - - - */

    @Override
    public GroupAction setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public GroupAction setUserIds(@NotNull Collection<Long> users) {
        this.users = new ArrayList<>(users);
        return this;
    }

    @Override
    public GroupAction addUserId(long user) {
        this.users.add(user);
        return this;
    }

    @Override
    public GroupAction removeUseId(long user) {
        this.users.remove(user);
        return this;
    }
}
