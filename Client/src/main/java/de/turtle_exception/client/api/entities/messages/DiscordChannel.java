package de.turtle_exception.client.api.entities.messages;

import de.turtle_exception.client.internal.data.annotations.Key;
import de.turtle_exception.client.internal.data.annotations.Resource;

@Resource(path = "discord_channels", builder = "buildDiscordChannel")
public interface DiscordChannel extends IChannel {
    @Key(name = "snowflake", sqlType = "")
    long getSnowflake();
}
