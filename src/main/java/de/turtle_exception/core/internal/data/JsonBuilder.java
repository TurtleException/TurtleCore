package de.turtle_exception.core.internal.data;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import de.turtle_exception.core.api.TurtleCore;
import de.turtle_exception.core.internal.data.annotations.Key;
import de.turtle_exception.core.internal.data.annotations.Relation;
import de.turtle_exception.core.internal.data.annotations.Resource;
import org.jetbrains.annotations.NotNull;

import java.lang.annotation.AnnotationFormatError;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Iterator;
import java.util.stream.Stream;

public class JsonBuilder {
    private static final String BUILD_METHOD_NAME = "build";

    private final TurtleCore core;

    public JsonBuilder(@NotNull TurtleCore core) {
        this.core = core;
    }

    public <T> @NotNull T buildObject(@NotNull Class<T> type, @NotNull JsonObject json) throws IllegalArgumentException, AnnotationFormatError {
        // Make sure the @Resource annotation is present
        getResourceAnnotation(type);

        try {
            return type.cast(type.getMethod(BUILD_METHOD_NAME, JsonObject.class, TurtleCore.class).invoke(null, json, core));
        } catch (NoSuchMethodException | SecurityException e) {
            throw new IllegalArgumentException("No build method available: " + type.getName(), e);
        } catch (ClassCastException e) {
            throw new IllegalArgumentException("Illegal return type on build method.");
        } catch (Throwable t) {
            throw new IllegalArgumentException(t);
        }
    }

    public @NotNull JsonObject buildJson(@NotNull Object object) throws IllegalArgumentException, AnnotationFormatError {
        Resource resource = getResourceAnnotation(object.getClass());

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
                addValue(json, atKey.name(), value);
            else
                json.add(atKey.name(), handleReference(atKey, value));
        }

        return json;
    }

    private static @NotNull Resource getResourceAnnotation(@NotNull Class<?> clazz) throws AnnotationFormatError {
        Resource annotation = clazz.getAnnotation(Resource.class);
        if (annotation == null)
            throw new AnnotationFormatError("Missing annotation @Resource");
        return annotation;
    }

    private static void addValue(@NotNull JsonArray json, Object object) {
        if (object instanceof Boolean entryBoolean) {
            json.add(entryBoolean);
        } else if (object instanceof Character entryCharacter) {
            json.add(entryCharacter);
        } else if (object instanceof Number entryNumber) {
            json.add(entryNumber);
        } else {
            json.add(String.valueOf(object));
        }
    }

    private static void addValue(@NotNull JsonObject json, @NotNull String key, Object object) {
        if (object instanceof Boolean objBoolean) {
            json.addProperty(key, objBoolean);
        } else if (object instanceof Character objCharacter) {
            json.addProperty(key, objCharacter);
        } else if (object instanceof Number objNumber) {
            json.addProperty(key, objNumber);
        } else {
            json.addProperty(key, String.valueOf(object));
        }
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
            addValue(arr, o);

        return arr;
    }

    private static JsonArray handleResourceReference(@NotNull Iterable<?> iterable) {
        JsonArray arr = new JsonArray();
        for (Object entry : iterable)
            addValue(arr, getPrimary(entry));

        return arr;
    }

    private static @NotNull Object getPrimary(@NotNull Object obj) throws AnnotationFormatError {
        getResourceAnnotation(obj.getClass());

        Stream<AccessibleObject> stream = Stream.concat(
                Arrays.stream(obj.getClass().getMethods()),
                Arrays.stream(obj.getClass().getFields())
        );

        for (Iterator<AccessibleObject> it = stream.iterator(); it.hasNext(); ) {
            AccessibleObject accObj = it.next();
            Key key = accObj.getAnnotation(Key.class);

            if (key != null && key.primary())
                return getValue(accObj, obj);
        }

        throw new AnnotationFormatError("Could not find primary key!");
    }

    private static Object getValue(@NotNull AccessibleObject accObj, @NotNull Object instance) {
        if (accObj instanceof Method method) {
            try {
                return method.invoke(instance);
            } catch (Throwable t) {
                throw new AnnotationFormatError("Unable to invoke key method: " + method.getName(), t);
            }
        } else if (accObj instanceof Field field) {
            try {
                return field.get(instance);
            } catch (Throwable t) {
                throw new AnnotationFormatError("Unable to access key field: " + field.getName(), t);
            }
        } else {
            throw new AssertionError("Unexpected type: " + accObj.getClass().getName());
        }
    }
}
