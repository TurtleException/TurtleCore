package de.turtle_exception.client.api.event;

import de.turtle_exception.client.api.event.entities.group.*;
import de.turtle_exception.client.api.event.entities.ticket.*;
import de.turtle_exception.client.api.event.entities.user.*;
import de.turtle_exception.client.api.event.net.RequestEvent;
import de.turtle_exception.client.api.event.net.ResponseEvent;
import de.turtle_exception.client.internal.util.StringUtil;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;
import java.util.logging.Level;

@SuppressWarnings("unused")
public abstract class EventListener {
    public void onGenericEvent(@NotNull Event event) { }

    // Group Events
    public void onGroupCreate(@NotNull GroupCreateEvent event) { }
    public void onGroupDelete(@NotNull GroupDeleteEvent event) { }
    public void onGroupUpdate(@NotNull GroupUpdateEvent<?> event) { }
    public void onGroupUpdateName(@NotNull GroupUpdateNameEvent event) { }
    public void onGroupMemberJoin(@NotNull GroupMemberJoinEvent event) { }
    public void onGroupMemberLeave(@NotNull GroupMemberLeaveEvent event) { }

    // User Events
    public void onUserCreate(@NotNull UserCreateEvent event) { }
    public void onUserDelete(@NotNull UserDeleteEvent event) { }
    public void onUserUpdate(@NotNull UserUpdateEvent<?> event) { }
    public void onUserUpdateName(@NotNull UserUpdateNameEvent event) { }
    public void onUserDiscordAdd(@NotNull UserDiscordAddEvent event) { }
    public void onUserDiscordRemove(@NotNull UserDiscordRemoveEvent event) { }
    public void onUserMinecraftAdd(@NotNull UserMinecraftAddEvent event) { }
    public void onUserMinecraftRemove(@NotNull UserMinecraftRemoveEvent event) { }

    // Ticket Events
    public void onTicketCreate(@NotNull TicketCreateEvent event) { }
    public void onTicketDelete(@NotNull TicketDeleteEvent event) { }
    public void onTicketUpdate(@NotNull TicketUpdateEvent<?> event) { }
    public void onTicketUpdateState(@NotNull TicketUpdateStateEvent event) { }
    public void onTicketUpdateTitle(@NotNull TicketUpdateTitleEvent event) { }
    public void onTicketUpdateCategory(@NotNull TicketUpdateCategoryEvent event) { }
    public void onTicketUpdateDiscordChannel(@NotNull TicketUpdateDiscordChannelEvent event) { }
    public void onTicketTagAdd(@NotNull TicketTagAddEvent event) { }
    public void onTicketTagRemove(@NotNull TicketTagRemoveEvent event) { }
    public void onTicketUserAdd(@NotNull TicketUserAddEvent event) { }
    public void onTicketUserRemove(@NotNull TicketUserRemoveEvent event) { }

    // Network
    public void onRequest(@NotNull RequestEvent event) { }
    public void onResponse(@NotNull ResponseEvent event) { }

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
