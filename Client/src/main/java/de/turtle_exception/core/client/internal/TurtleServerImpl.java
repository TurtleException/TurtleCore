package de.turtle_exception.core.client.internal;

import de.turtle_exception.core.api.TurtleServer;
import org.jetbrains.annotations.Nullable;

public class TurtleServerImpl extends TurtleCore implements TurtleServer {
    public TurtleServerImpl(@Nullable String name) {
        super(name);
    }

    public TurtleServerImpl() {
        this(null);
    }
}
