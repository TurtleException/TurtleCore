package de.turtle_exception.core.client.api.event;

import de.turtle_exception.core.client.api.event.group.GroupUpdateNameEvent;
import de.turtle_exception.core.client.api.event.user.UserGroupJoinEvent;
import de.turtle_exception.core.client.api.event.user.UserGroupLeaveEvent;
import de.turtle_exception.core.client.api.event.user.UserUpdateNameEvent;
import de.turtle_exception.core.netcore.util.StringUtil;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;
import java.util.logging.Level;

public abstract class EventListener {
    public void onGenericEvent(@NotNull Event event) { }

    // Group Events
    public void onGroupUpdateName(@NotNull GroupUpdateNameEvent event) { }

    // User Events
    public void onUserUpdateName(@NotNull UserUpdateNameEvent event) { }
    public void onUserGroupJoin(@NotNull UserGroupJoinEvent event) { }
    public void onUserGroupLeave(@NotNull UserGroupLeaveEvent event) { }

    /* - - - */

    public final <T extends Event> void onEvent(@NotNull T event) {
        this.onGenericEvent(event);

        final String methodName = "on" + StringUtil.cutEnd(event.getClass().getSimpleName(), "Event".length());

        for (Method method : this.getClass().getDeclaredMethods()) {
            if (method.getName().equals(methodName)) {
                try {
                    method.invoke(this, event);
                } catch (Throwable t) {
                    event.getClient().getLogger().log(Level.WARNING, "An EventListener threw an unexpected exception.", t);
                }
            }
        }
    }
}
