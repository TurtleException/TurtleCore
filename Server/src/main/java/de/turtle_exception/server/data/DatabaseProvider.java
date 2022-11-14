package de.turtle_exception.server.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import de.turtle_exception.client.api.request.Action;
import de.turtle_exception.client.internal.Provider;
import de.turtle_exception.client.internal.data.DataUtil;
import de.turtle_exception.client.internal.data.annotations.Key;
import de.turtle_exception.client.internal.data.annotations.Resource;
import de.turtle_exception.client.internal.request.SimpleAction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.lang.annotation.AnnotationFormatError;
import java.util.Map;

public class DatabaseProvider extends Provider {
    private final File dir;

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public DatabaseProvider(@NotNull File dir) {
        super(1);
        this.dir = dir;
        this.dir.mkdirs();
    }

    /* - - - */

    @Override
    public <T> @NotNull Action<Boolean> delete(@NotNull Class<T> type, @NotNull Object primary) throws AnnotationFormatError {
        return new SimpleAction<>(this, () -> this.doDelete(type, primary));
    }

    @Override
    public <T> @NotNull Action<JsonObject> get(@NotNull Class<T> type, @NotNull Object primary) throws AnnotationFormatError {
        return new SimpleAction<>(this, () -> this.doGet(type, primary));
    }

    @Override
    public <T> @NotNull Action<JsonObject> put(@NotNull Class<T> type, @NotNull JsonObject data) throws AnnotationFormatError {
        return new SimpleAction<>(this, () -> this.doPut(type, data));
    }

    @Override
    public <T> @NotNull Action<JsonObject> patch(@NotNull Class<T> type, @NotNull JsonObject data, @NotNull Object primary) throws AnnotationFormatError {
        return new SimpleAction<>(this, () -> this.doPatch(type, data, primary));
    }

    /* - - - */

    private boolean doDelete(@NotNull Class<?> type, @NotNull Object primary) throws AnnotationFormatError {
        Resource annotation = DataUtil.getResourceAnnotation(type);
        File file = this.getFile(annotation, primary);

        // TODO: associations?

        return file.delete();
    }

    private @Nullable JsonObject doGet(@NotNull Class<?> type, @NotNull Object primary) throws AnnotationFormatError {
        Resource annotation = DataUtil.getResourceAnnotation(type);
        File file = this.getFile(annotation, primary);

        if (!file.exists()) return null;

        try {
            return this.getJson(file);
        } catch (FileNotFoundException e) {
            throw new AssertionError("File should exist and not be a directory.");
        }
    }

    private @Nullable JsonObject doPut(@NotNull Class<?> type, @NotNull JsonObject data) throws AnnotationFormatError {
        Resource annotation = DataUtil.getResourceAnnotation(type);
        Object primary = DataUtil.getPrimaryValue(data, type);
        File file = getFile(annotation, primary);

        // TODO: make sure the primary is created by the server

        try {
            this.setJson(data, file);

            return this.getJson(file);
        } catch (IOException ignored) {
            return null;
        }
    }

    private @NotNull JsonObject doPatch(@NotNull Class<?> type, @NotNull JsonObject data, @NotNull Object primary) throws AnnotationFormatError {
        Resource annotation = DataUtil.getResourceAnnotation(type);
        File file = getFile(annotation, primary);

        if (!file.exists())
            throw new NullPointerException("Entry does not exist!");

        try {
            JsonObject json = this.getJson(file);

            Key primaryKey = DataUtil.getPrimary(type).getAnnotation(Key.class);

            for (Map.Entry<String, JsonElement> entry : data.entrySet()) {
                // don't overwrite the primary key
                if (entry.getKey().equals(primaryKey.name())) continue;

                json.add(entry.getKey(), entry.getValue());
            }

            this.setJson(json, file);
        } catch (FileNotFoundException e) {
            throw new AssertionError("File should exist and not be a directory.");
        } catch (IOException ignored) {
            // TODO ?
        }

        try {
            return this.getJson(file);
        } catch (FileNotFoundException e) {
            throw new AssertionError("File should exist and not be a directory.");
        }
    }

    /* - - - */

    private @NotNull File getFile(@NotNull Resource resource, @NotNull Object primary) {
        return new File(dir, resource.path() + File.separator + primary + ".json");
    }

    private @NotNull JsonObject getJson(@NotNull File file) throws FileNotFoundException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.fromJson(new FileReader(file), JsonObject.class);
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private void setJson(@NotNull JsonObject json, @NotNull File file) throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        file.createNewFile();

        try (FileWriter writer = new FileWriter(file, false)) {
            writer.write(gson.toJson(json));
        }
    }

    /* - - - */

    public @NotNull File getDir() {
        return dir;
    }
}
