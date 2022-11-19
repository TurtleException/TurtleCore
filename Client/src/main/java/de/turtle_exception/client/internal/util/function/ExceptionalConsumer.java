package de.turtle_exception.client.internal.util.function;

@FunctionalInterface
public interface ExceptionalConsumer<T> {
    void accept(T t) throws Exception;
}
