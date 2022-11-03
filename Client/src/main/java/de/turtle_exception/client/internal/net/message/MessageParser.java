package de.turtle_exception.client.internal.net.message;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import de.turtle_exception.client.api.TurtleClient;
import de.turtle_exception.client.internal.net.NetworkAdapter;
import de.turtle_exception.client.internal.net.route.CompiledRoute;
import de.turtle_exception.client.internal.net.route.Method;
import de.turtle_exception.client.internal.util.Checks;
import org.jetbrains.annotations.NotNull;

public class MessageParser {
    public static @NotNull String parse(@NotNull OutboundMessage msg) {
        JsonObject json = new JsonObject();

        JsonObject route = new JsonObject();
        route.addProperty("route", msg.getRoute().route().getRoute());

        JsonArray routeArgs = new JsonArray();
        for (String arg : msg.getRoute().args())
            routeArgs.add(arg);
        route.add("arguments", routeArgs);

        json.addProperty("code", msg.getConversation());
        json.addProperty("method", msg.getRoute().method().getName());
        json.add("route", route);
        json.add("content", msg.getRoute().content());

        return json.toString();
    }

    public static @NotNull InboundMessage parse(@NotNull TurtleClient core, @NotNull NetworkAdapter adapter, @NotNull String msg) throws IllegalArgumentException {
        try {
            JsonObject json = new Gson().fromJson(msg, JsonObject.class);

            JsonObject routeJson = json.getAsJsonObject("route");
            String     routeRaw  = routeJson.get("route").getAsString();
            JsonArray  routeArgs = routeJson.getAsJsonArray("arguments");

            long   conversation = json.get("code").getAsLong();
            String    methodStr = json.get("method").getAsString();
            JsonElement content = json.get("content");

            String[] args = new String[0];
            if (routeArgs != null) {
                args = new String[routeArgs.size()];
                for (int i = 0; i < routeArgs.size(); i++)
                    args[i] = routeArgs.get(i).getAsString();
            }

            Method method = null;
            for (Method value : Method.values()) {
                if (value.getName().equals(methodStr)) {
                    method = value;
                    break;
                }
            }

            Checks.nonNull(routeRaw, "Raw Route");
            Checks.nonNull(method  , "Method"   );

            CompiledRoute route = CompiledRoute.of(routeRaw, args, method, content);
            long deadline = System.currentTimeMillis() + core.getDefaultTimeoutInbound();

            return new InboundMessage(core, adapter, conversation, route, deadline);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }
}
