package de.turtle_exception.client.api.entities.messages;

import de.turtle_exception.client.api.entities.Turtle;
import de.turtle_exception.client.internal.data.annotations.Resource;

@Resource(path = "channels", builder = "buildSyncChannel")
public interface SyncChannel extends Turtle {

}
