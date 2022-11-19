package de.turtle_exception.client.api.entities;

import de.turtle_exception.client.api.request.Action;
import de.turtle_exception.client.internal.data.annotations.Key;
import de.turtle_exception.client.internal.data.annotations.Relation;
import de.turtle_exception.client.internal.data.annotations.Resource;
import net.dv8tion.jda.api.JDA;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Resource(path = "users", builder = "buildUser")
@SuppressWarnings("unused")
public interface User extends Turtle {
    @Override
    default @NotNull Action<User> update() {
        return this.getClient().retrieveUser(this.getId());
    }

    /* - NAME - */

    @Key(name = "name")
    @NotNull String getName();

    @NotNull Action<User> modifyName(@NotNull String name);

    /* - GROUPS - */

    default @NotNull List<Group> getGroups() {
        ArrayList<Group> groups = new ArrayList<>();
        for (Group group : getClient().getGroups())
            if (group.getUserById(this.getId()) != null)
                groups.add(group);
        return List.copyOf(groups);
    }

    default @NotNull Action<Group> joinGroup(@NotNull Group group) {
        return group.addUser(this);
    }

    default @NotNull Action<Group> leaveGroup(@NotNull Group group) {
        return group.removeUser(this);
    }

    /* - DISCORD - */

    @Key(name = "discord", relation = Relation.ONE_TO_MANY)
    @NotNull List<Long> getDiscordIds();

    @NotNull Action<User> addDiscordId(long discordId);

    @NotNull Action<User> removeDiscordId(long discordId);

    default @NotNull List<net.dv8tion.jda.api.entities.User> getDiscord() throws IllegalStateException {
        ArrayList<net.dv8tion.jda.api.entities.User> list = new ArrayList<>();
        JDA jda = this.getClient().getJDA();

        if (jda == null)
            throw new IllegalStateException("No JDA instance registered");

        for (Long discordId : this.getDiscordIds())
            list.add(jda.getUserById(discordId));

        return List.copyOf(list);
    }

    /* - MINECRAFT - */

    @Key(name = "minecraft", relation = Relation.ONE_TO_MANY)
    @NotNull List<UUID> getMinecraftIds();

    @NotNull Action<User> addMinecraftId(@NotNull UUID minecraftId);

    @NotNull Action<User> removeMinecraftId(@NotNull UUID minecraftId);

    default @NotNull List<OfflinePlayer> getMinecraft() throws IllegalStateException {
        ArrayList<OfflinePlayer> list = new ArrayList<>();
        Server server = this.getClient().getSpigotServer();

        if (server == null)
            throw new IllegalStateException("No server (bukkit) instance registered");

        for (UUID minecraftId : this.getMinecraftIds())
            list.add(server.getOfflinePlayer(minecraftId));

        return List.copyOf(list);
    }
}
