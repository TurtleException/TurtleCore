package de.turtle_exception.client.internal.data;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import de.turtle_exception.client.internal.data.annotations.Key;
import de.turtle_exception.client.internal.data.annotations.Resource;
import org.jetbrains.annotations.NotNull;

import java.lang.annotation.AnnotationFormatError;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Iterator;
import java.util.stream.Stream;

public class DataUtil {
    private DataUtil() { }

    public static @NotNull Resource getResourceAnnotation(@NotNull Class<?> clazz) throws AnnotationFormatError {
        Resource annotation = clazz.getAnnotation(Resource.class);
        if (annotation == null)
            throw new AnnotationFormatError("Missing annotation @Resource");
        return annotation;
    }

    public static @NotNull AccessibleObject getPrimary(@NotNull Class<?> type) {
        getResourceAnnotation(type);

        Stream<AccessibleObject> stream = Stream.concat(
                Arrays.stream(type.getMethods()),
                Arrays.stream(type.getFields())
        );

        for (Iterator<AccessibleObject> it = stream.iterator(); it.hasNext(); ) {
            AccessibleObject accObj = it.next();
            Key key = accObj.getAnnotation(Key.class);

            if (key != null && key.primary())
                return accObj;
        }

        throw new AnnotationFormatError("Could not find primary key!");
    }

    public static @NotNull Object getPrimaryValue(@NotNull Object obj) throws AnnotationFormatError {
        return getValue(getPrimary(obj.getClass()), obj);
    }

    public static @NotNull Object getPrimaryValue(@NotNull JsonObject json, Class<?> type) throws AnnotationFormatError {
        Key key = getPrimary(type).getAnnotation(Key.class);

        if (key == null)
            throw new AssertionError("AccessibleObject must have @Key annotation");

        return json.get(key.name());
    }

    private static @NotNull Object getValue(@NotNull AccessibleObject accObj, @NotNull Object instance) {
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

    public static void addValue(@NotNull JsonArray json, Object object) {
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

    public static void addValue(@NotNull JsonObject json, @NotNull String key, Object object) {
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
}
