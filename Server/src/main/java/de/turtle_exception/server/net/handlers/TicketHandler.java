package de.turtle_exception.server.net.handlers;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import de.turtle_exception.client.internal.data.JsonChecks;
import de.turtle_exception.client.internal.net.message.InboundMessage;
import de.turtle_exception.client.internal.net.route.CompiledRoute;
import de.turtle_exception.client.internal.net.route.Routes;
import de.turtle_exception.server.net.VirtualClient;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class TicketHandler extends ResourceHandler {
    public TicketHandler(@NotNull VirtualClient client) {
        super(client);
    }

    @Override
    public void handle(@NotNull InboundMessage msg) throws Exception {
        CompiledRoute route = msg.getRoute();

        if (route.isRoute(Routes.Ticket.GET)) {
            long id = Long.parseLong(route.args()[0]);
            msg.respond(Routes.RESPONSE.compile(getDataService().getTicket(id)));
            return;
        } else if (route.isRoute(Routes.Ticket.GET_ALL)) {
            msg.respond(Routes.RESPONSE.compile(getDataService().getTickets()));
            return;
        } else if (route.isRoute(Routes.Ticket.DEL)) {
            long id = Long.parseLong(route.args()[0]);
            getDataService().deleteTicket(id);
        } else if (route.isRoute(Routes.Ticket.CREATE)) {
            JsonObject json = new Gson().fromJson(route.content(), JsonObject.class);
            JsonChecks.validateTicket(json);
            getDataService().setTicket(json);
        } else if (route.isRoute(Routes.Ticket.MODIFY)) {
            long id = Long.parseLong(route.args()[0]);
            JsonObject modifications = new Gson().fromJson(route.content(), JsonObject.class);
            getDataService().modifyTicket(id, initialJson -> {
                for (Map.Entry<String, JsonElement> entry : modifications.entrySet())
                    initialJson.add(entry.getKey(), entry.getValue());
                JsonChecks.validateTicket(initialJson);
                return initialJson;
            });
        } else if (route.isRoute(Routes.Ticket.ADD_TAG)) {
            long ticket = Long.parseLong(route.args()[0]);
            String  tag = route.args()[2];

            getDataService().addTicketTag(ticket, tag);
        } else if (route.isRoute(Routes.Ticket.DEL_TAG)) {
            long ticket = Long.parseLong(route.args()[0]);
            String  tag = route.args()[2];

            getDataService().delTicketTag(ticket, tag);
        } else if (route.isRoute(Routes.Ticket.ADD_USER)) {
            long ticket = Long.parseLong(route.args()[0]);
            long   user = Long.parseLong(route.args()[1]);

            getDataService().addTicketUser(ticket, user);
        } else if (route.isRoute(Routes.Ticket.DEL_USER)) {
            long ticket = Long.parseLong(route.args()[0]);
            long   user = Long.parseLong(route.args()[1]);

            getDataService().delTicketUser(ticket, user);
        } else {
            throw new UnsupportedOperationException();
        }

        // return ok for everything that did not already return
        msg.respond(Routes.OK.compile(null));
    }
}
