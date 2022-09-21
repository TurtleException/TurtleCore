package de.turtle_exception.core;

import org.jetbrains.annotations.Nullable;

public class TurtleClient extends TurtleCore {
    public TurtleClient(@Nullable String name) {
        super(name);
    }

    public TurtleClient() {
        this(null);
    }
}
