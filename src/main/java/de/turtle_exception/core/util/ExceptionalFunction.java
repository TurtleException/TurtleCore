package de.turtle_exception.core.util;

@FunctionalInterface
@SuppressWarnings("unused")
public interface ExceptionalFunction<T, R, E extends Exception> {
    R apply(T t) throws E;
}
