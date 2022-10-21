package de.turtle_exception.server.data.resource;

import com.google.gson.JsonObject;

import java.util.function.Predicate;

public class UserResource extends AbstractResource {
    public UserResource() {
        fields.add(new Field("id", Long.class, false, true));
        fields.add(new Field("name", String.class, false, false));
    }

    /* - - - */

    @Override
    public JsonObject get(Predicate<Object> identifier) {
        return null;
    }

    @Override
    public boolean delete(Predicate<Object> identifier) {
        return false;
    }

    @Override
    public void put(JsonObject json) {

    }

    @Override
    public void patch(JsonObject json) {

    }
}
