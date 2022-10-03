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

public class FilesystemService implements DataService {
    private final File dir;

    private final Object lock = new Object();

    private final File fileCredentials;
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

    private @NotNull JsonObject getFile(@NotNull File file) throws FileNotFoundException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.fromJson(new FileReader(file), JsonObject.class);
    }

    private @NotNull JsonObject getCredentialsJson() throws DataAccessException {
        try {
            return this.getFile(fileCredentials);
        } catch (FileNotFoundException e) {
            throw new DataAccessException(e);
        }
    }

    private @NotNull JsonObject getGroupJson(long group) throws DataAccessException {
        try {
            return this.getFile(new File(dirGroups, group + ".json"));
        } catch (FileNotFoundException e) {
            throw new DataAccessException(e);
        }
    }

    private @NotNull JsonObject getUserJson(long user) throws DataAccessException {
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
        synchronized (lock) {
            JsonObject json = this.getCredentialsJson();
            String pass = json.get(login).getAsString();
            if (pass != null)
                return pass;
            else
                throw new DataAccessException("Invalid login");
        }
    }

    @Override
    public @NotNull List<Long> getGroupIds() {
        File[] groupFiles  = dirGroups.listFiles();

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
    public @NotNull String getGroup(long id) throws DataAccessException {
        synchronized (lock) {
            JsonObject json = this.getGroupJson(id);
            return new Gson().toJson(json);
        }
    }

    @Override
    public void setGroup(@NotNull String group) throws DataAccessException {
        synchronized (lock) {
            JsonObject json = new Gson().fromJson(group, JsonObject.class);
            this.setGroupJson(json);
        }
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Override
    public void deleteGroup(long id) {
        synchronized (lock) {
            File file = new File(dirGroups, id + ".json");
            file.delete();
        }
    }

    @Override
    public @NotNull List<Long> getUserIds() {
        File[] userFiles  = dirUsers.listFiles();

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
    public @NotNull String getUser(long id) throws DataAccessException {
        synchronized (lock) {
            JsonObject json = this.getUserJson(id);
            return new Gson().toJson(json);
        }
    }

    @Override
    public void setUser(@NotNull String user) throws DataAccessException {
        synchronized (lock) {
            JsonObject json = new Gson().fromJson(user, JsonObject.class);
            this.setUserJson(json);
        }
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Override
    public void deleteUser(long id) {
        synchronized (lock) {
            File file = new File(dirUsers, id + ".json");
            file.delete();
        }
    }

    @Override
    public void userJoinGroup(long userId, long groupId) throws DataAccessException {
        synchronized (lock) {
            JsonObject json   = getUserJson(userId);
            JsonArray  groups = json.getAsJsonArray("groups");

            groups.add(groupId);

            json.add("groups", groups);
            this.setUserJson(json);
        }
    }

    @Override
    public void userLeaveGroup(long userId, long groupId) throws DataAccessException {
        synchronized (lock) {
            JsonObject json = getUserJson(userId);
            JsonArray oldGroups = json.getAsJsonArray("groups");
            JsonArray newGroups = new JsonArray();

            for (JsonElement oldGroup : oldGroups)
                if (oldGroup.getAsLong() != groupId)
                    newGroups.add(oldGroup);

            json.add("groups", newGroups);
            this.setUserJson(json);
        }
    }

    @Override
    public @NotNull List<Long> getUserDiscord(long user) throws DataAccessException {
        JsonObject json    = getUserJson(user);
        JsonArray  discord = json.getAsJsonArray("discord");

        List<Long> list = new ArrayList<>();
        for (JsonElement entry : discord)
            list.add(entry.getAsLong());
        return List.copyOf(list);
    }

    @Override
    public @NotNull List<UUID> getUserMinecraft(long user) throws DataAccessException {
        JsonObject json      = getUserJson(user);
        JsonArray  minecraft = json.getAsJsonArray("minecraft");

        try {
            List<UUID> list = new ArrayList<>();
            for (JsonElement entry : minecraft)
                list.add(UUID.fromString(entry.getAsString()));
            return List.copyOf(list);
        } catch (IllegalArgumentException e) {
            throw new DataAccessException(e);
        }
    }

    @Override
    public void addUserDiscord(long user, long discord) throws DataAccessException {
        synchronized (lock) {
            JsonObject json = getUserJson(user);
            JsonArray  arr  = json.getAsJsonArray("discord");

            arr.add(user);

            json.add("discord", arr);
            this.setUserJson(json);
        }
    }

    @Override
    public void delUserDiscord(long user, long discord) throws DataAccessException {
        synchronized (lock) {
            JsonObject json = getUserJson(user);
            JsonArray oldArr = json.getAsJsonArray("discord");
            JsonArray newArr = new JsonArray();

            for (JsonElement entry : oldArr)
                if (entry.getAsLong() != discord)
                    newArr.add(entry);

            json.add("discord", newArr);
            this.setUserJson(json);
        }
    }

    @Override
    public void addUserMinecraft(long user, @NotNull UUID minecraft) throws DataAccessException {
        synchronized (lock) {
            JsonObject json = getUserJson(user);
            JsonArray  arr  = json.getAsJsonArray("minecraft");

            arr.add(minecraft.toString());

            json.add("minecraft", arr);
            this.setUserJson(json);
        }
    }

    @Override
    public void delUserMinecraft(long user, @NotNull UUID minecraft) throws DataAccessException {
        synchronized (lock) {
            JsonObject json = getUserJson(user);
            JsonArray oldArr = json.getAsJsonArray("minecraft");
            JsonArray newArr = new JsonArray();

            String uuidStr = minecraft.toString();

            for (JsonElement entry : oldArr)
                if (!entry.getAsString().equals(uuidStr))
                    newArr.add(entry);

            json.add("minecraft", newArr);
            this.setUserJson(json);
        }
    }
}
