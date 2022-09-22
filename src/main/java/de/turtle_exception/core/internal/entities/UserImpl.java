package de.turtle_exception.core.internal.entities;

import de.turtle_exception.core.api.entities.User;

public class UserImpl implements User {
    private final long id;

    UserImpl(long id) {
        this.id = id;
    }

    @Override
    public long getId() {
        return this.id;
    }
}
