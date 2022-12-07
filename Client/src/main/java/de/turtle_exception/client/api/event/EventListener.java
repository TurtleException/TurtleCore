package de.turtle_exception.client.api.event;

import de.turtle_exception.client.api.event.entities.EphemeralEntityEvent;
import de.turtle_exception.client.api.event.entities.form.completed_form.CompletedFormCreateEvent;
import de.turtle_exception.client.api.event.entities.form.completed_form.CompletedFormDeleteEvent;
import de.turtle_exception.client.api.event.entities.form.query_element.*;
import de.turtle_exception.client.api.event.entities.form.query_response.QueryResponseCreateEvent;
import de.turtle_exception.client.api.event.entities.form.query_response.QueryResponseDeleteEvent;
import de.turtle_exception.client.api.event.entities.form.template_form.TemplateFormCreateEvent;
import de.turtle_exception.client.api.event.entities.form.template_form.TemplateFormDeleteEvent;
import de.turtle_exception.client.api.event.entities.form.text_element.*;
import de.turtle_exception.client.api.event.entities.group.*;
import de.turtle_exception.client.api.event.entities.json_resource.*;
import de.turtle_exception.client.api.event.entities.messages.discord_channel.*;
import de.turtle_exception.client.api.event.entities.messages.minecraft_channel.*;
import de.turtle_exception.client.api.event.entities.messages.sync_channel.*;
import de.turtle_exception.client.api.event.entities.messages.sync_message.*;
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

    // EPHEMERAL ENTITY
    public void onEphemeralEntity(@NotNull EphemeralEntityEvent<?> event) { }

    /* - ENTITIES - */

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
    public void onProjectUpdateApplicationForm(@NotNull ProjectUpdateApplicationFormEvent event) { }
    public void onProjectUpdateTimeRelease(@NotNull ProjectUpdateTimeReleaseEvent event) { }
    public void onProjectUpdateTimeApply(@NotNull ProjectUpdateTimeApplyEvent event) { }
    public void onProjectUpdateTimeStart(@NotNull ProjectUpdateTimeStartEvent event) { }
    public void onProjectUpdateTimeEnd(@NotNull ProjectUpdateTimeEndEvent event) { }

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

    /* - FORM - */

    // CompletedForm Events
    public void onCompletedFormCreate(@NotNull CompletedFormCreateEvent event) { }
    public void onCompletedFormDelete(@NotNull CompletedFormDeleteEvent event) { }

    // QueryElement Events
    public void onQueryElementCreate(@NotNull QueryElementCreateEvent event) { }
    public void onQueryElementDelete(@NotNull QueryElementDeleteEvent event) { }
    public void onQueryElementUpdate(@NotNull QueryElementUpdateEvent<?> event) { }
    public void onQueryElementUpdateTitle(@NotNull QueryElementUpdateTitleEvent event) { }
    public void onQueryElementUpdateDescription(@NotNull QueryElementUpdateDescriptionEvent event) { }
    public void onQueryElementUpdateRequired(@NotNull QueryElementUpdateRequiredEvent event) { }

    // QueryResponse Events
    public void onQueryResponseCreate(@NotNull QueryResponseCreateEvent event) { }
    public void onQueryResponseDelete(@NotNull QueryResponseDeleteEvent event) { }

    // TemplateForm Events
    public void onTemplateFormCreate(@NotNull TemplateFormCreateEvent event) { }
    public void onTemplateFormDelete(@NotNull TemplateFormDeleteEvent event) { }

    // TextElement Events
    public void onTextElementCreate(@NotNull TextElementCreateEvent event) { }
    public void onTextElementDelete(@NotNull TextElementDeleteEvent event) { }
    public void onTextElementUpdate(@NotNull TextElementUpdateEvent<?> event) { }
    public void onTextElementUpdateTitle(@NotNull TextElementUpdateTitleEvent event) { }
    public void onTextElementUpdateContent(@NotNull TextElementUpdateContentEvent event) { }

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

    // SyncMessage Events
    public void onSyncMessageCreate(@NotNull SyncMessageCreateEvent event) { }
    public void onSyncMessageDelete(@NotNull SyncMessageDeleteEvent event) { }
    public void onSyncMessageUpdate(@NotNull SyncMessageUpdateEvent<?> event) { }
    public void onSyncMessageUpdateFormat(@NotNull SyncMessageUpdateFormatEvent event) { }
    public void onSyncMessageUpdateAuthor(@NotNull SyncMessageUpdateAuthorEvent event) { }
    public void onSyncMessageUpdateContent(@NotNull SyncMessageUpdateContentEvent event) { }
    public void onSyncMessageUpdateReference(@NotNull SyncMessageUpdateReferenceEvent event) { }
    public void onSyncMessageUpdateChannel(@NotNull SyncMessageUpdateChannelEvent event) { }
    public void onSyncMessageUpdateSource(@NotNull SyncMessageUpdateSourceEvent event) { }

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
