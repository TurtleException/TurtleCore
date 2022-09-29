package de.turtle_exception.core.client.api.event;

import de.turtle_exception.core.client.api.event.group.GroupUpdateNameEvent;
import de.turtle_exception.core.client.api.event.user.UserGroupJoinEvent;
import de.turtle_exception.core.client.api.event.user.UserGroupLeaveEvent;
import de.turtle_exception.core.client.api.event.user.UserUpdateNameEvent;
import org.jetbrains.annotations.NotNull;

public abstract class EventListener {
    public void onGenericEvent(@NotNull Event event) { }

    // Group Events
    public void onGroupUpdateName(@NotNull GroupUpdateNameEvent event) { }

    // User Events
    public void onUserUpdateName(@NotNull UserUpdateNameEvent event) { }
    public void onUserGroupJoin(@NotNull UserGroupJoinEvent event) { }
    public void onUserGroupLeave(@NotNull UserGroupLeaveEvent event) { }
}
