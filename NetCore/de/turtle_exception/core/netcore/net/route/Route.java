package de.turtle_exception.core.netcore.net.route;

import de.turtle_exception.core.netcore.util.Checks;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

// TODO: docs
public class Route {
    private final String command;
    private final boolean terminating;
    private final ContentType contentType;
    private Integer callbackCode = null;
    private String  content      = null;

    Route(@Nullable String command, boolean terminating, @NotNull ContentType contentType) {
        this.command = command;
        this.terminating = terminating;
        this.contentType = contentType;
    }

    public @NotNull CompiledRoute build() throws IllegalArgumentException {
        Checks.nonNull(callbackCode, "CallbackCode");

        return new CompiledRoute(callbackCode, command, contentType, content, terminating);
    }

    /* - - - */

    public Route setCallbackCode(Integer callbackCode) {
        this.callbackCode = callbackCode;
        return this;
    }

    public Route setContent(String content) {
        this.content = content;
        return this;
    }

    /* - - - */

    public @Nullable Integer getCallbackCode() {
        return callbackCode;
    }

    public @Nullable String getCommand() {
        return command;
    }

    public @NotNull ContentType getContentType() {
        return contentType;
    }

    public @Nullable String getContent() {
        return content;
    }

    public boolean isTerminating() {
        return terminating;
    }
}
