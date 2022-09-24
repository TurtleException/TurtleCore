package de.turtle_exception.core.netcore.net;

import de.turtle_exception.core.netcore.util.AsyncLoopThread;
import de.turtle_exception.core.netcore.util.logging.NestedLogger;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;

// TODO: docs
public abstract class NetworkAdapter {
    protected final CallbackRegistrar callbackRegistrar = new CallbackRegistrar();

    public final NestedLogger logger;

    // using 2 different executors for basically the same tasks to prevent blocking requests (outbound awaiting response)
    protected RequestExecutor outboundExecutor;
    protected RequestExecutor  inboundExecutor;

    protected AsyncLoopThread receiver;

    protected NetworkAdapter(NestedLogger logger) {
        this.logger = logger;
    }

    /* - - - */

    public abstract void start() throws IOException;

    public abstract void stop() throws IOException;

    /* - - - */

    protected final void prepareExecutors() {
        this.outboundExecutor = new RequestExecutor((r, executor) -> logger.log(Level.WARNING, "Outbound RequestExecutor rejected task: " + r.toString()));
        this.inboundExecutor  = new RequestExecutor((r, executor) -> logger.log(Level.WARNING,  "Inbound RequestExecutor rejected task: " + r.toString()));
    }

    protected final void stopReceiver() {
        this.receiver.interrupt();
        this.receiver = null;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    protected final void awaitExecutorShutdown() {
        // await remaining requests
        inboundExecutor.shutdown();
        while (!inboundExecutor.isTerminated()) { }
        outboundExecutor.shutdown();
        while (!outboundExecutor.isTerminated()) { }
    }

    /* - - - */

    public abstract <T> @NotNull CompletableFuture<T> submitOutbound(@NotNull NetworkTask<T> action) throws RejectedExecutionException, TimeoutException;

    public abstract <T> void submitInbound(@NotNull NetworkTask<T> action);

    public abstract boolean isStopped();
}
