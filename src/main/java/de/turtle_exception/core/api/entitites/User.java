package de.turtle_exception.core.api.entitites;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;

public interface User extends Turtle {
    @NotNull String getString();

    @NotNull List<Long> getDiscordIds();

    @NotNull List<UUID> getMinecraftIds();
}
