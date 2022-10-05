package de.turtle_exception.client.internal.entities;

import com.google.gson.JsonObject;
import de.turtle_exception.client.api.TurtleClient;
import de.turtle_exception.client.api.entities.Group;
import de.turtle_exception.client.api.requests.Action;
import de.turtle_exception.client.internal.ActionImpl;
import de.turtle_exception.core.net.route.Routes;
import org.jetbrains.annotations.NotNull;

public class GroupImpl implements Group {
    private final @NotNull TurtleClient client;
    private final long id;

    private String name;

    GroupImpl(@NotNull TurtleClient client, long id, String name) {
        this.client = client;
        this.id = id;
        this.name = name;
    }

    @Override
    public @NotNull TurtleClient getClient() {
        return this.client;
    }

    @Override
    public long getId() {
        return this.id;
    }

    @Override
    public @NotNull String getName() {
        return this.name;
    }

    @Override
    public @NotNull Action<Void> modifyName(@NotNull String name) {
        JsonObject json = new JsonObject();
        json.addProperty("name", name);
        return new ActionImpl<>(client, Routes.Group.MODIFY.compile(json, this.id), null);
    }

    public void setName(@NotNull String name) {
        this.name = name;
    }
}
