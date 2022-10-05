package de.turtle_exception.core.net.route;

import com.google.gson.JsonElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A final version of {@link Route} with arguments and content.
 * @param route The underlying {@link Route}.
 * @param method The request method.
 * @param args The arguments for the route parameters. This array should always have the same length as {@link Route#getParamCount()}.
 * @param content The content this route should be sent with.
 */
public record CompiledRoute(
        @NotNull Route route,
        @NotNull Method method,
        @NotNull String[] args,
        @Nullable JsonElement content
) {
    public boolean isRoute(@NotNull Route route) {
        return this.route.getRoute().equals(route.getRoute());
    }

    public static CompiledRoute of(@NotNull String routeRaw, @NotNull String[] args, @NotNull Method method, @Nullable JsonElement content) throws IllegalArgumentException {
        Route route = null;
        for (Route checkRoute : Routes.getRoutes()) {
            if (!checkRoute.getMethod().equals(method))  continue;
            if (!checkRoute.getRoute().equals(routeRaw)) continue;

            route = checkRoute;
            break;
        }
        if (route == null)
            throw new IllegalArgumentException("Unknown route: " + routeRaw);

        return route.compileReplacing(content, args);
    }
}
