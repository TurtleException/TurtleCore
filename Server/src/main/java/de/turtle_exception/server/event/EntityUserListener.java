package de.turtle_exception.server.event;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import de.turtle_exception.client.api.entities.User;
import de.turtle_exception.client.api.event.user.*;
import de.turtle_exception.client.internal.data.Data;
import de.turtle_exception.client.internal.data.DataUtil;
import de.turtle_exception.server.net.NetServer;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class EntityUserListener extends AbstractListener {
    public EntityUserListener(@NotNull NetServer server) {
        super(server);
    }

    /* - - - */

    @Override
    public void onUserCreate(@NotNull UserCreateEvent event) {
        JsonObject json = server.getClientImpl().getJsonBuilder().buildJson(event.getUser());
        this.sendPacket(Data.buildUpdate(User.class, json));
    }

    @Override
    public void onUserDelete(@NotNull UserDeleteEvent event) {
        this.sendPacket(Data.buildRemove(User.class, event.getUser().getId()));
    }

    @Override
    public void onUserUpdate(@NotNull UserUpdateEvent<?> event) {
        JsonObject json = new JsonObject();
        DataUtil.addValue(json, "id", event.getUser().getId());
        DataUtil.addValue(json, event.getKey(), event.getNewValue());
        this.sendPacket(Data.buildUpdate(User.class, json));
    }

    @Override
    public void onUserDiscordAdd(@NotNull UserDiscordAddEvent event) {
        JsonObject json = new JsonObject();
        JsonArray  arr  = new JsonArray();
        DataUtil.addValue(json, "id", event.getUser().getId());
        for (Long discord : event.getUser().getDiscordIds())
            arr.add(discord);
        json.add("discord", arr);
        this.sendPacket(Data.buildUpdate(User.class, json));
    }

    @Override
    public void onUserDiscordRemove(@NotNull UserDiscordRemoveEvent event) {
        JsonObject json = new JsonObject();
        JsonArray  arr  = new JsonArray();
        DataUtil.addValue(json, "id", event.getUser().getId());
        for (Long discord : event.getUser().getDiscordIds())
            arr.add(discord);
        json.add("discord", arr);
        this.sendPacket(Data.buildUpdate(User.class, json));
    }

    @Override
    public void onUserMinecraftAdd(@NotNull UserMinecraftAddEvent event) {
        JsonObject json = new JsonObject();
        JsonArray  arr  = new JsonArray();
        DataUtil.addValue(json, "id", event.getUser().getId());
        for (UUID minecraft : event.getUser().getMinecraftIds())
            arr.add(minecraft.toString());
        json.add("minecraft", arr);
        this.sendPacket(Data.buildUpdate(User.class, json));
    }

    @Override
    public void onUserMinecraftRemove(@NotNull UserMinecraftRemoveEvent event) {
        JsonObject json = new JsonObject();
        JsonArray  arr  = new JsonArray();
        DataUtil.addValue(json, "id", event.getUser().getId());
        for (UUID minecraft : event.getUser().getMinecraftIds())
            arr.add(minecraft.toString());
        json.add("minecraft", arr);
        this.sendPacket(Data.buildUpdate(User.class, json));
    }
}
