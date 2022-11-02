package de.turtle_exception.core.api;

import de.turtle_exception.core.internal.TurtleCoreImpl;
import org.checkerframework.checker.nullness.qual.NonNull;

@SuppressWarnings("unused")
public class TurtleBuilder {
    // TODO: fields

    /* - - - */

    public TurtleBuilder() { }

    public @NonNull TurtleCore build() throws IllegalArgumentException {
        // TODO
        return new TurtleCoreImpl();
    }

    /* - - - */

    // TODO: setters
}
