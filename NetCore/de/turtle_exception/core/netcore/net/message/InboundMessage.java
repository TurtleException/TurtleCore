package de.turtle_exception.core.netcore.net.message;

import de.turtle_exception.core.netcore.TurtleCore;
import de.turtle_exception.core.netcore.net.route.Route;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class InboundMessage extends Message {
    public InboundMessage(@NotNull TurtleCore core, int callbackCode, @NotNull Route route, @Nullable String content, long timeout) {
        super(core, callbackCode, route, content, timeout);
    }

    public void handleResponse(@NotNull OutboundMessage response) {

    }
}
