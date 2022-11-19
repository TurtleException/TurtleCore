package de.turtle_exception.client.internal;

import de.turtle_exception.client.api.request.Action;
import de.turtle_exception.client.internal.util.function.ExceptionalFunction;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.Callable;

public class ProxyAction<F, T> extends ActionImpl<T> {
    protected final @NotNull ActionImpl<F> initialAction;
    protected final @NotNull ExceptionalFunction<F, T> function;

    public ProxyAction(@NotNull ActionImpl<F> initialAction, @NotNull ExceptionalFunction<F, T> function) {
        super(initialAction.getProvider());

        this.initialAction = initialAction;
        this.function = function;
    }

    private static int getChainLength(ProxyAction<?, ?> action, int i) {
        i++;
        if (action.getInitialAction() instanceof ProxyAction<?,?> pAction)
            return getChainLength(pAction, i);
        return i;
    }

    public @NotNull Action<F> getInitialAction() {
        return initialAction;
    }

    /** This includes the action itself */
    public int getChainLength() {
        return getChainLength(this, 1);
    }

    /* - - - */

    @Override
    protected @NotNull Callable<T> asCallable() throws IllegalStateException {
        return () -> {
            F result = initialAction.asCallable().call();
            return function.apply(result);
        };
    }
}
