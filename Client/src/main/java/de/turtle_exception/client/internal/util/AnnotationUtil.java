package de.turtle_exception.client.internal.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.annotation.Annotation;
import java.lang.reflect.AccessibleObject;
import java.util.LinkedList;
import java.util.List;

public class AnnotationUtil {
    private AnnotationUtil() { }

    /**
     * Iterates through the entire Class hierarchy of {@code clazz} and returns the first occurrence of an annotation of
     * the specified type.
     * <p> Classes are added to the buffer in BFS-order (Breadth-first search), starting with the direct superclass (if
     * present) and all implemented interfaces in the order specified by {@link Class#getInterfaces()}.
     * @param clazz The Class object to retrieve the annotation from.
     * @param type The Class object corresponding to the annotation type.
     * @return The first occurrence of an annotation of the specified type.
     * @param <T> The type of the annotation to query for and return.
     */
    public static <T extends Annotation> @Nullable T getAnnotation(@NotNull Class<?> clazz, @NotNull Class<T> type) {
        LinkedList<Class<?>> buffer = new LinkedList<>();
        buffer.add(clazz);

        while (!buffer.isEmpty()) {
            Class<?> i = buffer.poll();

            T annotation = i.getDeclaredAnnotation(type);
            if (annotation != null)
                return annotation;

            // add superclass (or skip if null)
            Class<?> superclass = i.getSuperclass();
            if (superclass != null)
                buffer.add(superclass);

            // add interfaces
            buffer.addAll(List.of(i.getInterfaces()));
        }

        return null;
    }

    // TODO: docs
    public static <T extends Annotation> @Nullable T getAnnotation(@NotNull Class<?> clazz, @NotNull AccessibleObject accObj, @NotNull Class<T> type) {
        // TODO
    }
}
