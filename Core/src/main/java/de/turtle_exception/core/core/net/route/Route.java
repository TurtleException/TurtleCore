package de.turtle_exception.core.core.net.route;

import com.google.gson.JsonElement;
import de.turtle_exception.core.core.util.StringUtil;
import org.jetbrains.annotations.NotNull;

/**
 * A Route is an instruction on where and how to execute a network request.
 * Routes can be re-used across connections and instances.
 * @see Routes
 */
public class Route {
    private final @NotNull Method method;
    private final @NotNull String route;
    private final boolean hasContent;
    private final int paramCount;

    Route(@NotNull Method method, @NotNull String route, boolean hasContent) throws IllegalArgumentException {
        this.method  = method;
        this.route = route;
        this.hasContent = hasContent;

        this.paramCount = StringUtil.count(route, "{");
        if (paramCount != StringUtil.count(route, "}"))
            throw new IllegalArgumentException("Missing braces on a argument for route: " + method.getName() + " " + route);
    }

    /* - - - */

    public CompiledRoute compile(JsonElement content, @NotNull Object... args) throws IllegalArgumentException {
        if (args.length != paramCount)
            throw new IllegalArgumentException("Incorrect amount of arguments (" + args.length + ") for " + paramCount + " parameters.");

        if (content == null && hasContent)
            throw new IllegalArgumentException("Expected non-null content.");
        if (content != null && !hasContent)
            throw new IllegalArgumentException("Unexpected non-null content. This route does not support content.");

        String[] stringArgs = new String[args.length];
        for (int i = 0; i < args.length; i++) {
            stringArgs[i] = String.valueOf(args[i]);
        }

        return new CompiledRoute(this, method, stringArgs, content);
    }

    CompiledRoute compileReplacing(JsonElement content, String[] args) {
        return new CompiledRoute(this, method, args, content);
    }

    /* - - - */

    public @NotNull Method getMethod() {
        return method;
    }

    public @NotNull String getRoute() {
        return route;
    }

    public int getParamCount() {
        return paramCount;
    }
}
