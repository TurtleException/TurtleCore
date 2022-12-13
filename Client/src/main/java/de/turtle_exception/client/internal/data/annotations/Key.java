package de.turtle_exception.client.internal.data.annotations;

import de.turtle_exception.client.api.entities.Turtle;
import org.jetbrains.annotations.NotNull;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks an attribute (provided by a method) of a {@link Turtle} that has the {@link Resource} annotation.
 * <p> This annotation is used to reflectively building the necessary structure of the backing database and to
 * (de-)serialize resources from/to JSON objects.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Key {
    /**
     * The name of a key should be unique within a resource or any super- or sub-implementations of that resource. It is
     * an identifier that is used for (de-)serialization and is used to create the structure of the backing database.
     * @return Name of this key.
     */
    @NotNull String name();

    /** If this is not {@link Relation#ONE_TO_ONE} the annotation must be combined with a {@link Relational}. */
    @NotNull Relation relation() default Relation.ONE_TO_ONE;

    /** String representation of the SQL data type. */
    String sqlType();
}