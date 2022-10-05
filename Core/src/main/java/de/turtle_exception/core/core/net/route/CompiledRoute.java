package de.turtle_exception.core.core.net.route;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

// TODO: docs
public record CompiledRoute(
        @NotNull Route route,
        @NotNull Method method,
        @NotNull String[] args,
        @Nullable String content
) {
    public boolean isRoute(@NotNull Route route) {
        return this.route.getRoute().equals(route.getRoute());
    }

    public static CompiledRoute of(@NotNull String routeRaw, @NotNull String[] args, @NotNull Method method, @Nullable String content) throws IllegalArgumentException {
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
