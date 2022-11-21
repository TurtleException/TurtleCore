package de.turtle_exception.client.api.request;

import de.turtle_exception.client.api.entities.Group;
import de.turtle_exception.client.api.entities.User;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collection;

@SuppressWarnings("unused")
public interface GroupAction extends Action<Group> {
    GroupAction setName(String name);

    GroupAction setUserIds(@NotNull Collection<Long> users);

    default GroupAction setUsers(@NotNull Collection<User> users) {
        return this.setUserIds(users.stream().map(User::getId).toList());
    }

    default GroupAction setUserIds(@NotNull Long... users) {
        return this.setUserIds(Arrays.asList(users));
    }

    default GroupAction setUsers(@NotNull User... users) {
        return this.setUsers(Arrays.asList(users));
    }

    GroupAction addUserId(long user);

    default GroupAction addUser(@NotNull User user) {
        return this.addUserId(user.getId());
    }

    GroupAction removeUseId(long user);

    default GroupAction removeUser(@NotNull User user) {
        return this.removeUseId(user.getId());
    }
}
