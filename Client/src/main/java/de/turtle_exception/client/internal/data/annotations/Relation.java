package de.turtle_exception.client.internal.data.annotations;

public enum Relation {
    /** A {@code 1:1} relationship. One unique resource is linked to one unique value. */
    ONE_TO_ONE(false),
    /** A {@code 1:n} relationship. One  resource is linked to multiple values exclusively. */
    ONE_TO_MANY(true),
    /** A {@code n:1} relationship. Multiple resources are linked to one value exclusively. */
    MANY_TO_ONE(false),
    /** A {@code n:m} relationship. Multiple resources are linked to multiple values. */
    MANY_TO_MANY(true),

    ;

    private final boolean ext;

    Relation(boolean ext) {
        this.ext = ext;
    }

    /** Returns true if this Relation requires an external table (relational table) */
    public boolean isExt() {
        return ext;
    }
}