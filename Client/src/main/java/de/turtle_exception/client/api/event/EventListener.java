package de.turtle_exception.client.api.event;

import de.turtle_exception.client.api.event.entities.group.*;
import de.turtle_exception.client.api.event.entities.json_resource.*;
import de.turtle_exception.client.api.event.entities.messages.discord_channel.*;
import de.turtle_exception.client.api.event.entities.messages.minecraft_channel.*;
import de.turtle_exception.client.api.event.entities.messages.sync_channel.*;
import de.turtle_exception.client.api.event.entities.project.*;
import de.turtle_exception.client.api.event.entities.ticket.*;
import de.turtle_exception.client.api.event.entities.user.*;
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

    // JsonResource Events
    public void onJsonResourceCreate(@NotNull JsonResourceCreateEvent event) { }
    public void onJsonResourceDelete(@NotNull JsonResourceDeleteEvent event) { }
    public void onJsonResourceUpdate(@NotNull JsonResourceUpdateEvent<?> event) { }
    public void onJsonResourceUpdateIdentifier(@NotNull JsonResourceUpdateIdentifierEvent event) { }
    public void onJsonResourceUpdateContent(@NotNull JsonResourceUpdateContentEvent event) { }
    public void onJsonResourceUpdateEphemeral(@NotNull JsonResourceUpdateEphemeralEvent event) { }

    // Project Events
    public void onProjectCreate(@NotNull ProjectCreateEvent event) { }
    public void onProjectDelete(@NotNull ProjectDeleteEvent event) { }
    public void onProjectUpdate(@NotNull ProjectUpdateEvent<?> event) { }
    public void onProjectUpdateTitle(@NotNull ProjectUpdateTitleEvent event) { }
    public void onProjectUpdateCode(@NotNull ProjectUpdateCodeEvent event) { }
    public void onProjectUpdateState(@NotNull  ProjectUpdateStateEvent event) { }
    public void onProjectMemberJoin(@NotNull ProjectMemberJoinEvent event) { }
    public void onProjectMemberLeave(@NotNull ProjectMemberLeaveEvent event) { }

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

    /* - MESSAGES - */

    // DiscordChannel Events
    public void onDiscordChannelCreate(@NotNull DiscordChannelCreateEvent event) { }
    public void onDiscordChannelDelete(@NotNull DiscordChannelDeleteEvent event) { }
    public void onDiscordChannelUpdate(@NotNull DiscordChannelUpdateEvent<?> event) { }
    public void onDiscordChannelUpdateSyncChannel(@NotNull DiscordChannelUpdateSyncChannelEvent event) { }
    public void onDiscordChannelUpdateSnowflake(@NotNull DiscordChannelUpdateSnowflakeEvent event) { }

    // MinecraftChannel Events
    public void onMinecraftChannelCreate(@NotNull MinecraftChannelCreateEvent event) { }
    public void onMinecraftChannelDelete(@NotNull MinecraftChannelDeleteEvent event) { }
    public void onMinecraftChannelUpdate(@NotNull MinecraftChannelUpdateEvent<?> event) { }
    public void onMinecraftChannelUpdateSyncChannel(@NotNull MinecraftChannelUpdateSyncChannelEvent event) { }
    public void onMinecraftChannelUpdateType(@NotNull MinecraftChannelUpdateTypeEvent event) { }
    public void onMinecraftChannelUpdateIdentifier(@NotNull MinecraftChannelUpdateIdentifierEvent event) { }

    // SyncChannel Events
    public void onSyncChannelCreate(@NotNull SyncChannelCreateEvent event) { }
    public void onSyncChannelDelete(@NotNull SyncChannelDeleteEvent event) { }
    public void onSyncChannelUpdate(@NotNull SyncChannelUpdateEvent<?> event) { }
    public void onSyncChannelDiscordChannelAdd(@NotNull SyncChannelDiscordChannelAddEvent event) { }
    public void onSyncChannelDiscordChannelRemove(@NotNull SyncChannelDiscordChannelRemoveEvent event) { }
    public void onSyncChannelMinecraftChannelAdd(@NotNull SyncChannelMinecraftChannelAddEvent event) { }
    public void onSyncChannelMinecraftChannelRemove(@NotNull SyncChannelMinecraftChannelRemoveEvent event) { }

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
