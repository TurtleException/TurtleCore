package de.turtle_exception.client.internal.data;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import de.turtle_exception.client.api.TurtleClient;
import de.turtle_exception.client.api.entities.Turtle;
import de.turtle_exception.client.internal.data.annotations.Key;
import de.turtle_exception.client.internal.data.annotations.Relation;
import de.turtle_exception.client.internal.data.annotations.Resource;
import de.turtle_exception.client.internal.entities.EntityBuilder;
import de.turtle_exception.client.internal.util.AnnotationUtil;
import de.turtle_exception.client.internal.util.Checks;
import de.turtle_exception.client.internal.util.logging.NestedLogger;
import org.jetbrains.annotations.NotNull;

import java.lang.annotation.AnnotationFormatError;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class ResourceBuilder {
    private final TurtleClient client;
    private final NestedLogger logger;

    public ResourceBuilder(@NotNull TurtleClient client) {
        this.client = client;
        this.logger = new NestedLogger("ResourceBuilder", client.getLogger());
    }

    public <T extends Turtle> @NotNull T buildObject(@NotNull Class<T> type, JsonObject json) throws IllegalArgumentException, AnnotationFormatError {
        Checks.nonNull(json, "JSON data");

        // Make sure the @Resource annotation is present
        Resource annotation = ResourceUtil.getResourceAnnotation(type);

        this.logger.log(Level.FINE, "Build call (JSON > obj) for object of type " + type.getSimpleName());
        this.logger.log(Level.FINEST, "\tJSON:  " + json);

        try {
            Method buildMethod = EntityBuilder.class.getMethod(annotation.builder(), JsonObject.class, TurtleClient.class);
            return type.cast(buildMethod.invoke(null, json, client));
        } catch (NoSuchMethodException | SecurityException e) {
            throw new IllegalArgumentException("No build method available: " + type.getName(), e);
        } catch (ClassCastException e) {
            throw new IllegalArgumentException("Illegal return type on build method.");
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Throwable t) {
            throw new IllegalArgumentException(t);
        }
    }

    public <T extends Turtle> @NotNull List<T> buildObjects(@NotNull Class<T> type, JsonArray json) throws IllegalArgumentException, AnnotationFormatError {
        Checks.nonNull(json, "JSON data");

        this.logger.log(Level.FINE, "Build call (JSON > obj) for " + json.size() + " objects of type " + type.getSimpleName());
        this.logger.log(Level.FINEST, "\tJSON:  " + json);

        ArrayList<T> list = new ArrayList<>();
        for (JsonElement element : json)
            list.add(this.buildObject(type, (JsonObject) element));
        return List.copyOf(list);
    }

    public @NotNull JsonObject buildJson(@NotNull Turtle object) throws IllegalArgumentException, AnnotationFormatError {
        Resource resource = ResourceUtil.getResourceAnnotation(object.getClass());

        this.logger.log(Level.FINE, "Build call (obj > JSON) for object of type " + object.getClass().getSimpleName());
        this.logger.log(Level.FINEST, "\tObject:  " + object);

        JsonObject json = new JsonObject();

        for (Method method : object.getClass().getMethods()) {
            Key atKey = AnnotationUtil.getAnnotation(method, Key.class);

            // value should be ignored
            if (atKey == null) continue;

            Object value = ResourceUtil.getValue(method, object);

            if (atKey.relation() == Relation.ONE_TO_ONE)
                ResourceUtil.addValue(json, atKey.name(), value);
            else
                json.add(atKey.name(), handleReference(atKey, value));
        }

        return json;
    }

    private static JsonArray handleReference(@NotNull Key annotation, @NotNull Object value) {
        if (annotation.relation() == Relation.ONE_TO_ONE)
            throw new IllegalArgumentException("Key may not mark a 1:1 relation");

        // TODO: Handle Relation.ONE_TO_MANY

        if (!(value instanceof Iterable<?> iterable))
            throw new AnnotationFormatError("Unexpected type " + value.getClass().getName() + " on reference: " + annotation.name());

        JsonArray arr = new JsonArray();

        // reference to another resource
        if (AnnotationUtil.getAnnotation(annotation.type(), Resource.class) != null) {
            try {
                for (Object o : iterable)
                    arr.add(((Turtle) o).getId());
                return arr;
            } catch (ClassCastException e) {
                throw new AnnotationFormatError("Illegal Resource annotation on Iterable that is not of type Turtle");
            }
        }

        // reference to a primitive type
        for (Object o : iterable)
            ResourceUtil.addValue(arr, o);

        return arr;
    }
}