package de.turtle_exception.client.api.request.entities;

import de.turtle_exception.client.api.TurtleClient;
import de.turtle_exception.client.api.entities.User;
import de.turtle_exception.client.api.request.Action;
import net.dv8tion.jda.api.entities.ISnowflake;
import net.dv8tion.jda.api.entities.UserSnowflake;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collection;
import java.util.UUID;

/**
 * A UserAction is an Action that requests the creation of a new {@link User}, according to the arguments this Action
 * holds. If any required field is missing the server will reject the request and respond with an error. Required fields
 * are all attributes that are not a subclass of {@link Collection}, as these are set to an empty Collection by default.
 * @see TurtleClient#createUser()
 */
@SuppressWarnings("unused")
public interface UserAction extends Action<User> {
    /**
     * Sets the name of this User to the provided String.
     * @param name Username.
     * @return This UserAction for chaining convenience.
     */
    UserAction setName(String name);

    /**
     * Sets the List of ids that each represent a Discord user that is exclusively linked to this User.
     * The existing List will be overwritten.
     * @param ids Collection of User ids.
     * @return This UserAction for chaining convenience.
     */
    UserAction setDiscordIds(@NotNull Collection<Long> ids);

    /**
     * Sets the List of {@link UserSnowflake UserSnowflakes} that are exclusively linked to this User.
     * The existing List will be overwritten.
     * @param users Collection of Discord Users.
     * @return This UserAction for chaining convenience.
     */
    default UserAction setDiscordUsers(@NotNull Collection<UserSnowflake> users) {
        return this.setDiscordIds(users.stream().map(ISnowflake::getIdLong).toList());
    }

    /**
     * Sets the List of ids that each represent a Discord user that is exclusively linked to this User.
     * The existing List will be overwritten.
     * @param ids Array of User ids.
     * @return This UserAction for chaining convenience.
     */
    default UserAction setDiscordIds(@NotNull Long... ids) {
        return this.setDiscordIds(Arrays.asList(ids));
    }

    /**
     * Sets the List of {@link UserSnowflake UserSnowflakes} that are exclusively linked to this User.
     * The existing List will be overwritten.
     * @param users Array of Discord Users.
     * @return This UserAction for chaining convenience.
     */
    default UserAction setDiscordUsers(@NotNull UserSnowflake... users) {
        return this.setDiscordUsers(Arrays.asList(users));
    }

    /**
     * Adds the provided {@code long} to the List of ids that each represent a Discord user that is exclusively linked
     * to this User.
     * @param id Discord User id.
     * @return This UserAction for chaining convenience.
     */
    UserAction addDiscordId(long id);

    /**
     * Adds the provided {@link UserSnowflake} to the List of Discord users that are exclusively linked to this User.
     * @param user Some Discord User.
     * @return This UserAction for chaining convenience.
     */
    default UserAction addDiscordUser(@NotNull UserSnowflake user) {
        return this.addDiscordId(user.getIdLong());
    }

    /**
     * Removes the provided {@code long} from the List of ids that each represent a Discord user that is exclusively
     * linked to this User.
     * @param id Discord User id.
     * @return This UserAction for chaining convenience.
     */
    UserAction removeDiscordId(long id);

    /**
     * Removes the provided {@link UserSnowflake} from the List of Discord users that are exclusively linked to this User.
     * @param user Some Discord User.
     * @return This UserAction for chaining convenience.
     */
    default UserAction removeDiscordUser(@NotNull UserSnowflake user) {
        return this.removeDiscordId(user.getIdLong());
    }

    /**
     * Sets the List of {@link UUID UUIDs} that each represent a Minecraft Account that is exclusively linked to this User.
     * The existing List will be overwritten.
     * @param ids Collection of UUIDs.
     * @return This UserAction for chaining convenience.
     */
    UserAction setMinecraftIds(@NotNull Collection<UUID> ids);

    /**
     * Sets the List of {@link OfflinePlayer OfflinePlayers} that are exclusively linked to this User.
     * The existing List will be overwritten.
     * @param users Collection of Minecraft Players.
     * @return This UserAction for chaining convenience.
     */
    default UserAction setMinecraftUsers(@NotNull Collection<OfflinePlayer> users) {
        return this.setMinecraftIds(users.stream().map(OfflinePlayer::getUniqueId).toList());
    }

    /**
     * Sets the List of {@link UUID UUIDs} that each represent a Minecraft Account that is exclusively linked to this User.
     * The existing List will be overwritten.
     * @param ids Array of UUIDs.
     * @return This UserAction for chaining convenience.
     */
    default UserAction setMinecraftIds(@NotNull UUID... ids) {
        return this.setMinecraftIds(Arrays.asList(ids));
    }

    /**
     * Sets the List of {@link OfflinePlayer OfflinePlayers} that are exclusively linked to this User.
     * The existing List will be overwritten.
     * @param users Array of Minecraft Players.
     * @return This UserAction for chaining convenience.
     */
    default UserAction setMinecraftUsers(@NotNull OfflinePlayer... users) {
        return this.setMinecraftUsers(Arrays.asList(users));
    }

    /**
     * Adds the provided {@link UUID} to the List of ids that each represent a Minecraft Account that is exclusively
     * linked to this User.
     * @param id Minecraft Account id.
     * @return This UserAction for chaining convenience.
     */
    UserAction addMinecraftId(UUID id);

    /**
     * Adds the provided {@link OfflinePlayer} to the List of Minecraft Accounts that are exclusively linked to this User.
     * @param user Some Minecraft Player.
     * @return This UserAction for chaining convenience.
     */
    default UserAction addMinecraftUser(@NotNull OfflinePlayer user) {
        return this.addMinecraftId(user.getUniqueId());
    }

    /**
     * Removes the provided {@link UUID} from the List of ids that each represent a Minecraft Account that is
     * exclusively linked to this User.
     * @param id Minecraft Account id.
     * @return This UserAction for chaining convenience.
     */
    UserAction removeMinecraftId(UUID id);

    /**
     * Removes the provided {@link OfflinePlayer} from the List of Minecraft Accounts that are exclusively linked to
     * this User.
     * @param user Some Minecraft Player.
     * @return This UserAction for chaining convenience.
     */
    default UserAction removeMinecraftUser(@NotNull OfflinePlayer user) {
        return this.removeMinecraftId(user.getUniqueId());
    }
}
