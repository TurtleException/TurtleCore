package de.turtle_exception.server.event;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import de.turtle_exception.client.api.entities.Ticket;
import de.turtle_exception.client.api.entities.User;
import de.turtle_exception.client.api.event.ticket.*;
import de.turtle_exception.client.internal.data.Data;
import de.turtle_exception.client.internal.data.DataUtil;
import de.turtle_exception.server.net.NetServer;
import org.jetbrains.annotations.NotNull;

public class EntityTicketListener extends AbstractListener {
    public EntityTicketListener(@NotNull NetServer server) {
        super(server);
    }

    /* - - - */

    @Override
    public void onTicketCreate(@NotNull TicketCreateEvent event) {
        JsonObject json = server.getClientImpl().getJsonBuilder().buildJson(event.getTicket());
        this.sendPacket(Data.buildUpdate(Ticket.class, json));
    }

    @Override
    public void onTicketDelete(@NotNull TicketDeleteEvent event) {
        this.sendPacket(Data.buildRemove(Ticket.class, event.getTicket().getId()));
    }

    @Override
    public void onTicketUpdate(@NotNull TicketUpdateEvent<?> event) {
        JsonObject json = new JsonObject();
        DataUtil.addValue(json, "id", event.getTicket().getId());
        DataUtil.addValue(json, event.getKey(), event.getNewValue());
        this.sendPacket(Data.buildUpdate(Ticket.class, json));
    }

    @Override
    public void onTicketTagAdd(@NotNull TicketTagAddEvent event) {
        JsonObject json = new JsonObject();
        JsonArray  arr  = new JsonArray();
        DataUtil.addValue(json, "id", event.getTicket().getId());
        for (String tag : event.getTicket().getTags())
            arr.add(tag);
        json.add("tags", arr);
        this.sendPacket(Data.buildUpdate(Ticket.class, json));
    }

    @Override
    public void onTicketTagRemove(@NotNull TicketTagRemoveEvent event) {
        JsonObject json = new JsonObject();
        JsonArray  arr  = new JsonArray();
        DataUtil.addValue(json, "id", event.getTicket().getId());
        for (String tag : event.getTicket().getTags())
            arr.add(tag);
        json.add("tags", arr);
        this.sendPacket(Data.buildUpdate(Ticket.class, json));
    }

    @Override
    public void onTicketUserAdd(@NotNull TicketUserAddEvent event) {
        JsonObject json = new JsonObject();
        JsonArray  arr  = new JsonArray();
        DataUtil.addValue(json, "id", event.getTicket().getId());
        for (User user : event.getTicket().getUsers())
            arr.add(user.getId());
        json.add("users", arr);
        this.sendPacket(Data.buildUpdate(Ticket.class, json));
    }

    @Override
    public void onTicketUserRemove(@NotNull TicketUserRemoveEvent event) {
        JsonObject json = new JsonObject();
        JsonArray  arr  = new JsonArray();
        DataUtil.addValue(json, "id", event.getTicket().getId());
        for (User user : event.getTicket().getUsers())
            arr.add(user.getId());
        json.add("users", arr);
        this.sendPacket(Data.buildUpdate(Ticket.class, json));
    }
}
