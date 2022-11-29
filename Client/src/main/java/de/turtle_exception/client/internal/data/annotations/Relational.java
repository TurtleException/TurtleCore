package de.turtle_exception.client.internal.data.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** This annotation must always be combined with a {@link Key} annotation. */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Relational {
    /** Name of a relational table */
    String table();

    /** Self-referencing name in a relational table */
    String self();

    /** Foreign name in a relational table */
    String foreign();

    /** Generic content of a marked collection. */
    Class<?> type();
}
