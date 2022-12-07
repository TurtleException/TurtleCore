package de.turtle_exception.client.api.event.entities;

import de.turtle_exception.client.api.entities.attributes.EphemeralType;
import org.jetbrains.annotations.NotNull;

public class EphemeralEntityEvent<T extends EphemeralType> extends EntityEvent<T> {
    public EphemeralEntityEvent(@NotNull T entity) {
        super(entity);
    }
}
