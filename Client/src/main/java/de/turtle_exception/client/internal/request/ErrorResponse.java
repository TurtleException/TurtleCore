package de.turtle_exception.client.internal.request;

import de.turtle_exception.client.internal.net.packets.ErrorPacket;
import org.jetbrains.annotations.NotNull;

public record ErrorResponse(@NotNull ErrorPacket packet) implements IResponse { }
