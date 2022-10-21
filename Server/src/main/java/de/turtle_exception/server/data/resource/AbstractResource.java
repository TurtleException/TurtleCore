package de.turtle_exception.server.data.resource;

import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public abstract class AbstractResource {
    protected final List<Field> fields = new ArrayList<>();

    public AbstractResource() {

    }

    /* - - - */

    public abstract JsonObject get(Predicate<Object> identifier);

    public abstract boolean delete(Predicate<Object> identifier);

    public abstract void put(JsonObject json);

    public abstract void patch(JsonObject json);
}
