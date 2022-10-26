package de.turtle_exception.core.internal.data.tree;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

@SuppressWarnings("unused")
public class DataArray extends DataElement implements Iterable<DataElement> {
    private final List<DataElement> arr = Collections.synchronizedList(new ArrayList<>());

    public DataArray() { }

    public void add(@NotNull DataElement element) {
        arr.add(element);
    }

    public @Nullable DataElement get(int i) throws IndexOutOfBoundsException {
        return arr.get(i);
    }

    @NotNull
    @Override
    public Iterator<DataElement> iterator() {
        return this.arr.iterator();
    }
}
