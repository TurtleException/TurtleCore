package de.turtle_exception.client.api.entities;

import de.turtle_exception.client.api.TurtleClient;
import de.turtle_exception.client.internal.data.annotations.*;
import de.turtle_exception.client.api.request.Action;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * A user is a member of the community, part of the team or moderation or a bot. Users can have an unspecified amount of
 * Discord and Minecraft accounts linked to them (exclusively). A custom name can be set by the user themselves.
 * @see Group
 * @see Ticket
 */
@Resource(path = "users", builder = "buildUser")
@SuppressWarnings("unused")
public interface User extends Turtle {
    @Override
    default @NotNull Action<User> update() {
        return this.getClient().retrieveUser(this.getId());
    }

    /* - NAME - */

    /**
     * Provides the name of this User. Usernames are not guaranteed to be unique and can be set by the User themselves.
     * @return The username.
     */
    @Key(name = Keys.User.NAME, sqlType = Types.User.NAME)
    @NotNull String getName();

    /**
     * Creates an Action with the instruction to modify this User's name and change it to the provided String.
     * @param name New username.
     * @return Action that provides the modified {@link User} on completion.
     */
    @NotNull Action<User> modifyName(@NotNull String name);

    /* - GROUPS - */

    /**
     * Convenience-method that provides a List of all {@link Group Groups} that this User is a member of.
     * <p> A Group can have multiple Users; A User can also be part of multiple Groups.
     * <p> The performance impact of this method is heavier than that of {@link Group#getUsers()}, since it scans all
     * available Groups to check for this User. The actual data lies with the Groups. If there is a choice between both
     * methods (e.g. to check whether a specific User is member of a specific Group) the implementation in {@link Group}
     * is recommended.
     * @return List of Groups.
     */
    default @NotNull List<Group> getGroups() {
        ArrayList<Group> groups = new ArrayList<>();
        for (Group group : getClient().getGroups())
            if (group.getUserById(this.getId()) != null)
                groups.add(group);
        return List.copyOf(groups);
    }

    /**
     * Creates an Action with the instruction to add this User to the member-list of the provided Group.
     * <p> This is a shortcut for {@code group.addUser(this)}.
     * @param group A Group.
     * @return Action that provides the modified {@link Group} on completion.
     */
    default @NotNull Action<Group> joinGroup(@NotNull Group group) {
        return group.addUser(this);
    }

    /**
     * Creates an Action with the instruction to remove this User from the member-list of the provided Group.
     * <p> This is a shortcut for {@code group.removeUser(this)}.
     * @param group A Group.
     * @return Action that provides the modified {@link Group} on completion.
     */
    default @NotNull Action<Group> leaveGroup(@NotNull Group group) {
        return group.removeUser(this);
    }

    /* - DISCORD - */

    /**
     * Provides a List of snowflake ids that each represent a Discord user this User is linked to (exclusively).
     * @return List of snowflake ids.
     */
    @Key(name = Keys.User.DISCORD, relation = Relation.ONE_TO_MANY, type = Long.class, sqlType = Types.User.DISCORD)
    @Relational(table = "user_discord", self = "user", foreign = "discord")
    @NotNull List<Long> getDiscordIds();

    /**
     * Creates an Action with the instruction to add the provided id to the list of snowflake ids that each represent a
     * Discord user this User is linked to (exclusively).
     * <p> The provided {@code long} should be a representation of a Discord user id (snowflake).
     * @param discordId Snowflake ID of a Discord User.
     * @return Action that provides the modified {@link User} on completion.
     */
    @NotNull Action<User> addDiscordId(long discordId);

    /**
     * Creates an Action with the instruction to remove the provided id from the list of snowflake ids that each
     * represent a Discord user this User is linked to (exclusively).
     * <p> The provided {@code long} should be a representation of a Discord user id (snowflake).
     * @param discordId Snowflake ID of a Discord User.
     * @return Action that provides the modified {@link User} on completion.
     */
    @NotNull Action<User> removeDiscordId(long discordId);

    /**
     * Provides a List of {@link net.dv8tion.jda.api.entities.User JDA User objects} this User is linked to (exclusively).
     * @return List of Discord users.
     * @throws IllegalStateException if no {@link JDA} instance has been registered to the {@link TurtleClient}.
     */
    default @NotNull List<net.dv8tion.jda.api.entities.User> getDiscord() throws IllegalStateException {
        ArrayList<net.dv8tion.jda.api.entities.User> list = new ArrayList<>();
        JDA jda = this.getClient().getJDA();

        if (jda == null)
            throw new IllegalStateException("No JDA instance registered");

        for (Long discordId : this.getDiscordIds()) {
            net.dv8tion.jda.api.entities.User user = jda.getUserById(discordId);
            if (user == null) continue;
            list.add(user);
        }

        return List.copyOf(list);
    }

    /**
     * Provides a List of {@link Member JDA Member objects} from the specified {@link Guild} this User is linked to (exclusively).
     * @param guild A Discord Guild.
     * @return List of Discord Members.
     */
    default @NotNull List<Member> getDiscord(@NotNull Guild guild) {
        ArrayList<Member> list = new ArrayList<>();

        for (Long discordId : this.getDiscordIds()) {
            Member member = guild.getMemberById(discordId);
            if (member == null) continue;
            list.add(member);
        }

        return List.copyOf(list);
    }

    /* - MINECRAFT - */

    /**
     * Provides a List of {@link UUID UUIDs} that each represent a Minecraft account this User is linked to (exclusively).
     * @return List of {@link UUID UUIDs}.
     */
    @Key(name = Keys.User.MINECRAFT, relation = Relation.ONE_TO_MANY, type = UUID.class, sqlType = Types.User.MINECRAFT)
    @Relational(table = "user_minecraft", self = "user", foreign = "minecraft")
    @NotNull List<UUID> getMinecraftIds();

    /**
     * Creates an Action with the instruction to add the provided {@link UUID} to the list of ids that each represent a
     * Minecraft account this User is linked to (exclusively).
     * <p> The provided {@link UUID} should be a representation of a Minecraft account.
     * @param minecraftId Unique id of a Minecraft account.
     * @return Action that provides the modified {@link User} on completion.
     */
    @NotNull Action<User> addMinecraftId(@NotNull UUID minecraftId);

    /**
     * Creates an Action with the instruction to remove the provided {@link UUID} from the list of ids that each
     * represent a Minecraft account this User is linked to (exclusively).
     * <p> The provided {@link UUID} should be a representation of a Minecraft account.
     * @param minecraftId Unique id of a Minecraft account.
     * @return Action that provides the modified {@link User} on completion.
     */
    @NotNull Action<User> removeMinecraftId(@NotNull UUID minecraftId);

    /**
     * Provides a List of {@link OfflinePlayer OfflinePlayers} this User is linked to (exclusively).
     * @return List of bukkit representations of Minecraft accounts.
     * @throws IllegalStateException if no {@link Server} (bukkit) instance has been registered to the {@link TurtleClient}.
     */
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
