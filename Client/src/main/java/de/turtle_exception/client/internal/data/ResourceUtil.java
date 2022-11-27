package de.turtle_exception.client.internal.data;

import com.google.gson.*;
import de.turtle_exception.client.api.entities.Turtle;
import de.turtle_exception.client.internal.data.annotations.Key;
import de.turtle_exception.client.internal.data.annotations.Keys;
import de.turtle_exception.client.internal.data.annotations.Resource;
import de.turtle_exception.client.internal.util.AnnotationUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.annotation.AnnotationFormatError;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public class ResourceUtil {
    private ResourceUtil() { }

    // this throws an Error instead of an Exception because every Class extending Turtle should hava a @Resource annotation
    public static @NotNull Resource getResourceAnnotation(@NotNull Class<? extends Turtle> clazz) throws AnnotationFormatError {
        Resource annotation = AnnotationUtil.getAnnotation(clazz, Resource.class);
        if (annotation == null)
            throw new AnnotationFormatError("Missing annotation @Resource on class " + clazz.getName());
        return annotation;
    }

    public static @NotNull List<Key> getKeyAnnotations(@NotNull Class<? extends Turtle> clazz) throws AnnotationFormatError {
        return Stream.concat(
                Arrays.stream(clazz.getMethods()),
                Arrays.stream(clazz.getFields())
        )
                .map(accObj -> AnnotationUtil.getAnnotation(accObj, Key.class))
                .filter(Objects::nonNull)
                .toList();
    }

    public static @Nullable Key getKeyAnnotation(@NotNull Class<? extends Turtle> clazz, @NotNull String name) {
        return getKeyAnnotations(clazz).stream().filter(key -> key.name().equals(name)).findFirst().orElse(null);
    }

    public static long getTurtleId(@NotNull JsonObject content) throws IllegalArgumentException {
        try {
            return content.get(Keys.Turtle.ID).getAsLong();
        } catch (ClassCastException | IllegalStateException e) {
            throw new IllegalArgumentException("Not a valid turtle");
        }
    }

    public static @NotNull Object getValue(@NotNull AccessibleObject accObj, @NotNull Object instance) {
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

    public static Object getValue(@NotNull JsonPrimitive json) {
        if (json.isJsonNull()) {
            return null;
        } else if (json.isBoolean()) {
            return json.getAsBoolean();
        } else if (json.isNumber()) {
            return json.getAsNumber();
        } else if (json.isString()) {
            return json.getAsString();
        }
        return json;
    }

    public static void addValue(@NotNull JsonArray json, Object object) {
        if (object instanceof JsonElement entryElement) {
            json.add(entryElement);
        } else if (object instanceof Boolean entryBoolean) {
            json.add(entryBoolean);
        } else if (object instanceof Character entryCharacter) {
            json.add(entryCharacter);
        } else if (object instanceof Number entryNumber) {
            json.add(entryNumber);
        } else {
            json.add(String.valueOf(object));
        }
    }

    public static void removeValue(@NotNull JsonArray json, @NotNull Object object) {
        for (int i = 0; i < json.size(); i++) {
            JsonElement element = json.get(i);

            if (equals(element, object)) {
                json.remove(i);
                return;
            }
        }
    }

    public static void addValue(@NotNull JsonObject json, @NotNull String key, Object object) {
        if (object == null) {
            json.add(key, JsonNull.INSTANCE);
        } else if (object instanceof JsonElement objElement) {
            json.add(key, objElement);
        } else if (object instanceof Boolean objBoolean) {
            json.addProperty(key, objBoolean);
        } else if (object instanceof Character objCharacter) {
            json.addProperty(key, objCharacter);
        } else if (object instanceof Number objNumber) {
            json.addProperty(key, objNumber);
        } else {
            json.addProperty(key, String.valueOf(object));
        }
    }

    public static boolean equals(@Nullable JsonElement element, @Nullable Object object) {
        if (element == null || element.isJsonNull() || object == null) return false;

        try {
            if (object instanceof Boolean objBoolean) {
                return objBoolean.equals(element.getAsBoolean());
            } else if (object instanceof Character objCharacter) {
                return objCharacter.equals(element.getAsString().charAt(0));
            } else if (object instanceof Number objNumber) {
                return objNumber.equals(element.getAsNumber());
            } else {
                return String.valueOf(object).equals(element.getAsString());
            }
        } catch (ClassCastException | IllegalStateException | IndexOutOfBoundsException ignored) {
            return false;
        }
    }
}
