package de.turtle_exception.client.internal.data.annotations;

import org.jetbrains.annotations.NotNull;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// TODO: docs
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Key {
    @NotNull String name();

    /** If this is not {@link Relation#ONE_TO_ONE} the annotation must be combined with a {@link Relational}. */
    @NotNull Relation relation() default Relation.ONE_TO_ONE;

    Class<?> type() default Object.class;

    /** String representation of the SQL data type. */
    String sqlType();
}