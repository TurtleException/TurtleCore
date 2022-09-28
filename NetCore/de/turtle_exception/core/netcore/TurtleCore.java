package de.turtle_exception.core.netcore;

import de.turtle_exception.core.netcore.net.route.RouteManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import java.util.concurrent.TimeUnit;

public abstract class TurtleCore {
    protected RouteManager routeManager;

    private long defaultTimeoutInbound  = TimeUnit.SECONDS.toMillis( 8);
    private long defaultTimeoutOutbound = TimeUnit.SECONDS.toMillis(16);

    protected TurtleCore() {
        this.routeManager = new RouteManager();
    }

    public @NotNull RouteManager getRouteManager() {
        return this.routeManager;
    }

    public @Range(from = 0, to = Long.MAX_VALUE) long getDefaultTimeoutInbound() {
        return defaultTimeoutInbound;
    }

    public void setDefaultTimeoutInbound(@Range(from = 0, to = Long.MAX_VALUE) long defaultTimeoutInbound) {
        this.defaultTimeoutInbound = defaultTimeoutInbound;
    }

    public @Range(from = 0, to = Long.MAX_VALUE) long getDefaultTimeoutOutbound() {
        return defaultTimeoutOutbound;
    }

    public void setDefaultTimeoutOutbound(@Range(from = 0, to = Long.MAX_VALUE) long defaultTimeoutOutbound) {
        this.defaultTimeoutOutbound = defaultTimeoutOutbound;
    }
}
