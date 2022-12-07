package de.turtle_exception.client.internal.event;

import de.turtle_exception.client.api.entities.*;
import de.turtle_exception.client.api.entities.attributes.EphemeralType;
import de.turtle_exception.client.api.entities.form.*;
import de.turtle_exception.client.api.entities.messages.DiscordChannel;
import de.turtle_exception.client.api.entities.messages.MinecraftChannel;
import de.turtle_exception.client.api.entities.messages.SyncChannel;
import de.turtle_exception.client.api.entities.messages.SyncMessage;
import de.turtle_exception.client.api.event.entities.EphemeralEntityEvent;
import de.turtle_exception.client.api.event.entities.form.completed_form.CompletedFormCreateEvent;
import de.turtle_exception.client.api.event.entities.form.completed_form.CompletedFormDeleteEvent;
import de.turtle_exception.client.api.event.entities.form.query_element.QueryElementCreateEvent;
import de.turtle_exception.client.api.event.entities.form.query_element.QueryElementDeleteEvent;
import de.turtle_exception.client.api.event.entities.form.query_response.QueryResponseCreateEvent;
import de.turtle_exception.client.api.event.entities.form.query_response.QueryResponseDeleteEvent;
import de.turtle_exception.client.api.event.entities.form.template_form.TemplateFormCreateEvent;
import de.turtle_exception.client.api.event.entities.form.template_form.TemplateFormDeleteEvent;
import de.turtle_exception.client.api.event.entities.form.text_element.TextElementCreateEvent;
import de.turtle_exception.client.api.event.entities.form.text_element.TextElementDeleteEvent;
import de.turtle_exception.client.api.event.entities.group.GroupCreateEvent;
import de.turtle_exception.client.api.event.entities.group.GroupDeleteEvent;
import de.turtle_exception.client.api.event.entities.group.GroupMemberJoinEvent;
import de.turtle_exception.client.api.event.entities.group.GroupMemberLeaveEvent;
import de.turtle_exception.client.api.event.entities.json_resource.JsonResourceCreateEvent;
import de.turtle_exception.client.api.event.entities.json_resource.JsonResourceDeleteEvent;
import de.turtle_exception.client.api.event.entities.messages.discord_channel.DiscordChannelCreateEvent;
import de.turtle_exception.client.api.event.entities.messages.discord_channel.DiscordChannelDeleteEvent;
import de.turtle_exception.client.api.event.entities.messages.minecraft_channel.MinecraftChannelCreateEvent;
import de.turtle_exception.client.api.event.entities.messages.minecraft_channel.MinecraftChannelDeleteEvent;
import de.turtle_exception.client.api.event.entities.messages.sync_channel.*;
import de.turtle_exception.client.api.event.entities.messages.sync_message.SyncMessageCreateEvent;
import de.turtle_exception.client.api.event.entities.messages.sync_message.SyncMessageDeleteEvent;
import de.turtle_exception.client.api.event.entities.project.ProjectCreateEvent;
import de.turtle_exception.client.api.event.entities.project.ProjectDeleteEvent;
import de.turtle_exception.client.api.event.entities.project.ProjectMemberJoinEvent;
import de.turtle_exception.client.api.event.entities.project.ProjectMemberLeaveEvent;
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
        if (turtle instanceof EphemeralType e && e.isEphemeral()) {
            turtle.getClient().getEventManager().handleEvent(new EphemeralEntityEvent<>(e));
            return;
        }

        if (turtle instanceof Group group)
            turtle.getClient().getEventManager().handleEvent(new GroupCreateEvent(group));
        if (turtle instanceof JsonResource jsonResource)
            turtle.getClient().getEventManager().handleEvent(new JsonResourceCreateEvent(jsonResource));
        if (turtle instanceof Project project)
            turtle.getClient().getEventManager().handleEvent(new ProjectCreateEvent(project));
        if (turtle instanceof Ticket ticket)
            turtle.getClient().getEventManager().handleEvent(new TicketCreateEvent(ticket));
        if (turtle instanceof User user)
            turtle.getClient().getEventManager().handleEvent(new UserCreateEvent(user));

        // FORM
        if (turtle instanceof CompletedForm form)
            turtle.getClient().getEventManager().handleEvent(new CompletedFormCreateEvent(form));
        if (turtle instanceof QueryElement element)
            turtle.getClient().getEventManager().handleEvent(new QueryElementCreateEvent(element));
        if (turtle instanceof QueryResponse query)
            turtle.getClient().getEventManager().handleEvent(new QueryResponseCreateEvent(query));
        if (turtle instanceof TemplateForm form)
            turtle.getClient().getEventManager().handleEvent(new TemplateFormCreateEvent(form));
        if (turtle instanceof TextElement element)
            turtle.getClient().getEventManager().handleEvent(new TextElementCreateEvent(element));

        // MESSAGES
        if (turtle instanceof DiscordChannel discordChannel)
            turtle.getClient().getEventManager().handleEvent(new DiscordChannelCreateEvent(discordChannel));
        if (turtle instanceof MinecraftChannel minecraftChannel)
            turtle.getClient().getEventManager().handleEvent(new MinecraftChannelCreateEvent(minecraftChannel));
        if (turtle instanceof SyncChannel syncChannel)
            turtle.getClient().getEventManager().handleEvent(new SyncChannelCreateEvent(syncChannel));
        if (turtle instanceof SyncMessage syncMessage)
            turtle.getClient().getEventManager().handleEvent(new SyncMessageCreateEvent(syncMessage));
    }

    public static void ofDeleteTurtle(@NotNull Turtle turtle) {
        if (turtle instanceof Group group)
            turtle.getClient().getEventManager().handleEvent(new GroupDeleteEvent(group));
        if (turtle instanceof JsonResource jsonResource)
            turtle.getClient().getEventManager().handleEvent(new JsonResourceDeleteEvent(jsonResource));
        if (turtle instanceof Project project)
            turtle.getClient().getEventManager().handleEvent(new ProjectDeleteEvent(project));
        if (turtle instanceof Ticket ticket)
            turtle.getClient().getEventManager().handleEvent(new TicketDeleteEvent(ticket));
        if (turtle instanceof User user)
            turtle.getClient().getEventManager().handleEvent(new UserDeleteEvent(user));

        // FORM
        if (turtle instanceof CompletedForm form)
            turtle.getClient().getEventManager().handleEvent(new CompletedFormDeleteEvent(form));
        if (turtle instanceof QueryElement element)
            turtle.getClient().getEventManager().handleEvent(new QueryElementDeleteEvent(element));
        if (turtle instanceof QueryResponse query)
            turtle.getClient().getEventManager().handleEvent(new QueryResponseDeleteEvent(query));
        if (turtle instanceof TemplateForm form)
            turtle.getClient().getEventManager().handleEvent(new TemplateFormDeleteEvent(form));
        if (turtle instanceof TextElement element)
            turtle.getClient().getEventManager().handleEvent(new TextElementDeleteEvent(element));

        // MESSAGES
        if (turtle instanceof DiscordChannel discordChannel)
            turtle.getClient().getEventManager().handleEvent(new DiscordChannelDeleteEvent(discordChannel));
        if (turtle instanceof MinecraftChannel minecraftChannel)
            turtle.getClient().getEventManager().handleEvent(new MinecraftChannelDeleteEvent(minecraftChannel));
        if (turtle instanceof SyncChannel syncChannel)
            turtle.getClient().getEventManager().handleEvent(new SyncChannelDeleteEvent(syncChannel));
        if (turtle instanceof SyncMessage syncMessage)
            turtle.getClient().getEventManager().handleEvent(new SyncMessageDeleteEvent(syncMessage));
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

    /* - PROJECT - */

    public static void ofProjectMembers(@NotNull Project project, @NotNull TurtleSet<User> oldUsers, @NotNull TurtleSet<User> newUsers) {
        List<User> added   = newUsers.stream().filter(user -> !oldUsers.containsId(user.getId())).toList();
        List<User> removed = oldUsers.stream().filter(user -> !newUsers.containsId(user.getId())).toList();

        for (User newUser : added)
            project.getClient().getEventManager().handleEvent(new ProjectMemberJoinEvent(project, newUser));
        for (User oldUser : removed)
            project.getClient().getEventManager().handleEvent(new ProjectMemberLeaveEvent(project, oldUser));
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

    /* - SYNC CHANNEL - */

    public static void ofSyncChannelDiscord(@NotNull SyncChannel syncChannel, @NotNull TurtleSet<DiscordChannel> oldDiscordSet, @NotNull TurtleSet<DiscordChannel> newDiscordSet) {
        List<DiscordChannel> added   = newDiscordSet.stream().filter(channel -> !oldDiscordSet.containsId(channel.getId())).toList();
        List<DiscordChannel> removed = oldDiscordSet.stream().filter(channel -> !newDiscordSet.containsId(channel.getId())).toList();

        for (DiscordChannel newDiscord : added)
            syncChannel.getClient().getEventManager().handleEvent(new SyncChannelDiscordChannelAddEvent(syncChannel, newDiscord));
        for (DiscordChannel newDiscord : removed)
            syncChannel.getClient().getEventManager().handleEvent(new SyncChannelDiscordChannelRemoveEvent(syncChannel, newDiscord));
    }

    public static void ofSyncChannelMinecraft(@NotNull SyncChannel syncChannel, @NotNull TurtleSet<MinecraftChannel> oldMinecraftSet, @NotNull TurtleSet<MinecraftChannel> newMinecraftSet) {
        List<MinecraftChannel> added   = newMinecraftSet.stream().filter(channel -> !oldMinecraftSet.containsId(channel.getId())).toList();
        List<MinecraftChannel> removed = oldMinecraftSet.stream().filter(channel -> !newMinecraftSet.containsId(channel.getId())).toList();

        for (MinecraftChannel newMinecraft : added)
            syncChannel.getClient().getEventManager().handleEvent(new SyncChannelMinecraftChannelAddEvent(syncChannel, newMinecraft));
        for (MinecraftChannel newMinecraft : removed)
            syncChannel.getClient().getEventManager().handleEvent(new SyncChannelMinecraftChannelRemoveEvent(syncChannel, newMinecraft));
    }
}
