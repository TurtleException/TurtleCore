package de.turtle_exception.core.client.internal;

import com.google.gson.*;
import de.turtle_exception.core.client.api.TurtleClient;
import de.turtle_exception.core.client.api.entities.Group;
import de.turtle_exception.core.client.api.entities.User;
import de.turtle_exception.core.client.api.requests.Action;
import de.turtle_exception.core.client.internal.entities.EntityBuilder;
import de.turtle_exception.core.client.internal.entities.UserImpl;
import de.turtle_exception.core.client.internal.net.NetClient;
import de.turtle_exception.core.client.internal.util.TurtleSet;
import de.turtle_exception.core.core.TurtleCore;
import de.turtle_exception.core.core.net.route.Routes;
import de.turtle_exception.core.core.util.version.Version;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TurtleClientImpl extends TurtleCore implements TurtleClient {
    private final Version version;

    /** The root logger of this core */
    private final Logger logger;

    /** Name of this instance. Naming is not required, but it may be helpful when using multiple instances. */
    private final @Nullable String name;

    /** The internal network part of the client */
    private final NetClient netClient;

    private final ScheduledThreadPoolExecutor callbackExecutor;

    private @NotNull Consumer<Object>            defaultOnSuccess = o -> { };
    private @NotNull Consumer<? super Throwable> defaultOnFailure = t -> {
        // TODO
    };

    private final TurtleSet<User> userCache = new TurtleSet<>();
    private final TurtleSet<Group> groupCache = new TurtleSet<>();

    public TurtleClientImpl(@Nullable String name, @NotNull String host, @Range(from = 0, to = 65535) int port, @NotNull String login, @NotNull String pass) throws IOException, LoginException {
        this.version = Version.retrieveFromResources(TurtleClient.class);
        if (this.version == null)
            throw new IOException("Illegal version");

        this.name = name;
        this.logger = Logger.getLogger(name != null ? "CLIENT#" + name : "CLIENT");

        this.callbackExecutor = new ScheduledThreadPoolExecutor(4, (r, executor) -> logger.log(Level.WARNING, "A callback task was rejected by the executor: ", r));
        this.netClient = new NetClient(this, host, port, login, pass);

        this.routeManager.setLog(logger::log);

        this.routeManager.setRouteFinalizer(Routes.QUIT, inboundMessage -> {
            logger.log(Level.WARNING, "Received QUIT command!");
            try {
                netClient.quit();
            } catch (IOException e) {
                logger.log(Level.WARNING, "Could not close socket properly.", e);
            }
        });
        this.routeManager.setRouteFinalizer(Routes.ERROR, inboundMessage -> {
            // this is for debug purposes (relevant errors should be reported in processing)
            logger.log(Level.FINE, "Received error: " + inboundMessage.getRoute().content());
        });

        /* --- NOT HANDLED BY FINALIZERS */
        this.routeManager.setEmptyFinalizer(
                Routes.OK,
                Routes.Content.User.GET_ALL_R,
                Routes.Content.User.GET_R,
                Routes.Content.User.DISCORD_GET_R,
                Routes.Content.User.MINECRAFT_GET_R,
                Routes.Content.Group.GET_ALL_R,
                Routes.Content.Group.GET_R
        );

        /* --- CONTENT / USER */
        this.routeManager.setRouteFinalizer(Routes.Content.User.DELETED, inboundMessage -> {
            userCache.removeStringId(inboundMessage.getRoute().content());
        });
        this.routeManager.setRouteFinalizer(Routes.Content.User.UPDATE, inboundMessage -> {
            userCache.add(EntityBuilder.buildUser(inboundMessage.getRoute().content()));
        });
        this.routeManager.setRouteFinalizer(Routes.Content.User.UPDATES, inboundMessage -> {
            userCache.addAll(EntityBuilder.buildUsers(inboundMessage.getRoute().content()));
        });
        this.routeManager.setRouteFinalizer(Routes.Content.User.DISCORD_UPDATE, inboundMessage -> {
            try {
                JsonObject json = new Gson().fromJson(inboundMessage.getRoute().content(), JsonObject.class);

                long id = json.get("user").getAsLong();
                UserImpl  user    = (UserImpl) userCache.get(id);
                JsonArray discord = json.getAsJsonArray("discord");

                if (user == null)
                    throw new NullPointerException();

                ArrayList<Long> list = new ArrayList<>();
                for (JsonElement jsonElement : discord)
                    list.add(jsonElement.getAsLong());
                user.setDiscordIdSet(list);
            } catch (JsonSyntaxException e) {
                logger.log(Level.WARNING, "Malformed JSON on Route DISCORD_UPDATE", e);
            } catch (ClassCastException | NullPointerException e) {
                logger.log(Level.WARNING, "Could not finalize DISCORD_UPDATE", e);
            }
        });
        this.routeManager.setRouteFinalizer(Routes.Content.User.MINECRAFT_UPDATE, inboundMessage -> {
            try {
                JsonObject json = new Gson().fromJson(inboundMessage.getRoute().content(), JsonObject.class);

                long id = json.get("user").getAsLong();
                UserImpl  user      = (UserImpl) userCache.get(id);
                JsonArray minecraft = json.getAsJsonArray("minecraft");

                if (user == null)
                    throw new NullPointerException();

                ArrayList<UUID> list = new ArrayList<>();
                for (JsonElement jsonElement : minecraft)
                    list.add(UUID.fromString(jsonElement.getAsString()));
                user.setMinecraftIdSet(list);
            } catch (JsonSyntaxException e) {
                logger.log(Level.WARNING, "Malformed JSON on Route MINECRAFT_UPDATE", e);
            } catch (IllegalArgumentException | ClassCastException | NullPointerException e) {
                logger.log(Level.WARNING, "Could not finalize MINECRAFT_UPDATE", e);
            }
        });

        /* --- CONTENT / GROUP */
        this.routeManager.setRouteFinalizer(Routes.Content.Group.DELETED, inboundMessage -> {
            groupCache.removeStringId(inboundMessage.getRoute().content());
        });
        this.routeManager.setRouteFinalizer(Routes.Content.Group.UPDATE, inboundMessage -> {
            groupCache.add(EntityBuilder.buildGroup(inboundMessage.getRoute().content()));
        });
        this.routeManager.setRouteFinalizer(Routes.Content.Group.UPDATES, inboundMessage -> {
            groupCache.addAll(EntityBuilder.buildGroups(inboundMessage.getRoute().content()));
        });

        this.netClient.start();
    }

    /**
     * Provides the root logger of this instance.
     * @return Instance root logger.
     */
    public @NotNull Logger getLogger() {
        return logger;
    }

    /**
     * Provides the name of this instance. The name can be set during initialization depending on the implementation.
     * @return Instance name.
     */
    public @Nullable String getName() {
        return name;
    }

    public @NotNull Version getVersion() {
        return this.version;
    }

    public NetClient getNetClient() {
        return netClient;
    }

    public @NotNull TurtleSet<User> getUserCache() {
        return userCache;
    }

    @Override
    public @Nullable User getUserById(long id) {
        return userCache.get(id);
    }

    /* - - - */

    @Override
    public @NotNull Consumer<Object> getDefaultActionSuccess() {
        return this.defaultOnSuccess;
    }

    @Override
    public @NotNull Consumer<? super Throwable> getDefaultActionFailure() {
        return this.defaultOnFailure;
    }

    @Override
    public void setDefaultActionSuccess(@NotNull Consumer<Object> consumer) {
        this.defaultOnSuccess = consumer;
    }

    @Override
    public void setDefaultActionFailure(@NotNull Consumer<? super Throwable> consumer) {
        this.defaultOnFailure = consumer;
    }

    public ScheduledThreadPoolExecutor getCallbackExecutor() {
        return callbackExecutor;
    }

    /* - - - */

    @SuppressWarnings("CodeBlock2Expr")
    @Override
    public @NotNull Action<User> retrieveUser(long id) {
        return new ActionImpl<User>(this, Routes.User.GET.compile(null, String.valueOf(id)), (message, userRequest) -> {
            return EntityBuilder.buildUser(message.getRoute().content());
        }).onSuccess(user -> {
            userCache.removeIf(oldUser -> oldUser.getId() == id);
            userCache.add(user);
        });
    }

    @SuppressWarnings("CodeBlock2Expr")
    @Override
    public @NotNull Action<List<User>> retrieveUsers() {
        return new ActionImpl<List<User>>(this, Routes.User.GET_ALL.compile(null), (message, userRequest) -> {
            return EntityBuilder.buildUsers(message.getRoute().content());
        }).onSuccess(l -> {
            userCache.clear();
            userCache.addAll(l);
        });
    }

    @SuppressWarnings("CodeBlock2Expr")
    @Override
    public @NotNull Action<Group> retrieveGroup(long id) {
        return new ActionImpl<Group>(this, Routes.Group.GET.compile(null, String.valueOf(id)), (message, userRequest) -> {
            return EntityBuilder.buildGroup(message.getRoute().content());
        }).onSuccess(group -> {
            groupCache.removeIf(oldGroup -> oldGroup.getId() == id);
            groupCache.add(group);
        });
    }

    @SuppressWarnings("CodeBlock2Expr")
    @Override
    public @NotNull Action<List<Group>> retrieveGroups() {
        return new ActionImpl<List<Group>>(this, Routes.Group.GET_ALL.compile(null), (message, userRequest) -> {
            return EntityBuilder.buildGroups(message.getRoute().content());
        }).onSuccess(l -> {
            groupCache.clear();
            groupCache.addAll(l);
        });
    }
}
