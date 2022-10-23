package de.turtle_exception.core.api.entitites;

import de.turtle_exception.core.internal.data.annotations.Key;

public interface Turtle {
    @Key(name = "id", primary = true)
    long getId();
}
