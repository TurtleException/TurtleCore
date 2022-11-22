package de.turtle_exception.client.api.entities;

import de.turtle_exception.client.internal.data.annotations.Keys;
import de.turtle_exception.client.api.entities.attribute.IUserContainer;
import de.turtle_exception.client.api.request.Action;
import de.turtle_exception.client.internal.data.annotations.Key;
import de.turtle_exception.client.internal.data.annotations.Relation;
import de.turtle_exception.client.internal.data.annotations.Resource;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * A Group is a collection of {@link User Users} with collective attributes. Groups can be used to categorize Users or
 * to simplify permission handling.
 */
@Resource(path = "groups", builder = "buildGroup")
@SuppressWarnings("unused")
public interface Group extends Turtle, IUserContainer {
    @Override
    default @NotNull Action<Group> update() {
        return this.getClient().retrieveGroup(this.getId());
    }

    /* - NAME - */

    /**
     * Provides the name of this Group. Group names are not guaranteed to be unique and rather function as a description.
     * Uniqueness can only be checked by {@link Group#getId()}.
     * @return The Group name.
     */
    @Key(name = Keys.Group.NAME)
    @NotNull String getName();

    /**
     * Creates an Action with the instruction to modify this Group's name and change it to the provided String.
     * @param name New Group name.
     * @return Action that provides the modified {@link Group} on completion.
     */
    @NotNull Action<Group> modifyName(@NotNull String name);

    /* - USERS - */

    /**
     * Provides a List of all {@link User Users} that are a member of this Group.
     * <p> A Group can have multiple Users; A User can also be part of multiple Groups.
     * @return List of members.
     */
    @Key(name = Keys.Group.MEMBERS, relation = Relation.MANY_TO_MANY)
    @NotNull List<User> getUsers();

    /**
     * Creates an Action with the instruction to add the provided id to the list of group members.
     * <p> The provided {@code long} should be a representation of a {@link User} id.
     * @param user Turtle ID of a User.
     * @return Action that provides the modified {@link Group} on completion.
     */
    @NotNull Action<Group> addUser(long user);

    /**
     * Creates an Action with the instruction to add the provided {@link User} to the list of group members.
     * <p> This is a shortcut for {@code Group.addUser(user.getId())}.
     * @param user A User.
     * @return Action that provides the modified {@link Group} on completion.
     */
    default @NotNull Action<Group> addUser(@NotNull User user) {
        return this.addUser(user.getId());
    }

    /**
     * Creates an Action with the instruction to remove the provided id from the list of group members.
     * <p> The provided {@code long} should be a representation of a {@link User} id.
     * @param user Turtle ID of a User.
     * @return Action that provides the modified {@link Group} on completion.
     */
    @NotNull Action<Group> removeUser(long user);

    /**
     * Creates an Action with the instruction to remove the provided {@link User} from the list of group members.
     * <p> This is a shortcut for {@code Group.removeUser(user.getId())}.
     * @param user A User.
     * @return Action that provides the modified {@link Group} on completion.
     */
    default @NotNull Action<Group> removeUser(@NotNull User user) {
        return this.removeUser(user.getId());
    }
}
