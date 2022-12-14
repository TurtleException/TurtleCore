package de.turtle_exception.server.data;

import com.google.gson.*;
import de.turtle_exception.client.api.entities.Turtle;
import de.turtle_exception.client.internal.data.ResourceUtil;
import de.turtle_exception.client.internal.data.annotations.Keys;
import de.turtle_exception.client.internal.data.annotations.Resource;
import de.turtle_exception.client.internal.util.time.TurtleType;
import de.turtle_exception.client.internal.util.time.TurtleUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.lang.annotation.AnnotationFormatError;
import java.util.Map;
import java.util.logging.Level;

@Deprecated
@SuppressWarnings("unused")
public class FilesystemProvider extends DatabaseProvider {
    private final File dir;

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public FilesystemProvider(@NotNull File dir) {
        this.dir = dir;
        this.dir.mkdirs();
    }

    @Override
    protected void onStart() {
        this.logger.log(Level.INFO, "Assigned directory: " + dir.getPath());
    }

    /* - - - */


    @Override
    protected boolean doDelete(@NotNull Class<? extends Turtle> type, long id) throws AnnotationFormatError {
        Resource annotation = ResourceUtil.getResourceAnnotation(type);
        File file = this.getFile(annotation, id);

        /*
         * This would normally be the point where all associations to this resource (i.e. Group membership when deleting
         * a User) should be deleted. Doing so would either require a two-way reference (which isn't ideal when working
         * with a local filesystem) or a complete sweep of the entire database to filter for occurrences of the resource
         * id. This would of course be a pretty costly operation, especially with a growing database.
         * So for now the approach will be this: Resources that have references to other resources should filter the
         * reference ids on instantiation and log any id that could not be linked to an instance of that referenced
         * resource. After that, the id will be ignored.
         * This might be revisited if / when the database will be moved to SQL.
         */

        return file.delete();
    }

    @Override
    protected @Nullable JsonObject doGet(@NotNull Class<? extends Turtle> type, long id) throws AnnotationFormatError {
        Resource annotation = ResourceUtil.getResourceAnnotation(type);
        File file = this.getFile(annotation, id);

        if (!file.exists()) return null;

        try {
            return this.getJson(file);
        } catch (FileNotFoundException e) {
            throw new AssertionError("File should exist and not be a directory.");
        }
    }

    @Override
    protected @NotNull JsonArray doGet(@NotNull Class<? extends Turtle> type) throws AnnotationFormatError {
        Resource annotation = ResourceUtil.getResourceAnnotation(type);
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

    @Override
    protected @Nullable JsonObject doPut(@NotNull Class<? extends Turtle> type, @NotNull JsonObject data) throws AnnotationFormatError {
        Resource annotation = ResourceUtil.getResourceAnnotation(type);

        long id = TurtleUtil.newId(TurtleType.RESOURCE);
        data.addProperty(Keys.Turtle.ID, id);

        File file = getFile(annotation, id);

        try {
            this.setJson(data, file);

            return this.getJson(file);
        } catch (IOException ignored) {
            return null;
        }
    }

    @Override
    protected @NotNull JsonObject doPatch(@NotNull Class<? extends Turtle> type, @NotNull JsonObject data, long id) throws AnnotationFormatError, IOException {
        Resource annotation = ResourceUtil.getResourceAnnotation(type);
        File file = getFile(annotation, id);

        if (!file.exists())
            throw new NullPointerException("Entry does not exist!");

        try {
            JsonObject json = this.getJson(file);

            for (Map.Entry<String, JsonElement> entry : data.entrySet()) {
                // don't overwrite the id
                if (entry.getKey().equals(Keys.Turtle.ID)) continue;

                json.add(entry.getKey(), entry.getValue());
            }

            this.setJson(json, file);
        } catch (FileNotFoundException e) {
            throw new AssertionError("File should exist and not be a directory.");
        }

        try {
            return this.getJson(file);
        } catch (FileNotFoundException e) {
            throw new AssertionError("File should exist and not be a directory.");
        }
    }

    @Override
    protected @NotNull JsonObject doPatchEntry(@NotNull Class<? extends Turtle> type, long id, @NotNull String key, @NotNull Object obj, boolean add) throws IOException {
        JsonObject json = this.doGet(type, id);

        if (json == null)
            throw new NullPointerException("Entry does not exist!");

        JsonObject patch = new JsonObject();
        JsonArray  arr   = json.get(key).getAsJsonArray();

        if (add)
            ResourceUtil.addValue(arr, obj);
        else
            ResourceUtil.removeValue(arr, obj);

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
