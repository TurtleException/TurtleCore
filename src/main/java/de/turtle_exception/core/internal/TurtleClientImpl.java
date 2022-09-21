package de.turtle_exception.core.internal;

import de.turtle_exception.core.api.TurtleClient;
import org.jetbrains.annotations.Nullable;

public class TurtleClientImpl extends TurtleCore implements TurtleClient {
    public TurtleClientImpl(@Nullable String name) {
        super(name);
    }

    public TurtleClientImpl() {
        this(null);
    }
}
