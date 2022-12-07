package de.turtle_exception.server.event;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import de.turtle_exception.client.api.entities.Group;
import de.turtle_exception.client.api.entities.Turtle;
import de.turtle_exception.client.api.event.Event;
import de.turtle_exception.client.api.event.EventListener;
import de.turtle_exception.client.api.event.entities.*;
import de.turtle_exception.client.internal.data.Data;
import de.turtle_exception.client.internal.data.ResourceUtil;
import de.turtle_exception.client.internal.data.annotations.Keys;
import de.turtle_exception.client.internal.net.Connection;
import de.turtle_exception.client.internal.net.packets.DataPacket;
import de.turtle_exception.client.internal.util.time.TurtleType;
import de.turtle_exception.client.internal.util.time.TurtleUtil;
import de.turtle_exception.server.net.NetServer;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.function.Function;

public class EntityUpdateListener extends EventListener {
    private final @NotNull NetServer server;

    public EntityUpdateListener(@NotNull NetServer server) {
        this.server = server;
    }

    private final void sendPacket(@NotNull Data data) {
        final long deadline = System.currentTimeMillis() + server.getClient().getTimeoutOutbound();
        final long responseCode = TurtleUtil.newId(TurtleType.RESPONSE_CODE);

        for (Connection client : server.getClients())
            client.send(new DataPacket(deadline, client, responseCode, data), true);
    }

    /* - - - */

    @Override
    public void onGenericEvent(@NotNull Event event) {
        if (!(event instanceof EntityEvent<?>)) return;

        if (event instanceof EntityCreateEvent<?> e)
            onCreate(e.getEntity());

        if (event instanceof EntityDeleteEvent<?> e)
            onDelete(e.getEntity());

        if (event instanceof EntityUpdateEvent<?,?> e)
            onUpdate(e.getKey(), e.getNewValue(), e.getEntity());

        if (event instanceof EntityUpdateEntryEvent<?,?> e)
            onUpdateCollection(e.getEntity(), e.getKey(), e.getCollection(), e.getMutator());
    }

    private void onCreate(@NotNull Turtle turtle) {
        JsonObject json = server.getClientImpl().getResourceBuilder().buildJson(turtle);
        this.sendPacket(Data.buildUpdate(turtle.getClass(), json));
    }

    private void onDelete(@NotNull Turtle turtle) {
        this.sendPacket(Data.buildRemove(turtle.getClass(), turtle.getId()));
    }

    private void onUpdate(@NotNull String key, @NotNull Object value, @NotNull Turtle turtle) {
        JsonObject json = new JsonObject();
        ResourceUtil.addValue(json, Keys.Turtle.ID, turtle.getId());
        ResourceUtil.addValue(json, key, value);
        this.sendPacket(Data.buildUpdate(turtle.getClass(), json));
    }

    @SuppressWarnings("unchecked")
    private <U> void onUpdateCollection(@NotNull Turtle turtle, @NotNull String key, @NotNull Collection<?> c, @NotNull Function<U, Object> mutator) {
        JsonObject json = new JsonObject();
        JsonArray  arr  = new JsonArray();
        ResourceUtil.addValue(json, Keys.Turtle.ID, turtle.getId());
        /*
         * Casting an entry of c to U is safe because this method is only used for EntityUpdateEntryEvents, which guarantees
         * that with a Function<U, Object>, you also have a Collection<U>.
         */
        for (Object obj : c)
            ResourceUtil.addValue(arr, mutator.apply((U) obj));
        json.add(key, arr);
        this.sendPacket(Data.buildUpdate(turtle.getClass(), json));
    }
}
