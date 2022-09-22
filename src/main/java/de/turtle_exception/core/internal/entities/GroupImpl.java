package de.turtle_exception.core.internal.entities;

import de.turtle_exception.core.api.entities.Group;

public class GroupImpl implements Group {
    private final long id;

    GroupImpl(long id) {
        this.id = id;
    }

    @Override
    public long getId() {
        return this.id;
    }
}
