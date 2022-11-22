package de.turtle_exception.client.internal.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.annotation.Annotation;
import java.lang.reflect.AccessibleObject;
import java.util.LinkedList;
import java.util.List;

public class AnnotationUtil {
    private AnnotationUtil() { }

    // TODO: docs
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
