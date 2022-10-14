package de.turtle_exception.client.internal.requests.action;

import com.google.common.collect.Sets;
import de.turtle_exception.client.api.TurtleClient;
import de.turtle_exception.client.api.entities.User;
import de.turtle_exception.client.api.requests.ActionHandler;
import de.turtle_exception.client.api.requests.action.UserAction;
import de.turtle_exception.client.internal.ActionImpl;
import de.turtle_exception.core.net.route.Routes;
import org.jetbrains.annotations.NotNull;

import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;

public class UserActionImpl extends ActionImpl<User> implements UserAction {
    private String name;

    private final Set<Long> discord   = Sets.newConcurrentHashSet();
    private final Set<UUID> minecraft = Sets.newConcurrentHashSet();

    public UserActionImpl(@NotNull TurtleClient client, ActionHandler<User> handler) {
        super(client, Routes.User.CREATE, handler);
    }

    @Override
    public UserActionImpl onSuccess(Consumer<? super User> consumer) {
        super.onSuccess(consumer);
        return this;
    }

    /* - - - */

    @Override
    public @NotNull UserAction setName(@NotNull String name) {
        this.name = name;
        return this;
    }

    @Override
    public @NotNull UserAction addDiscord(long id) {
        this.discord.add(id);
        return this;
    }

    @Override
    public @NotNull UserAction addMinecraft(@NotNull UUID id) {
        this.minecraft.add(id);
        return this;
    }
}
