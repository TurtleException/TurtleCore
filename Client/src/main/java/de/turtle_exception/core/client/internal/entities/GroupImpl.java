package de.turtle_exception.core.client.internal.entities;

import de.turtle_exception.core.client.api.TurtleClient;
import de.turtle_exception.core.client.api.entities.Group;
import de.turtle_exception.core.client.api.requests.Action;
import de.turtle_exception.core.client.internal.ActionImpl;
import de.turtle_exception.core.core.net.route.Routes;
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
        return new ActionImpl<>(client, Routes.Content.Group.MOD_NAME.setContent(name), null);
    }

    public void setName(@NotNull String name) {
        this.name = name;
    }
}
