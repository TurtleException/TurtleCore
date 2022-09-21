package de.turtle_exception.core;

import org.jetbrains.annotations.Nullable;

public class TurtleServer extends TurtleCore {
    public TurtleServer(@Nullable String name) {
        super(name);
    }

    public TurtleServer() {
        this(null);
    }
}
