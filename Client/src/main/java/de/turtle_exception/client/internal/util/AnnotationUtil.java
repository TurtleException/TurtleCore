package de.turtle_exception.client.internal.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.annotation.Annotation;
import java.util.LinkedList;
import java.util.List;

public class AnnotationUtil {
    private AnnotationUtil() { }

    // TODO: docs
    public static <T extends Annotation> @Nullable T getAnnotation(@NotNull Class<?> clazz, @NotNull Class<T> type) {
        T annotation = clazz.getAnnotation(type);
        if (annotation != null) return annotation;

        // check interfaces
        LinkedList<Class<?>> interfaces = new LinkedList<>(List.of(clazz.getInterfaces()));
        while (!interfaces.isEmpty()) {
            Class<?> i = interfaces.poll();

            T interfaceAnnotation = i.getAnnotation(type);
            if (interfaceAnnotation != null)
                return interfaceAnnotation;

            interfaces.addAll(List.of(i.getInterfaces()));
        }

        return null;
    }
}
