package de.turtle_exception.core.core;

import org.jetbrains.annotations.Range;

import java.util.concurrent.TimeUnit;

public abstract class TurtleCore {
    private long defaultTimeoutInbound  = TimeUnit.SECONDS.toMillis( 8);
    private long defaultTimeoutOutbound = TimeUnit.SECONDS.toMillis(16);

    protected TurtleCore() { }

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
