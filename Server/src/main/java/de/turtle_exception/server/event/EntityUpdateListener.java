package de.turtle_exception.server.event;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import de.turtle_exception.client.api.entities.Group;
import de.turtle_exception.client.api.entities.Turtle;
import de.turtle_exception.client.api.event.Event;
import de.turtle_exception.client.api.event.EventListener;
import de.turtle_exception.client.api.event.entities.EntityEvent;
import de.turtle_exception.client.api.event.entities.group.*;
import de.turtle_exception.client.api.event.entities.ticket.*;
import de.turtle_exception.client.api.event.entities.user.*;
import de.turtle_exception.client.internal.data.Data;
import de.turtle_exception.client.internal.data.DataUtil;
import de.turtle_exception.client.internal.data.annotations.Keys;
import de.turtle_exception.client.internal.net.Connection;
import de.turtle_exception.client.internal.net.packets.DataPacket;
import de.turtle_exception.client.internal.util.time.TurtleType;
import de.turtle_exception.client.internal.util.time.TurtleUtil;
import de.turtle_exception.server.net.NetServer;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.function.BiConsumer;

public class EntityUpdateListener extends EventListener {
    private final @NotNull NetServer server;

    public EntityUpdateListener(@NotNull NetServer server) {
        this.server = server;
    }

    private final void sendPacket(@NotNull Data data) {
        final long deadline = System.currentTimeMillis() + server.getClient().getDefaultTimeoutOutbound();
        final long responseCode = TurtleUtil.newId(TurtleType.RESPONSE_CODE);

        for (Connection client : server.getClients())
            client.send(new DataPacket(deadline, client, responseCode, data), true);
    }

    /* - - - */

    @Override
    public void onGenericEvent(@NotNull Event event) {
        if (!(event instanceof EntityEvent<?>)) return;

        if (event instanceof GroupCreateEvent e)
            onCreate(e.getEntity());
        if (event instanceof TicketCreateEvent e)
            onCreate(e.getEntity());
        if (event instanceof UserCreateEvent e)
            onCreate(e.getEntity());

        if (event instanceof GroupDeleteEvent e)
            onDelete(e.getEntity());
        if (event instanceof TicketDeleteEvent e)
            onDelete(e.getEntity());
        if (event instanceof UserDeleteEvent e)
            onDelete(e.getEntity());

        if (event instanceof GroupUpdateEvent<?> e)
            onUpdate(e.getKey(), e.getNewValue(), e.getEntity());
        if (event instanceof TicketUpdateEvent<?> e)
            onUpdate(e.getKey(), e.getNewValue(), e.getEntity());
        if (event instanceof UserUpdateEvent<?> e)
            onUpdate(e.getKey(), e.getNewValue(), e.getEntity());

        if (event instanceof GroupMemberJoinEvent e)
            onUpdateCollection(e.getEntity(), Keys.Group.MEMBERS, e.getEntity().getUsers(), (arr, user) -> arr.add(user.getId()));
        if (event instanceof GroupMemberLeaveEvent e)
            onUpdateCollection(e.getEntity(), Keys.Group.MEMBERS, e.getEntity().getUsers(), (arr, user) -> arr.add(user.getId()));
        if (event instanceof TicketTagAddEvent e)
            onUpdateCollection(e.getEntity(), Keys.Ticket.TAGS, e.getEntity().getTags(), JsonArray::add);
        if (event instanceof TicketTagRemoveEvent e)
            onUpdateCollection(e.getEntity(), Keys.Ticket.TAGS, e.getEntity().getTags(), JsonArray::add);
        if (event instanceof TicketUserAddEvent e)
            onUpdateCollection(e.getEntity(), Keys.Ticket.USERS, e.getEntity().getUsers(), (arr, user) -> arr.add(user.getId()));
        if (event instanceof TicketUserRemoveEvent e)
            onUpdateCollection(e.getEntity(), Keys.Ticket.USERS, e.getEntity().getUsers(), (arr, user) -> arr.add(user.getId()));
        if (event instanceof UserDiscordAddEvent e)
            onUpdateCollection(e.getEntity(), Keys.User.DISCORD, e.getEntity().getDiscordIds(), JsonArray::add);
        if (event instanceof UserDiscordRemoveEvent e)
            onUpdateCollection(e.getEntity(), Keys.User.DISCORD, e.getEntity().getDiscordIds(), JsonArray::add);
        if (event instanceof UserMinecraftAddEvent e)
            onUpdateCollection(e.getEntity(), Keys.User.MINECRAFT, e.getEntity().getMinecraftIds(), (arr, id) -> arr.add(id.toString()));
        if (event instanceof UserMinecraftRemoveEvent e)
            onUpdateCollection(e.getEntity(), Keys.User.MINECRAFT, e.getEntity().getMinecraftIds(), (arr, id) -> arr.add(id.toString()));
    }

    private void onCreate(@NotNull Turtle turtle) {
        JsonObject json = server.getClientImpl().getJsonBuilder().buildJson(turtle);
        this.sendPacket(Data.buildUpdate(turtle.getClass(), json));
    }

    private void onDelete(@NotNull Turtle turtle) {
        this.sendPacket(Data.buildRemove(turtle.getClass(), turtle.getId()));
    }

    private void onUpdate(@NotNull String key, @NotNull Object value, @NotNull Turtle turtle) {
        JsonObject json = new JsonObject();
        DataUtil.addValue(json, Keys.Turtle.ID, turtle.getId());
        DataUtil.addValue(json, key, value);
        this.sendPacket(Data.buildUpdate(turtle.getClass(), json));
    }

    private <T extends Turtle, U> void onUpdateCollection(@NotNull Turtle turtle, @NotNull String key, @NotNull Collection<U> c, @NotNull BiConsumer<JsonArray, U> consumer) {
        JsonObject json = new JsonObject();
        JsonArray  arr  = new JsonArray();
        DataUtil.addValue(json, Keys.Turtle.ID, turtle.getId());
        for (U obj : c)
            consumer.accept(arr, obj);
        json.add(key, arr);
        this.sendPacket(Data.buildUpdate(Group.class, json));
    }
}
