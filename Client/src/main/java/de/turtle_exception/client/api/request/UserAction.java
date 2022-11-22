package de.turtle_exception.client.api.request;

import de.turtle_exception.client.api.entities.User;
import net.dv8tion.jda.api.entities.ISnowflake;
import net.dv8tion.jda.api.entities.UserSnowflake;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collection;
import java.util.UUID;

@SuppressWarnings("unused")
public interface UserAction extends Action<User> {
    UserAction setName(String name);

    UserAction setDiscordIds(@NotNull Collection<Long> ids);

    default UserAction setDiscordUsers(@NotNull Collection<UserSnowflake> users) {
        return this.setDiscordIds(users.stream().map(ISnowflake::getIdLong).toList());
    }

    default UserAction setDiscordIds(@NotNull Long... ids) {
        return this.setDiscordIds(Arrays.asList(ids));
    }

    default UserAction setDiscordUsers(@NotNull UserSnowflake... users) {
        return this.setDiscordUsers(Arrays.asList(users));
    }

    UserAction addDiscordId(long id);

    default UserAction addDiscordUser(@NotNull UserSnowflake user) {
        return this.addDiscordId(user.getIdLong());
    }

    UserAction removeDiscordId(long id);

    default UserAction removeDiscordUser(@NotNull UserSnowflake user) {
        return this.removeDiscordId(user.getIdLong());
    }

    UserAction setMinecraftIds(@NotNull Collection<UUID> ids);

    default UserAction setMinecraftUsers(@NotNull Collection<OfflinePlayer> users) {
        return this.setMinecraftIds(users.stream().map(OfflinePlayer::getUniqueId).toList());
    }

    default UserAction setMinecraftIds(@NotNull UUID... ids) {
        return this.setMinecraftIds(Arrays.asList(ids));
    }

    default UserAction setMinecraftUsers(@NotNull OfflinePlayer... users) {
        return this.setMinecraftUsers(Arrays.asList(users));
    }

    UserAction addMinecraftId(UUID id);

    default UserAction addMinecraftUser(@NotNull OfflinePlayer user) {
        return this.addMinecraftId(user.getUniqueId());
    }

    UserAction removeMinecraftId(UUID id);

    default UserAction removeMinecraftUser(@NotNull OfflinePlayer user) {
        return this.removeMinecraftId(user.getUniqueId());
    }
}
