package de.turtle_exception.client.internal.util.function;

@FunctionalInterface
public interface ExceptionalFunction<T, R> {
    R apply(T t) throws Exception;
}
