package de.turtle_exception.core.internal.data;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import de.turtle_exception.core.internal.data.annotations.Key;
import de.turtle_exception.core.internal.data.annotations.Reference;
import de.turtle_exception.core.internal.data.annotations.Resource;
import org.jetbrains.annotations.NotNull;

import java.lang.annotation.AnnotationFormatError;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;

public class DataBuilder {
    private DataBuilder() { }

    public static <T> @NotNull T buildObject(@NotNull Class<T> type, @NotNull JsonObject json) throws IllegalArgumentException, AnnotationFormatError {
        // Make sure the @Resource annotation is present
        getResourceAnnotation(type);

        try {
            return type.cast(type.getMethod("build", JsonObject.class).invoke(null, json));
        } catch (NoSuchMethodException | SecurityException e) {
            throw new IllegalArgumentException("No build method available: " + type.getName(), e);
        } catch (ClassCastException e) {
            throw new IllegalArgumentException("Illegal return type on build method.");
        } catch (Throwable t) {
            throw new IllegalArgumentException(t);
        }
    }

    public static @NotNull JsonObject buildJson(@NotNull Object object) throws IllegalArgumentException, AnnotationFormatError {
        Resource resource = getResourceAnnotation(object.getClass());

        JsonObject json = new JsonObject();


        /* - Primitive keys */

        for (Method method : object.getClass().getMethods()) {
            Key atKey = method.getAnnotation(Key.class);

            // method should be ignored
            if (atKey == null) continue;

            Object value;
            try {
                value = method.invoke(object);
            } catch (Throwable t) {
                throw new AnnotationFormatError("Unable to invoke key method: " + method.getName(), t);
            }

            addValue(json, atKey.name(), value);
        }

        for (Field field : object.getClass().getFields()) {
            Key atKey = field.getAnnotation(Key.class);

            // field should be ignored
            if (atKey == null) continue;

            Object value;
            try {
                value = field.get(object);
            } catch (Throwable t) {
                throw new AnnotationFormatError("Unable to access key field: " + field.getName(), t);
            }

            addValue(json, atKey.name(), value);
        }


        /* - Reference keys */

        for (Method method : object.getClass().getMethods()) {
            Reference atReference = method.getAnnotation(Reference.class);

            // method should be ignored
            if (atReference == null) continue;

            Object value;
            try {
                value = method.invoke(object);
            } catch (Throwable t) {
                throw new AnnotationFormatError("Unable to invoke reference method: " + method.getName(), t);
            }

            json.add(atReference.name(), handleReference(atReference, value));
        }

        for (Field field : object.getClass().getFields()) {
            Reference atReference = field.getAnnotation(Reference.class);

            // field should be ignored
            if (atReference == null) continue;

            Object value;
            try {
                value = field.get(object);
            } catch (Throwable t) {
                throw new AnnotationFormatError("Unable to access reference field: " + field.getName(), t);
            }

            json.add(atReference.name(), handleReference(atReference, value));
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

    private static JsonElement handleReference(@NotNull Reference annotation, @NotNull Object value) {
        // TODO: add support for other relation types

        if (!(value instanceof Collection<?> collection))
            throw new AnnotationFormatError("Unexpected type " + value.getClass().getSimpleName() + " on reference: " + annotation.name());

        JsonArray arr = new JsonArray();

        entries:
        for (Object entry : collection) {
            // TODO: don't handle every entry by itself

            Resource atReferenceResource = entry.getClass().getAnnotation(Resource.class);

            if (atReferenceResource != null) {
                // reference is a resource itself

                for (Method refMethod : entry.getClass().getMethods()) {
                    Key atMethod = refMethod.getAnnotation(Key.class);
                    if (atMethod == null)    continue;
                    if (!atMethod.primary()) continue;

                    // found primary!
                    Object primaryValue;
                    try {
                        primaryValue = refMethod.invoke(entry);
                    } catch (Throwable t) {
                        throw new AnnotationFormatError("Unable to invoke reference primary key method: " + refMethod.getName(), t);
                    }
                    addValue(arr, primaryValue);

                    continue entries;
                }

                for (Field refField : entry.getClass().getFields()) {
                    Key atField = refField.getAnnotation(Key.class);
                    if (atField == null)    continue;
                    if (!atField.primary()) continue;

                    // found primary!
                    Object primaryValue;
                    try {
                        primaryValue = refField.get(entry);
                    } catch (Throwable t) {
                        throw new AnnotationFormatError("Unable to get reference primary key field: " + refField.getName(), t);
                    }
                    addValue(arr, primaryValue);

                    continue entries;
                }
            } else {
                // reference is a primitive
                addValue(arr, entry);
            }
        }

        return arr;
    }
}
