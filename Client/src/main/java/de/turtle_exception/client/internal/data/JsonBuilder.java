package de.turtle_exception.client.internal.data;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import de.turtle_exception.client.api.TurtleClient;
import de.turtle_exception.client.internal.data.annotations.Key;
import de.turtle_exception.client.internal.data.annotations.Relation;
import de.turtle_exception.client.internal.data.annotations.Resource;
import de.turtle_exception.client.internal.entities.EntityBuilder;
import de.turtle_exception.client.internal.util.Checks;
import org.jetbrains.annotations.NotNull;

import java.lang.annotation.AnnotationFormatError;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

public class JsonBuilder {
    private final TurtleClient client;

    public JsonBuilder(@NotNull TurtleClient client) {
        this.client = client;
    }

    public <T> @NotNull T buildObject(@NotNull Class<T> type, JsonObject json) throws IllegalArgumentException, AnnotationFormatError {
        Checks.nonNull(json, "JSON data");

        // Make sure the @Resource annotation is present
        Resource annotation = DataUtil.getResourceAnnotation(type);

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

    public <T> @NotNull List<T> buildObjects(@NotNull Class<T> type, JsonArray json) throws IllegalArgumentException, AnnotationFormatError {
        Checks.nonNull(json, "JSON data");

        ArrayList<T> list = new ArrayList<>();
        for (JsonElement element : json)
            list.add(this.buildObject(type, (JsonObject) element));
        return List.copyOf(list);
    }

    public @NotNull JsonObject buildJson(@NotNull Object object) throws IllegalArgumentException, AnnotationFormatError {
        Resource resource = DataUtil.getResourceAnnotation(object.getClass());

        JsonObject json = new JsonObject();

        Stream<AccessibleObject> stream = Stream.concat(
                Arrays.stream(object.getClass().getMethods()),
                Arrays.stream(object.getClass().getFields())
        );

        for (Iterator<AccessibleObject> it = stream.iterator(); it.hasNext(); ) {
            AccessibleObject accObj = it.next();
            Key atKey = accObj.getAnnotation(Key.class);

            // value should be ignored
            if (atKey == null) continue;

            Object value;
            if (accObj instanceof Method method) {
                try {
                    value = method.invoke(object);
                } catch (Throwable t) {
                    throw new AnnotationFormatError("Unable to invoke key method: " + method.getName(), t);
                }
            } else if (accObj instanceof Field field) {
                try {
                    value = field.get(object);
                } catch (Throwable t) {
                    throw new AnnotationFormatError("Unable to access key field: " + field.getName(), t);
                }
            } else {
                throw new AssertionError("Unexpected type: " + accObj.getClass().getName());
            }

            if (atKey.relation() == Relation.ONE_TO_ONE)
                DataUtil.addValue(json, atKey.name(), value);
            else
                json.add(atKey.name(), handleReference(atKey, value));
        }

        return json;
    }

    private static JsonArray handleReference(@NotNull Key annotation, @NotNull Object value) {
        if (annotation.relation() == Relation.ONE_TO_ONE)
            throw new IllegalArgumentException("Key may not mark a 1:1 relation");

        if (!(value instanceof Iterable<?> iterable))
            throw new AnnotationFormatError("Unexpected type " + value.getClass().getName() + "on reference: " + annotation.name());

        JsonArray arr = new JsonArray();

        // reference to another resource
        if (annotation.type().getAnnotation(Resource.class) != null)
            return handleResourceReference(iterable);

        // reference to a primitive type
        for (Object o : iterable)
            DataUtil.addValue(arr, o);

        return arr;
    }

    private static JsonArray handleResourceReference(@NotNull Iterable<?> iterable) {
        JsonArray arr = new JsonArray();
        for (Object entry : iterable)
            DataUtil.addValue(arr, DataUtil.getPrimaryValue(entry));

        return arr;
    }
}