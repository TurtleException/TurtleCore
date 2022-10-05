package de.turtle_exception.core.server.net.handlers;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import de.turtle_exception.core.core.net.message.InboundMessage;
import de.turtle_exception.core.core.net.route.CompiledRoute;
import de.turtle_exception.core.core.net.route.Routes;
import de.turtle_exception.core.server.net.VirtualClient;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class GroupHandler extends ResourceHandler {
    public GroupHandler(@NotNull VirtualClient client) {
        super(client);
    }

    @Override
    public void handle(@NotNull InboundMessage msg) throws Exception {
        CompiledRoute route = msg.getRoute();

        if (route.isRoute(Routes.Group.GET)) {
            long id = Long.parseLong(route.args()[0]);
            msg.respond(Routes.RESPONSE.compile(getDataService().getGroup(id).toString()));
            return;
        } else if (route.isRoute(Routes.Group.GET_ALL)) {
            msg.respond(Routes.RESPONSE.compile(getDataService().getGroups().toString()));
            return;
        } else if (route.isRoute(Routes.Group.DEL)) {
            long id = Long.parseLong(route.args()[0]);
            getDataService().deleteGroup(id);
        } else if (route.isRoute(Routes.Group.CREATE)) {
            JsonObject json = new Gson().fromJson(route.content(), JsonObject.class);
            getDataService().setGroup(json);
        } else if (route.isRoute(Routes.Group.MODIFY)) {
            long id = Long.parseLong(route.args()[0]);
            JsonObject modifications = new Gson().fromJson(route.content(), JsonObject.class);
            getDataService().modifyGroup(id, initialJson -> {
                for (Map.Entry<String, JsonElement> entry : modifications.entrySet())
                    initialJson.add(entry.getKey(), entry.getValue());
                return initialJson;
            });
        } else if (route.isRoute(Routes.Group.ADD_USER)) {
            long groupId = Long.parseLong(route.args()[0]);
            long  userId = Long.parseLong(route.args()[1]);

            getDataService().addUserGroup(userId, groupId);
        } else if (route.isRoute(Routes.Group.DEL_USER)) {
            long groupId = Long.parseLong(route.args()[0]);
            long  userId = Long.parseLong(route.args()[1]);

            getDataService().delUserGroup(userId, groupId);
        } else {
            throw new UnsupportedOperationException();
        }

        // return ok for everything that did not already return
        msg.respond(Routes.OK.compile(null));
    }
}
