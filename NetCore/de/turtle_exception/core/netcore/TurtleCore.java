package de.turtle_exception.core.netcore;

import de.turtle_exception.core.netcore.net.route.RouteManager;
import org.jetbrains.annotations.NotNull;

public abstract class TurtleCore {
    protected RouteManager routeManager;

    protected TurtleCore() {
        this.routeManager = new RouteManager();
    }

    public @NotNull RouteManager getRouteManager() {
        return this.routeManager;
    }
}
