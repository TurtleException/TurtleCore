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
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.stream.Stream;

public class JsonBuilder {
    private final TurtleClient client;
    private final NestedLogger logger;

    public JsonBuilder(@NotNull TurtleClient client) {
        this.client = client;
        this.logger = new NestedLogger("JsonBuilder", client.getLogger());
    }

    public <T extends Turtle> @NotNull T buildObject(@NotNull Class<T> type, JsonObject json) throws IllegalArgumentException, AnnotationFormatError {
        Checks.nonNull(json, "JSON data");

        // Make sure the @Resource annotation is present
        Resource annotation = DataUtil.getResourceAnnotation(type);

        this.logger.log(Level.FINE, "Build call (JSON > obj) for object of type " + type.getSimpleName());

        System.out.println(json);

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

        System.out.println(json);

        ArrayList<T> list = new ArrayList<>();
        for (JsonElement element : json)
            list.add(this.buildObject(type, (JsonObject) element));
        return List.copyOf(list);
    }

    public @NotNull JsonObject buildJson(@NotNull Turtle object) throws IllegalArgumentException, AnnotationFormatError {
        Resource resource = DataUtil.getResourceAnnotation(object.getClass());

        this.logger.log(Level.FINE, "Build call (obj > JSON) for object of type " + object.getClass().getSimpleName());

        JsonObject json = new JsonObject();

        Stream<AccessibleObject> stream = Stream.concat(
                Arrays.stream(object.getClass().getMethods()),
                Arrays.stream(object.getClass().getFields())
        );

        for (Iterator<AccessibleObject> it = stream.iterator(); it.hasNext(); ) {
            AccessibleObject accObj = it.next();
            Key atKey = AnnotationUtil.getAnnotation(object.getClass(), accObj, Key.class);

            // value should be ignored
            if (atKey == null) continue;

            Object value = DataUtil.getValue(accObj, object);

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
        if (annotation.type().getAnnotation(Resource.class) != null) {
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
            DataUtil.addValue(arr, o);

        return arr;
    }
}