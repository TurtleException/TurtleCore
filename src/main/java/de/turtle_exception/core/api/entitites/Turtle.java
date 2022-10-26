package de.turtle_exception.core.api.entitites;

import de.turtle_exception.core.internal.data.annotations.Key;

@SuppressWarnings("unused")
public interface Turtle {
    @Key(name = "id", primary = true)
    long getId();
}
