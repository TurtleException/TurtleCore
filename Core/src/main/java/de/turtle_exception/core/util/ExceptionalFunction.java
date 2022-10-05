package de.turtle_exception.core.util;

@FunctionalInterface
public interface ExceptionalFunction<T, R> {
    R apply(T t) throws Exception;
}
