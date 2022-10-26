package de.turtle_exception.core.internal.data.tree;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.ConcurrentHashMap;

@SuppressWarnings("unused")
public class DataObject extends DataElement {
    private final ConcurrentHashMap<String, DataElement> map = new ConcurrentHashMap<>();

    public DataObject() { }

    public void set(@NotNull String key, @Nullable DataElement element) {
        if (element != null)
            map.put(key, element);
        else
            map.remove(key);
    }

    public @Nullable DataElement get(@NotNull String key) {
        return map.get(key);
    }
}
