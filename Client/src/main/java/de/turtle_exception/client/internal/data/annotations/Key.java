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

    @NotNull Relation relation() default Relation.ONE_TO_ONE;

    @NotNull Class<?> type() default Object.class;

    /** String representation of the SQL data type. */
    @NotNull String sqlType() default "VARCHAR(255)";

    /** Name of a relational table */
    @NotNull String relationTable() default "";

    /** Self-referencing name in a relational table */
    @NotNull String relationName1() default "";

    /** Foreign name in a relational table */
    @NotNull String relationName2() default "";
}