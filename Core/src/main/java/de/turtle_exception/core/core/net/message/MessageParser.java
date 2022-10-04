package de.turtle_exception.core.core.net.message;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import de.turtle_exception.core.core.TurtleCore;
import de.turtle_exception.core.core.net.NetworkAdapter;
import de.turtle_exception.core.core.net.route.CompiledRoute;
import de.turtle_exception.core.core.net.route.Method;
import de.turtle_exception.core.core.util.Checks;
import org.jetbrains.annotations.NotNull;

public class MessageParser {
    public static @NotNull String parse(@NotNull OutboundMessage msg) {
        JsonObject json = new JsonObject();

        JsonObject route = new JsonObject();
        route.addProperty("raw", msg.getRoute().route().getRoute());
        route.addProperty("compiled", msg.getRoute().routeStr());

        json.addProperty("code", msg.getConversation());
        json.addProperty("method", msg.getRoute().method().getName());
        json.add("route", route);
        json.addProperty("content", msg.getRoute().content());

        return json.toString();
    }

    public static @NotNull InboundMessage parse(@NotNull TurtleCore core, @NotNull NetworkAdapter adapter, @NotNull String msg) throws IllegalArgumentException {
        try {
            JsonObject json = new Gson().fromJson(msg, JsonObject.class);

            JsonObject routeJson     = json.getAsJsonObject("route");
            String     routeRaw      = routeJson.get("raw").getAsString();
            String     routeCompiled = routeJson.get("compiled").getAsString();

            long conversation = json.get("code").getAsLong();
            String  methodStr = json.get("method").getAsString();
            String contentStr = json.get("content").getAsString();

            Method method = null;
            for (Method value : Method.values()) {
                if (value.getName().equals(methodStr)) {
                    method = value;
                    break;
                }
            }

            Checks.nonNull(routeRaw     , "Raw Route"     );
            Checks.nonNull(routeCompiled, "Compiled Route");
            Checks.nonNull(method       , "Method"        );

            CompiledRoute route = CompiledRoute.of(routeRaw, routeCompiled, method, contentStr);
            long deadline = System.currentTimeMillis() + core.getDefaultTimeoutInbound();

            return new InboundMessage(core, adapter, conversation, route, deadline);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }
}
