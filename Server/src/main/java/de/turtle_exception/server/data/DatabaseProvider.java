package de.turtle_exception.server.data;

import com.google.gson.*;
import de.turtle_exception.client.api.entities.Turtle;
import de.turtle_exception.client.internal.ActionImpl;
import de.turtle_exception.client.internal.Provider;
import de.turtle_exception.client.internal.data.DataUtil;
import de.turtle_exception.client.internal.data.annotations.Resource;
import de.turtle_exception.client.internal.request.actions.SimpleAction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.lang.annotation.AnnotationFormatError;
import java.util.Map;
import java.util.logging.Level;

public class DatabaseProvider extends Provider {
    private final File dir;

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public DatabaseProvider(@NotNull File dir) {
        super(1);
        this.dir = dir;
        this.dir.mkdirs();

        this.logger.log(Level.INFO, "Assigned directory: " + dir.getPath());
    }

    /* - - - */

    @Override
    public <T extends Turtle> @NotNull SimpleAction<Boolean> delete(@NotNull Class<T> type, long id) throws AnnotationFormatError {
        this.logger.log(Level.FINER, "DELETE request for id " + id);
        return new SimpleAction<>(this, () -> this.doDelete(type, id));
    }

    @Override
    public <T extends Turtle> @NotNull SimpleAction<JsonObject> get(@NotNull Class<T> type, long id) throws AnnotationFormatError {
        this.logger.log(Level.FINER, "GET request for id " + id);
        return new SimpleAction<>(this, () -> this.doGet(type, id));
    }

    @Override
    public <T extends Turtle> @NotNull SimpleAction<JsonArray> get(@NotNull Class<T> type) throws AnnotationFormatError {
        this.logger.log(Level.FINER, "GET request for type " + type.getSimpleName());
        return new SimpleAction<>(this, () -> this.doGet(type));
    }

    @Override
    public <T extends Turtle> @NotNull SimpleAction<JsonObject> put(@NotNull Class<T> type, @NotNull JsonObject content) throws AnnotationFormatError {
        this.logger.log(Level.FINER, "PUT request for object of type " + type.getSimpleName());
        return new SimpleAction<>(this, () -> this.doPut(type, content));
    }

    @Override
    public <T extends Turtle> @NotNull SimpleAction<JsonObject> patch(@NotNull Class<T> type, @NotNull JsonObject content, long id) throws AnnotationFormatError {
        this.logger.log(Level.FINER, "PATCH request for id " + id);
        return new SimpleAction<>(this, () -> this.doPatch(type, content, id));
    }

    @Override
    public @NotNull <T extends Turtle> ActionImpl<JsonObject> patchEntryAdd(@NotNull Class<T> type, long id, @NotNull String key, @NotNull Object obj) {
        this.logger.log(Level.FINER, "PATCH_ENTRY_ADD request for id " + id);
        return new SimpleAction<>(this, () -> this.doPatchEntry(type, id, key, obj, true));
    }

    @Override
    public @NotNull <T extends Turtle> ActionImpl<JsonObject> patchEntryDel(@NotNull Class<T> type, long id, @NotNull String key, @NotNull Object obj) {
        this.logger.log(Level.FINER, "PATCH_ENTRY_DEL request for id " + id);
        return new SimpleAction<>(this, () -> this.doPatchEntry(type, id, key, obj, false));
    }

    /* - - - */

    private boolean doDelete(@NotNull Class<? extends Turtle> type, long id) throws AnnotationFormatError {
        Resource annotation = DataUtil.getResourceAnnotation(type);
        File file = this.getFile(annotation, id);

        // TODO: associations?

        return file.delete();
    }

    private @Nullable JsonObject doGet(@NotNull Class<? extends Turtle> type, long id) throws AnnotationFormatError {
        Resource annotation = DataUtil.getResourceAnnotation(type);
        File file = this.getFile(annotation, id);

        if (!file.exists()) return null;

        try {
            return this.getJson(file);
        } catch (FileNotFoundException e) {
            throw new AssertionError("File should exist and not be a directory.");
        }
    }

    private @NotNull JsonArray doGet(@NotNull Class<? extends Turtle> type) throws AnnotationFormatError {
        Resource annotation = DataUtil.getResourceAnnotation(type);
        File file = this.getFile(annotation);

        JsonArray arr = new JsonArray();

        if (!file.exists()) return arr;

        File[] files = file.listFiles();

        if (files == null) return arr;

        for (File singleFile : files) {
            if (!singleFile.getName().endsWith(".json")) continue;

            long id = Long.parseLong(singleFile.getName().substring(0, singleFile.getName().lastIndexOf(".")));
            arr.add(this.doGet(type, id));
        }

        return arr;
    }

    private @Nullable JsonObject doPut(@NotNull Class<? extends Turtle> type, @NotNull JsonObject data) throws AnnotationFormatError {
        Resource annotation = DataUtil.getResourceAnnotation(type);
        long id = DataUtil.getTurtleId(data);
        File file = getFile(annotation, id);

        // TODO: make sure the id is created by the server

        try {
            this.setJson(data, file);

            return this.getJson(file);
        } catch (IOException ignored) {
            return null;
        }
    }

    private @NotNull JsonObject doPatch(@NotNull Class<? extends Turtle> type, @NotNull JsonObject data, long id) throws AnnotationFormatError {
        Resource annotation = DataUtil.getResourceAnnotation(type);
        File file = getFile(annotation, id);

        if (!file.exists())
            throw new NullPointerException("Entry does not exist!");

        try {
            JsonObject json = this.getJson(file);

            for (Map.Entry<String, JsonElement> entry : data.entrySet()) {
                // don't overwrite the id
                if (entry.getKey().equals("id")) continue;

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

    private @NotNull JsonObject doPatchEntry(@NotNull Class<? extends Turtle> type, long id, @NotNull String key, @NotNull Object obj, boolean add) {
        JsonObject json = this.doGet(type, id);

        if (json == null)
            throw new NullPointerException("Entry does not exist!");

        JsonObject patch = new JsonObject();
        JsonArray  arr   = json.get(key).getAsJsonArray();

        if (add)
            DataUtil.addValue(arr, obj);
        else
            DataUtil.removeValue(arr, obj);

        patch.add(key, arr);
        return this.doPatch(type, patch, id);
    }

    /* - - - */

    private @NotNull File getFile(@NotNull Resource resource, long id) {
        return new File(dir, resource.path() + File.separator + id + ".json");
    }

    private @NotNull File getFile(@NotNull Resource resource) {
        return new File(dir, resource.path());
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

        this.logger.log(Level.FINER, "Resource written: " + file.getName());
    }

    /* - - - */

    public @NotNull File getDir() {
        return dir;
    }
}
