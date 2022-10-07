package de.turtle_exception.client.internal.event;

import de.turtle_exception.client.api.entities.Group;
import de.turtle_exception.client.api.entities.Ticket;
import de.turtle_exception.client.api.entities.User;
import de.turtle_exception.client.api.event.EventManager;
import de.turtle_exception.client.api.event.group.GroupCreateEvent;
import de.turtle_exception.client.api.event.group.GroupMemberJoinEvent;
import de.turtle_exception.client.api.event.group.GroupMemberLeaveEvent;
import de.turtle_exception.client.api.event.group.GroupUpdateNameEvent;
import de.turtle_exception.client.api.event.ticket.*;
import de.turtle_exception.client.api.event.user.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class UpdateHelper {
    private UpdateHelper() { }

    public static void handleGroupUpdate(@Nullable Group oldGroup, @NotNull Group newGroup) {
        EventManager eventManager = newGroup.getClient().getEventManager();

        if (oldGroup == null) {
            eventManager.handleEvent(new GroupCreateEvent(newGroup));
            return;
        }

        /* NAME */
        if (!newGroup.getName().equals(oldGroup.getName()))
            eventManager.handleEvent(new GroupUpdateNameEvent(newGroup, oldGroup.getName(), newGroup.getName()));

        /* MEMBERS */
        List<User> oldUsers = oldGroup.getUsers();
        List<User> newUsers = newGroup.getUsers();
        for (User oldUser : oldUsers)
            if (!newUsers.contains(oldUser))
                eventManager.handleEvent(new GroupMemberLeaveEvent(newGroup, oldUser));
        for (User newUser : newUsers)
            if (!oldUsers.contains(newUser))
                eventManager.handleEvent(new GroupMemberJoinEvent(newGroup, newUser));
    }

    public static void handleUserUpdate(@Nullable User oldUser, @NotNull User newUser) {
        EventManager eventManager = newUser.getClient().getEventManager();

        if (oldUser == null) {
            eventManager.handleEvent(new UserCreateEvent(newUser));
            return;
        }

        /* NAME */
        if (!newUser.getName().equals(oldUser.getName()))
            eventManager.handleEvent(new UserUpdateNameEvent(newUser, oldUser.getName(), newUser.getName()));

        /* DISCORD */
        List<Long> oldDiscord = oldUser.getDiscordIds();
        List<Long> newDiscord = newUser.getDiscordIds();
        for (Long oldEntry : oldDiscord)
            if (!newDiscord.contains(oldEntry))
                eventManager.handleEvent(new UserDiscordRemoveEvent(newUser, oldEntry));
        for (Long newEntry : newDiscord)
            if (!oldDiscord.contains(newEntry))
                eventManager.handleEvent(new UserDiscordAddEvent(newUser, newEntry));

        /* MINECRAFT */
        List<UUID> oldMinecraft = oldUser.getMinecraftIds();
        List<UUID> newMinecraft = newUser.getMinecraftIds();
        for (UUID oldEntry : oldMinecraft)
            if (!newMinecraft.contains(oldEntry))
                eventManager.handleEvent(new UserMinecraftAddEvent(newUser, oldEntry));
        for (UUID newEntry : newMinecraft)
            if (!oldMinecraft.contains(newEntry))
                eventManager.handleEvent(new UserMinecraftRemoveEvent(newUser, newEntry));
    }

    public static void handleTicketUpdate(@Nullable Ticket oldTicket, @NotNull Ticket newTicket) {
        EventManager eventManager = newTicket.getClient().getEventManager();

        if (oldTicket == null) {
            eventManager.handleEvent(new TicketCreateEvent(newTicket));
            return;
        }

        /* STATE */
        if (newTicket.getState() != oldTicket.getState())
            eventManager.handleEvent(new TicketUpdateStateEvent(newTicket, oldTicket.getState(), newTicket.getState()));

        /* TITLE */
        // null safe
        if (!Objects.equals(newTicket.getTitle(), oldTicket.getTitle()))
            eventManager.handleEvent(new TicketUpdateTitleEvent(newTicket, oldTicket.getTitle(), newTicket.getTitle()));

        /* CATEGORY */
        if (!newTicket.getCategory().equals(oldTicket.getCategory()))
            eventManager.handleEvent(new TicketUpdateCategoryEvent(newTicket, oldTicket.getCategory(), newTicket.getCategory()));

        /* DISCORD */
        if (newTicket.getDiscordChannelId() != oldTicket.getDiscordChannelId())
            eventManager.handleEvent(new TicketUpdateDiscordChannelEvent(newTicket, oldTicket.getDiscordChannelId(), newTicket.getDiscordChannelId()));

        /* TAGS */
        List<String> oldTags = oldTicket.getTags();
        List<String> newTags = newTicket.getTags();
        for (String oldTag : oldTags)
            if (!newTags.contains(oldTag))
                eventManager.handleEvent(new TicketTagAddEvent(newTicket, oldTag));
        for (String newTag : newTags)
            if (!oldTags.contains(newTag))
                eventManager.handleEvent(new TicketTagRemoveEvent(newTicket, newTag));

        /* USERS */
        List<User> oldUsers = oldTicket.getUsers();
        List<User> newUsers = newTicket.getUsers();
        for (User oldUser : oldUsers)
            if (!newUsers.contains(oldUser))
                eventManager.handleEvent(new TicketUserAddEvent(newTicket, oldUser));
        for (User newUser : newUsers)
            if (!oldUsers.contains(newUser))
                eventManager.handleEvent(new TicketUserRemoveEvent(newTicket, newUser));
    }
}
