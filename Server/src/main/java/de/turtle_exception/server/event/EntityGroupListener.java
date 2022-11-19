package de.turtle_exception.server.event;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import de.turtle_exception.client.api.entities.Group;
import de.turtle_exception.client.api.entities.User;
import de.turtle_exception.client.api.event.group.*;
import de.turtle_exception.client.internal.data.Data;
import de.turtle_exception.client.internal.data.DataUtil;
import de.turtle_exception.server.net.NetServer;
import org.jetbrains.annotations.NotNull;

public class EntityGroupListener extends AbstractListener {
    public EntityGroupListener(@NotNull NetServer server) {
        super(server);
    }

    /* - - - */

    @Override
    public void onGroupCreate(@NotNull GroupCreateEvent event) {
        JsonObject json = server.getClientImpl().getJsonBuilder().buildJson(event.getGroup());
        this.sendPacket(Data.buildUpdate(Group.class, json));
    }

    @Override
    public void onGroupDelete(@NotNull GroupDeleteEvent event) {
        this.sendPacket(Data.buildRemove(Group.class, event.getGroup().getId()));
    }

    @Override
    public void onGroupUpdate(@NotNull GroupUpdateEvent<?> event) {
        JsonObject json = new JsonObject();
        DataUtil.addValue(json, "id", event.getGroup().getId());
        DataUtil.addValue(json, event.getKey(), event.getNewValue());
        this.sendPacket(Data.buildUpdate(Group.class, json));
    }

    @Override
    public void onGroupMemberJoin(@NotNull GroupMemberJoinEvent event) {
        JsonObject json = new JsonObject();
        JsonArray  arr  = new JsonArray();
        DataUtil.addValue(json, "id", event.getGroup().getId());
        for (User user : event.getGroup().getUsers())
            arr.add(user.getId());
        json.add("users", arr);
        this.sendPacket(Data.buildUpdate(Group.class, json));
    }

    @Override
    public void onGroupMemberLeave(@NotNull GroupMemberLeaveEvent event) {
        JsonObject json = new JsonObject();
        JsonArray  arr  = new JsonArray();
        DataUtil.addValue(json, "id", event.getGroup().getId());
        for (User user : event.getGroup().getUsers())
            arr.add(user.getId());
        json.add("users", arr);
        this.sendPacket(Data.buildUpdate(Group.class, json));
    }
}
