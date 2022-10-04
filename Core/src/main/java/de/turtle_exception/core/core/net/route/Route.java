package de.turtle_exception.core.core.net.route;

import de.turtle_exception.core.core.util.StringUtil;
import org.jetbrains.annotations.NotNull;

// TODO: docs
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

    public CompiledRoute compile(String content, @NotNull String... args) throws IllegalArgumentException {
        if (args.length != paramCount)
            throw new IllegalArgumentException("Incorrect amount of arguments (" + args.length + ") for " + paramCount + " parameters.");

        if (content == null && hasContent)
            throw new IllegalArgumentException("Expected non-null content.");
        if (content != null && !hasContent)
            throw new IllegalArgumentException("Unexpected non-null content. This route does not support content.");

        StringBuilder builder = new StringBuilder(this.route);

        for (int i = 0; i < paramCount; i++) {
            int beginIndex = builder.indexOf("{");
            int   endIndex = builder.indexOf("}");

            if (beginIndex <= endIndex)
                throw new IllegalArgumentException("Unexpected order of parameter braces");

            builder.replace(beginIndex, endIndex + 1, args[i]);
        }

        return new CompiledRoute(this, method, builder.toString(), content);
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
