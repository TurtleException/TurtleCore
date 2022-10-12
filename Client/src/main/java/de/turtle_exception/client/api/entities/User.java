package de.turtle_exception.client.api.entities;

import de.turtle_exception.client.api.requests.Action;
import net.dv8tion.jda.api.JDA;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@SuppressWarnings("unused")
public interface User extends Turtle {
    @NotNull String getName();

    @NotNull Action<Void> modifyName(@NotNull String name);

    /* - GROUPS - */

    default @NotNull List<Group> getGroups() {
        ArrayList<Group> groups = new ArrayList<>();
        for (Group group : getClient().getGroups())
            if (group.getUserById(this.getId()) != null)
                groups.add(group);
        return List.copyOf(groups);
    }

    default @NotNull Action<Void> joinGroup(@NotNull Group group) {
        return group.addUser(this);
    }

    default @NotNull Action<Void> leaveGroup(@NotNull Group group) {
        return group.removeUser(this);
    }

    /* - DISCORD - */

    @NotNull List<Long> getDiscordIds();

    @NotNull Action<Void> addDiscordId(long discordId);

    @NotNull Action<Void> removeDiscordId(long discordId);

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

    @NotNull List<UUID> getMinecraftIds();

    @NotNull Action<Void> addMinecraftId(@NotNull UUID minecraftId);

    @NotNull Action<Void> removeMinecraftId(@NotNull UUID minecraftId);

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
