package de.turtle_exception.client.internal.event;

import de.turtle_exception.client.api.entities.Group;
import de.turtle_exception.client.api.entities.Ticket;
import de.turtle_exception.client.api.entities.Turtle;
import de.turtle_exception.client.api.entities.User;
import de.turtle_exception.client.api.event.entities.group.GroupCreateEvent;
import de.turtle_exception.client.api.event.entities.group.GroupDeleteEvent;
import de.turtle_exception.client.api.event.entities.group.GroupMemberJoinEvent;
import de.turtle_exception.client.api.event.entities.group.GroupMemberLeaveEvent;
import de.turtle_exception.client.api.event.entities.ticket.*;
import de.turtle_exception.client.api.event.entities.user.*;
import de.turtle_exception.client.internal.util.TurtleSet;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Set;
import java.util.UUID;

// TODO: this can probably be simplified
public class UpdateHelper {
    private UpdateHelper() { }

    public static void ofCreateTurtle(@NotNull Turtle turtle) {
        if (turtle instanceof Group group)
            turtle.getClient().getEventManager().handleEvent(new GroupCreateEvent(group));
        if (turtle instanceof Ticket ticket)
            turtle.getClient().getEventManager().handleEvent(new TicketCreateEvent(ticket));
        if (turtle instanceof User user)
            turtle.getClient().getEventManager().handleEvent(new UserCreateEvent(user));
    }

    public static void ofDeleteTurtle(@NotNull Turtle turtle) {
        if (turtle instanceof Group group)
            turtle.getClient().getEventManager().handleEvent(new GroupDeleteEvent(group));
        if (turtle instanceof Ticket ticket)
            turtle.getClient().getEventManager().handleEvent(new TicketDeleteEvent(ticket));
        if (turtle instanceof User user)
            turtle.getClient().getEventManager().handleEvent(new UserDeleteEvent(user));
    }

    /* - GROUP - */

    public static void ofGroupMembers(@NotNull Group group, @NotNull TurtleSet<User> oldUsers, @NotNull TurtleSet<User> newUsers) {
        List<User> added   = newUsers.stream().filter(user -> !oldUsers.containsId(user.getId())).toList();
        List<User> removed = oldUsers.stream().filter(user -> !newUsers.containsId(user.getId())).toList();

        for (User newUser : added)
            group.getClient().getEventManager().handleEvent(new GroupMemberJoinEvent(group, newUser));
        for (User oldUser : removed)
            group.getClient().getEventManager().handleEvent(new GroupMemberLeaveEvent(group, oldUser));
    }

    /* - TICKET - */

    public static void ofTicketTags(@NotNull Ticket ticket, @NotNull Set<String> oldTags, @NotNull Set<String> newTags) {
        List<String> added   = newTags.stream().filter(tag -> !oldTags.contains(tag)).toList();
        List<String> removed = oldTags.stream().filter(tag -> !newTags.contains(tag)).toList();

        for (String newTag : added)
            ticket.getClient().getEventManager().handleEvent(new TicketTagAddEvent(ticket, newTag));
        for (String oldTag : removed)
            ticket.getClient().getEventManager().handleEvent(new TicketTagRemoveEvent(ticket, oldTag));
    }

    public static void ofTicketUsers(@NotNull Ticket ticket, @NotNull TurtleSet<User> oldUsers, @NotNull TurtleSet<User> newUsers) {
        List<User> added   = newUsers.stream().filter(user -> !oldUsers.containsId(user.getId())).toList();
        List<User> removed = oldUsers.stream().filter(user -> !newUsers.containsId(user.getId())).toList();

        for (User newUser : added)
            ticket.getClient().getEventManager().handleEvent(new TicketUserAddEvent(ticket, newUser));
        for (User oldUser : removed)
            ticket.getClient().getEventManager().handleEvent(new TicketUserRemoveEvent(ticket, oldUser));
    }

    /* - USER - */

    public static void ofUserDiscord(@NotNull User user, @NotNull List<Long> oldDiscordSet, @NotNull List<Long> newDiscordSet) {
        List<Long> added   = newDiscordSet.stream().filter(l -> !oldDiscordSet.contains(l)).toList();
        List<Long> removed = oldDiscordSet.stream().filter(l -> !newDiscordSet.contains(l)).toList();

        for (Long newDiscord : added)
            user.getClient().getEventManager().handleEvent(new UserDiscordAddEvent(user, newDiscord));
        for (Long oldDiscord : removed)
            user.getClient().getEventManager().handleEvent(new UserDiscordRemoveEvent(user, oldDiscord));
    }

    public static void ofUserMinecraft(@NotNull User user, @NotNull List<UUID> oldMinecraftSet, @NotNull List<UUID> newMinecraftSet) {
        List<UUID> added   = newMinecraftSet.stream().filter(uuid -> !oldMinecraftSet.contains(uuid)).toList();
        List<UUID> removed = oldMinecraftSet.stream().filter(uuid -> !oldMinecraftSet.contains(uuid)).toList();

        for (UUID newMinecraft : added)
            user.getClient().getEventManager().handleEvent(new UserMinecraftAddEvent(user, newMinecraft));
        for (UUID oldMinecraft : removed)
            user.getClient().getEventManager().handleEvent(new UserMinecraftRemoveEvent(user, oldMinecraft));
    }
}
