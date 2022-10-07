package de.turtle_exception.server.net.handlers;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import de.turtle_exception.core.data.JsonChecks;
import de.turtle_exception.core.net.message.InboundMessage;
import de.turtle_exception.core.net.route.CompiledRoute;
import de.turtle_exception.core.net.route.Routes;
import de.turtle_exception.server.net.VirtualClient;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.UUID;

public class UserHandler extends ResourceHandler {
    public UserHandler(@NotNull VirtualClient client) {
        super(client);
    }

    @Override
    public void handle(@NotNull InboundMessage msg) throws Exception {
        CompiledRoute route = msg.getRoute();

        if (route.isRoute(Routes.User.GET)) {
            long id = Long.parseLong(route.args()[0]);
            msg.respond(Routes.RESPONSE.compile(getDataService().getUser(id)));
            return;
        } else if (route.isRoute(Routes.User.GET_ALL)) {
            msg.respond(Routes.RESPONSE.compile(getDataService().getUsers()));
            return;
        } else if (route.isRoute(Routes.User.DEL)) {
            long id = Long.parseLong(route.args()[0]);
            getDataService().deleteUser(id);
        } else if (route.isRoute(Routes.User.CREATE)) {
            JsonObject json = new Gson().fromJson(route.content(), JsonObject.class);
            JsonChecks.validateUser(json);
            getDataService().setUser(json);
        } else if (route.isRoute(Routes.User.MODIFY)) {
            long id = Long.parseLong(route.args()[0]);
            JsonObject modifications = new Gson().fromJson(route.content(), JsonObject.class);
            getDataService().modifyUser(id, initialJson -> {
                for (Map.Entry<String, JsonElement> entry : modifications.entrySet())
                    initialJson.add(entry.getKey(), entry.getValue());
                JsonChecks.validateUser(initialJson);
                return initialJson;
            });
        } else if (route.isRoute(Routes.User.ADD_DISCORD)) {
            long  userId = Long.parseLong(route.args()[0]);
            long discord = Long.parseLong(route.args()[1]);

            getDataService().addUserDiscord(userId, discord);
        } else if (route.isRoute(Routes.User.DEL_DISCORD)) {
            long  userId = Long.parseLong(route.args()[0]);
            long discord = Long.parseLong(route.args()[1]);

            getDataService().delUserDiscord(userId, discord);
        } else if (route.isRoute(Routes.User.ADD_MINECRAFT)) {
            long  userId = Long.parseLong(route.args()[0]);
            UUID minecraft = UUID.fromString(route.args()[1]);

            getDataService().addUserMinecraft(userId, minecraft);
        } else if (route.isRoute(Routes.User.DEL_MINECRAFT)) {
            long  userId = Long.parseLong(route.args()[0]);
            UUID minecraft = UUID.fromString(route.args()[1]);

            getDataService().delUserMinecraft(userId, minecraft);
        } else {
            throw new UnsupportedOperationException();
        }

        // return ok for everything that did not already return
        msg.respond(Routes.OK.compile(null));
    }
}
