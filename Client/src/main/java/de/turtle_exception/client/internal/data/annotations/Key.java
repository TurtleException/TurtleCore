package de.turtle_exception.client.internal.data.annotations;

import org.jetbrains.annotations.NotNull;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface Key {
    @NotNull String name();

    boolean primary() default false;

    @NotNull Relation relation() default Relation.ONE_TO_ONE;

    @NotNull String table() default "";

    // foreign key (column in reference table)
    @NotNull String fKey() default "";

    @NotNull Class<?> type() default Object.class;
}