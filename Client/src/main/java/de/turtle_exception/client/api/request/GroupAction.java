package de.turtle_exception.client.api.request;

import de.turtle_exception.client.api.TurtleClient;
import de.turtle_exception.client.api.entities.Group;
import de.turtle_exception.client.api.entities.User;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collection;

/**
 * A GroupAction is an Action that requests the creation of a new {@link Group}, according to the arguments this Action
 * holds. If any required field is missing the server will reject the request and respond with an error. Required fields
 * are all attributes that are not a subclass of {@link Collection}, as these are set to an empty Collection by default.
 * @see TurtleClient#createGroup()
 */
@SuppressWarnings("unused")
public interface GroupAction extends Action<Group> {
    /**
     * Sets the name of this Group to the provided String.
     * @param name Group name.
     * @return This GroupAction for chaining convenience.
     */
    GroupAction setName(String name);

    /**
     * Sets the List of ids that each represent a {@link User} as members of this Group.
     * The existing List will be overwritten.
     * @param users Collection of User ids.
     * @return This GroupAction for chaining convenience.
     */
    GroupAction setUserIds(@NotNull Collection<Long> users);

    /**
     * Sets the List of {@link User Users} that are members of this Group.
     * The existing List will be overwritten.
     * @param users Collection of Users.
     * @return This GroupAction for chaining convenience.
     */
    default GroupAction setUsers(@NotNull Collection<User> users) {
        return this.setUserIds(users.stream().map(User::getId).toList());
    }

    /**
     * Sets the List of ids that each represent a {@link User} as members of this Group.
     * The existing List will be overwritten.
     * @param users Array of User ids.
     * @return This GroupAction for chaining convenience.
     */
    default GroupAction setUserIds(@NotNull Long... users) {
        return this.setUserIds(Arrays.asList(users));
    }

    /**
     * Sets the List of {@link User Users} that are members of this Group.
     * The existing List will be overwritten.
     * @param users Array of Users.
     * @return This GroupAction for chaining convenience.
     */
    default GroupAction setUsers(@NotNull User... users) {
        return this.setUsers(Arrays.asList(users));
    }

    /**
     * Adds the provided {@code long} to the List of ids that each represent a {@link User} as a member of this Group.
     * @param user User id.
     * @return This GroupAction for chaining convenience.
     */
    GroupAction addUserId(long user);

    /**
     * Adds the provided {@link User} to the List Users that are members of this Group.
     * @param user Some User.
     * @return This GroupAction for chaining convenience.
     */
    default GroupAction addUser(@NotNull User user) {
        return this.addUserId(user.getId());
    }

    /**
     * Removes the provided {@code long} from the List of ids that each represent a {@link User} as a member of this Group.
     * @param user User id.
     * @return This GroupAction for chaining convenience.
     */
    GroupAction removeUseId(long user);

    /**
     * Removes the provided {@link User} from the List Users that are members of this Group.
     * @param user Some User.
     * @return This GroupAction for chaining convenience.
     */
    default GroupAction removeUser(@NotNull User user) {
        return this.removeUseId(user.getId());
    }
}
