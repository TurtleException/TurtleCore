package de.turtle_exception.core.netcore;

import de.turtle_exception.core.netcore.net.route.RouteManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import java.util.concurrent.TimeUnit;

/**
 * The TurtleCore is the heart of both Client and Server. This class is used internally so that NetCore is able to work
 * with both the Client and the Server without a circular dependency.
 * <p>If you came across this class and don't intend to change the NetCore Module you can safely ignore it. If it is
 * a required parameter type you can simply provide your Client / Server implementation class.
 */
public abstract class TurtleCore {
    protected RouteManager routeManager;

    // default times for either in- or outbound messages
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
