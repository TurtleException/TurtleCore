package de.turtle_exception.core.netcore.net.message;

import de.turtle_exception.core.netcore.TurtleCore;
import de.turtle_exception.core.netcore.net.route.Route;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class OutboundMessage extends Message {
    public OutboundMessage(@NotNull TurtleCore core, int callbackCode, @NotNull Route route, @Nullable String content, long deadline) {
        super(core, callbackCode, route, content, deadline);
    }

    public void handleResponse(@NotNull InboundMessage response) {

    }
}
