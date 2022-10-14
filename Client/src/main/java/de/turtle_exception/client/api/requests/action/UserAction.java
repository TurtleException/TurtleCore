package de.turtle_exception.client.api.requests.action;

import de.turtle_exception.client.api.entities.User;
import de.turtle_exception.client.api.requests.Action;
import net.dv8tion.jda.api.entities.UserSnowflake;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

@SuppressWarnings("unused")
public interface UserAction extends Action<User> {
    @NotNull UserAction setName(@NotNull String name);

    @NotNull UserAction addDiscord(long id);

    default @NotNull UserAction addDiscord(@NotNull UserSnowflake user) {
        return this.addDiscord(user.getIdLong());
    }

    @NotNull UserAction addMinecraft(@NotNull UUID id);

    default @NotNull UserAction addMinecraft(@NotNull OfflinePlayer player) {
        return this.addMinecraft(player.getUniqueId());
    }
}
