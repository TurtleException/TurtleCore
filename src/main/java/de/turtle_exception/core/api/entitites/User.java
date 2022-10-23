package de.turtle_exception.core.api.entitites;

import de.turtle_exception.core.internal.data.annotations.Key;
import de.turtle_exception.core.internal.data.annotations.Reference;
import de.turtle_exception.core.internal.data.annotations.Relation;
import de.turtle_exception.core.internal.data.annotations.Resource;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;

@Resource(name = "users")
public interface User extends Turtle {
    @Key(name = "name")
    @NotNull String getString();

    @Reference(name = "user_discord", relation = Relation.ONE_TO_MANY)
    @NotNull List<Long> getDiscordIds();

    @Reference(name = "user_minecraft", relation = Relation.ONE_TO_MANY)
    @NotNull List<UUID> getMinecraftIds();
}
