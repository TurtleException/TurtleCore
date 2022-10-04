package de.turtle_exception.core.core.util;

import de.turtle_exception.core.core.net.message.InboundMessage;
import de.turtle_exception.core.core.net.message.OutboundMessage;
import de.turtle_exception.core.core.net.route.Errors;
import de.turtle_exception.core.core.net.route.Route;
import de.turtle_exception.core.core.net.route.Routes;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.Callable;

public class ResponseHelper {
    public static void tryResponse(@NotNull InboundMessage in, @NotNull Route route, @NotNull Callable<String> contentProvider, @Nullable String error) {
        try {
            // try to send response
            String content = contentProvider.call();
            in.respond(new OutboundMessage(
                    in.getCore(),
                    route.setCallbackCode(in.getRoute().callbackCode()).setContent(content).build(),
                    System.currentTimeMillis() + in.getCore().getDefaultTimeoutOutbound(),
                    ignored -> { }));
        } catch (Exception e) {
            // fail silently if error is null
            if (error == null) return;

            // send error
            if (error.equals(Errors.EXCEPTION))
                respondError(in, error + " " + e);
            else
                respondError(in, error);
        }
    }

    public static void tryOK(@NotNull InboundMessage in, @NotNull Callable<Void> callable, @Nullable String error) {
        tryResponse(in, Routes.OK, () -> {
            callable.call();
            return "";
        }, error);
    }

    public static void respondError(@NotNull InboundMessage in, @NotNull String error) {
        Route errorRoute = Routes.ERROR.setCallbackCode(in.getRoute().callbackCode()).setContent(error);
        in.respond(new OutboundMessage(
                in.getCore(),
                errorRoute.build(),
                System.currentTimeMillis() + in.getCore().getDefaultTimeoutOutbound(),
                ignored -> { }));
    }

    public static @Nullable Long parseLong(@NotNull InboundMessage in) {
        String str = in.getRoute().content();
        str = str.substring(0, str.indexOf(" "));
        try {
            return Long.parseLong(str);
        } catch (NumberFormatException e) {
            respondError(in, Errors.ILLEGAL_ID + " " + str);
            return null;
        }
    }
}
