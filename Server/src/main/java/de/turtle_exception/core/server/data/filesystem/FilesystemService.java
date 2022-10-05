package de.turtle_exception.core.server.data.filesystem;

import com.google.gson.*;
import de.turtle_exception.core.server.data.DataAccessException;
import de.turtle_exception.core.server.data.DataService;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.nio.file.NotDirectoryException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;

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
            groupFiles  = dirGroups.listFiles();
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
        synchronized (groupLock) {
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
    public void modifyGroup(long group, @NotNull Function<JsonObject, JsonObject> function) throws DataAccessException {
        synchronized (groupLock) {
            JsonObject json = this.getGroupJson(group);
            json = function.apply(json);
            this.setGroupJson(json);
        }
    }

    @Override
    public @NotNull List<Long> getUserIds() {
        File[] userFiles;
        synchronized (userLock) {
            userFiles  = dirUsers.listFiles();
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

            json.remove("groups");
            json.remove("discord");
            json.remove("minecraft");

            return json;
        }
    }

    @Override
    public void setUser(@NotNull JsonObject user) throws DataAccessException {
        final long id = user.get("id").getAsLong();

        synchronized (userLock) {
            JsonObject json = this.getUserJson(id);

            if (json == null) {
                // new user
                json = user.deepCopy();

                json.add("groups", new JsonArray());
                json.add("discord", new JsonArray());
                json.add("minecraft", new JsonArray());
            } else {
                // overwrite values
                for (String key : user.keySet())
                    json.add(key, user.get(key));
            }

            this.setUserJson(json);
        }
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Override
    public void deleteUser(long id) {
        synchronized (userLock) {
            File file = new File(dirUsers, id + ".json");
            file.delete();
        }
    }

    @Override
    public void modifyUser(long user, @NotNull Function<JsonObject, JsonObject> function) throws DataAccessException {
        synchronized (userLock) {
            JsonObject json = this.getUserJson(user);
            json = function.apply(json);
            this.setUserJson(json);
        }
    }

    @Override
    public @NotNull JsonArray getUserGroups(long user) throws DataAccessException {
        JsonObject json;
        synchronized (userLock) {
            json = this.getUserJson(user);
        }
        JsonArray group = json.getAsJsonArray("groups");

        return group != null ? group : new JsonArray();
    }

    @Override
    public void addUserGroup(long user, long group) throws DataAccessException {
        synchronized (userLock) {
            JsonArray json = getUserGroups(user);
            json.add(group);
            this.setUserGroups(user, json);
        }
    }

    @Override
    public void delUserGroup(long user, long group) throws DataAccessException {
        synchronized (userLock) {
            JsonArray oldJson = getUserGroups(user);
            JsonArray newJson = new JsonArray();

            for (JsonElement entry : oldJson)
                if (entry.getAsLong() != group)
                    newJson.add(entry);

            this.setUserGroups(user, newJson);
        }
    }

    private void setUserGroups(long user, @NotNull JsonArray groups) throws DataAccessException {
        JsonObject json = this.getUserJson(user);
        json.add("groups", groups);
        this.setUserJson(json);
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
        synchronized (userLock) {
            JsonArray json = getUserDiscord(user);
            json.add(discord);
            this.setUserDiscord(user, json);
        }
    }

    @Override
    public void delUserDiscord(long user, long discord) throws DataAccessException {
        synchronized (userLock) {
            JsonArray oldJson = getUserDiscord(user);
            JsonArray newJson = new JsonArray();

            for (JsonElement entry : oldJson)
                if (entry.getAsLong() != discord)
                    newJson.add(entry);

            this.setUserDiscord(user, newJson);
        }
    }

    private void setUserDiscord(long user, @NotNull JsonArray discord) throws DataAccessException {
        JsonObject json = this.getUserJson(user);
        json.add("discord", discord);
        this.setUserJson(json);
    }

    @Override
    public @NotNull JsonArray getUserMinecraft(long user) throws DataAccessException {
        JsonObject json;
        synchronized (userLock) {
            json = this.getUserJson(user);
        }
        JsonArray  minecraft = json.getAsJsonArray("minecraft");

        return minecraft != null ? minecraft : new JsonArray();
    }

    @Override
    public void addUserMinecraft(long user, @NotNull UUID minecraft) throws DataAccessException {
        synchronized (userLock) {
            JsonArray json = getUserMinecraft(user);
            json.add(minecraft.toString());
            this.setUserMinecraft(user, json);
        }
    }

    @Override
    public void delUserMinecraft(long user, @NotNull UUID minecraft) throws DataAccessException {
        synchronized (userLock) {
            JsonArray oldJson = getUserMinecraft(user);
            JsonArray newJson = new JsonArray();

            String mStr = minecraft.toString();

            for (JsonElement entry : oldJson)
                if (!entry.getAsString().equals(mStr))
                    newJson.add(entry);

            this.setUserMinecraft(user, newJson);
        }
    }

    private void setUserMinecraft(long user, @NotNull JsonArray minecraft) throws DataAccessException {
        JsonObject json = this.getUserJson(user);
        json.add("minecraft", minecraft);
        this.setUserJson(json);
    }
}
