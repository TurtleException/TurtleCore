package de.turtle_exception.core.client.internal.entities;

import de.turtle_exception.core.client.api.entities.Group;
import de.turtle_exception.core.client.api.net.Action;
import org.jetbrains.annotations.NotNull;

public class GroupImpl implements Group {
    private final long id;

    private String name;

    GroupImpl(long id, String name) {
        this.id = id;
        this.name = name;
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
        // TODO
    }

    public void setName(@NotNull String name) {
        this.name = name;
    }
}
