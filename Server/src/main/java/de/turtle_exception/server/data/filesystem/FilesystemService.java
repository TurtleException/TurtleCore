package de.turtle_exception.server.data.filesystem;

import com.google.gson.*;
import de.turtle_exception.core.util.ExceptionalFunction;
import de.turtle_exception.server.data.DataAccessException;
import de.turtle_exception.server.data.DataService;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.nio.file.NotDirectoryException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * An implementation of {@link DataService} that uses the local filesystem as database. All data is written into
 * {@code .json}-files and stored according to their primary identifier.
 */
public class FilesystemService implements DataService {
    /** The root directory of the filesystem database. */
    private final File dir;

    // These locks are used to prevent reading old data while it is being rewritten by another Thread
    private final Object metaLock  = new Object();
    private final Object groupLock = new Object();
    private final Object userLock  = new Object();

    // file containing credential data
    private final File fileCredentials;

    // directories
    private final File dirGroups;
    private final File dirUsers;

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public FilesystemService(@NotNull File dir) throws IOException {
        if (dir.isFile())
            throw new NotDirectoryException("Expected directory");

        if (!dir.exists())
            dir.mkdirs();

        this.dir = dir;

        this.fileCredentials = new File(dir, "credentials.json");
        this.dirGroups       = new File(dir, "groups");
        this.dirUsers        = new File(dir, "users");

        this.fileCredentials.createNewFile();
        this.dirGroups.mkdir();
        this.dirUsers.mkdir();
    }

    /* - - - */

    private JsonObject getFile(@NotNull File file) throws FileNotFoundException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.fromJson(new FileReader(file), JsonObject.class);
    }

    private JsonObject getCredentialsJson() throws DataAccessException {
        try {
            return this.getFile(fileCredentials);
        } catch (FileNotFoundException e) {
            throw new DataAccessException(e);
        }
    }

    private JsonObject getGroupJson(long group) throws DataAccessException {
        try {
            return this.getFile(new File(dirGroups, group + ".json"));
        } catch (FileNotFoundException e) {
            throw new DataAccessException(e);
        }
    }

    private JsonObject getUserJson(long user) throws DataAccessException {
        try {
            return this.getFile(new File(dirUsers, user + ".json"));
        } catch (FileNotFoundException e) {
            throw new DataAccessException(e);
        }
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private void setFile(@NotNull JsonObject json, @NotNull File file) throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        file.createNewFile();

        try (FileWriter writer = new FileWriter(file, false)) {
            writer.write(gson.toJson(json));
        }
    }

    private void setCredentialsJson(@NotNull JsonObject credentials) throws DataAccessException {
        try {
            this.setFile(credentials, fileCredentials);
        } catch (IOException e) {
            throw new DataAccessException(e);
        }
    }

    private void setGroupJson(@NotNull JsonObject group) throws DataAccessException {
        try {
            this.setFile(group, new File(dirGroups, group.get("id").getAsString()));
        } catch (IOException e) {
            throw new DataAccessException(e);
        }
    }

    private void setUserJson(@NotNull JsonObject user) throws DataAccessException {
        try {
            this.setFile(user, new File(dirUsers, user.get("id").getAsString()));
        } catch (IOException e) {
            throw new DataAccessException(e);
        }
    }

    /* - - - */

    @Override
    public @NotNull String getPass(@NotNull String login) throws DataAccessException {
        synchronized (metaLock) {
            JsonObject json = this.getCredentialsJson().getAsJsonObject("login");
            String pass = json.get(login).getAsString();
            if (pass != null)
                return pass;
            else
                throw new DataAccessException("Invalid login");
        }
    }

    @Override
    public @NotNull List<Long> getGroupIds() {
        File[] groupFiles;
        synchronized (groupLock) {
            groupFiles = dirGroups.listFiles();
        }

        if (groupFiles == null) return List.of();

        ArrayList<Long> list = new ArrayList<>();
        for (File file : groupFiles) {
            String name = file.getName();

            if (!name.endsWith(".json")) continue;

            try {
                list.add(Long.parseLong(name.substring(0, name.length() - ".json".length())));
            } catch (NumberFormatException ignored) { }
        }

        return List.copyOf(list);
    }

    @Override
    public @NotNull JsonObject getGroup(long id) throws DataAccessException {
        synchronized (groupLock) {
            try {
                return this.getFile(new File(dirGroups, id + ".json"));
            } catch (FileNotFoundException e) {
                throw new DataAccessException(e);
            }
        }
    }

    @Override
    public void setGroup(@NotNull JsonObject group) throws DataAccessException {
        long id;
        try {
            id = group.get("id").getAsLong();
            group.get("name").getAsString();
        } catch (Exception e) {
            throw new DataAccessException("Malformed parameters for group object");
        }

        synchronized (groupLock) {
            // simpler to use this (all members will also be deleted)
            this.deleteGroup(id);

            try {
                this.setFile(group, new File(dirGroups, group.get("id").getAsString()));
            } catch (IOException e) {
                throw new DataAccessException(e);
            }
        }
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Override
    public void deleteGroup(long id) {
        synchronized (groupLock) {
            File file = new File(dirGroups, id + ".json");
            file.delete();
        }
    }

    @Override
    public void modifyGroup(long group, @NotNull ExceptionalFunction<JsonObject, JsonObject> function) throws DataAccessException {
        synchronized (groupLock) {
            JsonObject json = this.getGroupJson(group);
            try {
                json = function.apply(json);
            } catch (Exception e) {
                throw new DataAccessException(e);
            }
            this.setGroupJson(json);
        }
    }

    @Override
    public @NotNull JsonArray getGroupMembers(long group) throws DataAccessException {
        try {
            return this.getGroup(group).getAsJsonArray("members");
        } catch (Exception e) {
            throw new DataAccessException(e);
        }
    }

    @Override
    public void addGroupMember(long group, long user) throws DataAccessException {
        this.modifyGroup(group, json -> {
            addElementToJsonArray(json, "members", new JsonPrimitive(user));
            return json;
        });
    }

    @Override
    public void delGroupMember(long group, long user) throws DataAccessException {
        this.modifyGroup(group, json -> {
            removeElementFromJsonArray(json, "members", String.valueOf(user));
            return json;
        });
    }

    @Override
    public @NotNull List<Long> getUserIds() {
        File[] userFiles;
        synchronized (userLock) {
            userFiles = dirUsers.listFiles();
        }

        if (userFiles == null) return List.of();

        ArrayList<Long> list = new ArrayList<>();
        for (File file : userFiles) {
            String name = file.getName();

            if (!name.endsWith(".json")) continue;

            try {
                list.add(Long.parseLong(name.substring(0, name.length() - ".json".length())));
            } catch (NumberFormatException ignored) { }
        }

        return List.copyOf(list);
    }

    @Override
    public @NotNull JsonObject getUser(long id) throws DataAccessException {
        synchronized (userLock) {
            JsonObject json = this.getUserJson(id);

            if (json == null)
                throw new DataAccessException("Unknown user");

            return json;
        }
    }

    @Override
    public void setUser(@NotNull JsonObject user) throws DataAccessException {
        long id;
        String name;
        try {
            id = user.get("id").getAsLong();
            name = user.get("name").getAsString();
        } catch (Exception e) {
            throw new DataAccessException("Malformed parameters for group object");
        }

        JsonObject json = new JsonObject();

        try {
            json.addProperty("id", id);
            json.addProperty("name", name);

            JsonElement discord = user.get("discord");
            json.add("discord", (discord instanceof JsonArray) ? discord : new JsonArray());

            JsonElement minecraft = user.get("minecraft");
            json.add("minecraft", (minecraft instanceof JsonArray) ? minecraft : new JsonArray());
        } catch (Exception e) {
            throw new DataAccessException(e);
        }

        synchronized (userLock) {
            // simpler to use this (all members will also be deleted)
            this.deleteUser(id);

            this.setUserJson(user);
        }
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Override
    public void deleteUser(long id) {
        synchronized (userLock) {
            // TODO: delete members

            File file = new File(dirUsers, id + ".json");
            file.delete();
        }
    }

    @Override
    public void modifyUser(long user, @NotNull ExceptionalFunction<JsonObject, JsonObject> function) throws DataAccessException {
        synchronized (userLock) {
            JsonObject json = this.getUserJson(user);
            try {
                json = function.apply(json);
            } catch (Exception e) {
                throw new DataAccessException(e);
            }
            this.setUserJson(json);
        }
    }

    @Override
    public @NotNull JsonArray getUserDiscord(long user) throws DataAccessException {
        JsonObject json;
        synchronized (userLock) {
            json = this.getUserJson(user);
        }
        JsonArray  discord = json.getAsJsonArray("discord");

        return discord != null ? discord : new JsonArray();
    }

    @Override
    public void addUserDiscord(long user, long discord) throws DataAccessException {
        this.modifyUser(user, json -> {
            addElementToJsonArray(json, "discord", new JsonPrimitive(discord));
            return json;
        });
    }

    @Override
    public void delUserDiscord(long user, long discord) throws DataAccessException {
        this.modifyUser(user, json -> {
            removeElementFromJsonArray(json, "discord", String.valueOf(discord));
            return json;
        });
    }

    @Override
    public @NotNull JsonArray getUserMinecraft(long user) throws DataAccessException {
        JsonObject json;
        synchronized (userLock) {
            json = this.getUserJson(user);
        }
        JsonArray minecraft = json.getAsJsonArray("minecraft");

        return minecraft != null ? minecraft : new JsonArray();
    }

    @Override
    public void addUserMinecraft(long user, @NotNull UUID minecraft) throws DataAccessException {
        this.modifyUser(user, json -> {
            addElementToJsonArray(json, "minecraft", new JsonPrimitive(minecraft.toString()));

            JsonArray arr = json.getAsJsonArray("minecraft");
            arr.add(minecraft.toString());
            json.add("minecraft", arr);
            return json;
        });
    }

    @Override
    public void delUserMinecraft(long user, @NotNull UUID minecraft) throws DataAccessException {
        this.modifyUser(user, json -> {
            removeElementFromJsonArray(json, "minecraft", minecraft.toString());
            return json;
        });
    }

    /* - - - */

    private static void removeElementFromJsonArray(@NotNull JsonObject json, @NotNull String keyToArray, @NotNull String element) throws DataAccessException {
        modifyJsonArray(json, keyToArray, arr -> {
            for (int i = 0; i < arr.size(); i++)
                if (element.equals(arr.get(i).getAsString())) {
                    arr.remove(i);
                    break;
                }
            return arr;
        });
    }

    private static void addElementToJsonArray(@NotNull JsonObject json, @NotNull String keyToArray, @NotNull JsonElement element) throws DataAccessException {
        modifyJsonArray(json, keyToArray, arr -> {
            arr.add(element);
            return arr;
        });
    }

    private static void modifyJsonArray(@NotNull JsonObject json, @NotNull String keyToArray, @NotNull ExceptionalFunction<JsonArray, JsonArray> function) throws DataAccessException {
        try {
            JsonElement element = json.get(keyToArray);
            JsonArray   array   = element != null ? (JsonArray) element : new JsonArray();

            json.add(keyToArray, function.apply(array));
        } catch (Exception e) {
            throw new DataAccessException(e);
        }
    }
}
