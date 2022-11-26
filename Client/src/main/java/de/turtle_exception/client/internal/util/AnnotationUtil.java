package de.turtle_exception.client.internal.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.annotation.Annotation;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
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

        Class<?> i;
        while ((i = buffer.poll()) != null) {
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

    /**
     * This provides a specific {@link Annotation} of type {@code T} from an {@link AccessibleObject} that must either
     * be a {@link Field} or a {@link Method}, if present. If the AccessibleObject does not have the requested
     * annotation anywhere in its hierarchy (in case of a method), {@code null} is returned.
     * @param accObj A Field or Method that should have the annotation.
     * @param type The annotation class.
     * @return The requested annotation (may be {@code null}).
     * @param <T> Type of the annotation.
     * @throws IllegalArgumentException if the first argument is not a Field or Method
     * @see AnnotationUtil#getAnnotation(Method, Class)
     */
    public static <T extends Annotation> @Nullable T getAnnotation(@NotNull AccessibleObject accObj, @NotNull Class<T> type) throws IllegalArgumentException {
        if (accObj instanceof Method method)
            return getAnnotation(method, type);
        if (accObj instanceof Field field)
            return field.getAnnotation(type);
        throw new IllegalArgumentException("Unsupported AccessibleObject: " + accObj.getClass().getName());
    }

    /**
     * This provides a specific {@link Annotation} of type {@code T} from a {@link Method}, if present. If the Field
     * does not have the requested annotation anywhere in its hierarchy, {@code null} is returned.
     * <p> This method includes inherited annotations. While this is not technically a thing the idea behind it is that
     * an interface may have an annotated method, which is implemented by a class, from which the {@link Method} is
     * retrieved reflectively. This util then checks the hierarchy layer for layer, prioritizing superclasses over
     * interfaces and ordering interfaces as specified by {@link Class#getInterfaces()}. The first occurrence of the
     * requested annotation will be returned.
     * <p> Note that this approach only makes sense in very specific scenarios. The hierarchy of the provided Field or
     * Method should be taken into account to prevent confusing results.
     * @param method A Method that should have the annotation in its hierarchy.
     * @param type The annotation class.
     * @return The requested annotation (may be {@code null}).
     * @param <T> Type of the annotation.
     */
    public static <T extends Annotation> @Nullable T getAnnotation(@NotNull Method method, @NotNull Class<T> type) {
        LinkedList<Method> buffer = new LinkedList<>();
        buffer.add(method);

        Method i;
        while ((i = buffer.poll()) != null) {
            T annotation = i.getDeclaredAnnotation(type);
            if (annotation != null)
                return annotation;

            // add superclass (or skip if null)
            Class<?> superclass = i.getDeclaringClass().getSuperclass();
            if (superclass != null) {
                try {
                    buffer.add(superclass.getMethod(i.getName(), i.getParameterTypes()));
                } catch (NoSuchMethodException ignored) { }
            }

            // add interfaces
            for (Class<?> anInterface : i.getDeclaringClass().getInterfaces()) {
                try {
                    buffer.add(anInterface.getMethod(i.getName(), i.getParameterTypes()));
                } catch (NoSuchMethodException ignored) { }
            }
        }

        return null;
    }
}
