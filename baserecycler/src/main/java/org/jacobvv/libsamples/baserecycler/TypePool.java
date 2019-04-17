package org.jacobvv.libsamples.baserecycler;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jacob
 * @date 19-4-13
 */
public class TypePool implements ITypePool {

    private final List<Class<?>> classes;
    private final List<ItemType> types;
    private boolean isMultiType = true;

    public TypePool() {
        this.classes = new ArrayList<>();
        this.types = new ArrayList<>();
    }

    @Override
    public void register(ItemType itemType) {
        if (!types.isEmpty()) {
            throw new RuntimeException();
        }
        isMultiType = false;
        types.add(itemType);
    }

    @Override
    public void register(Class<?> itemClass, ItemType itemType) {
        if (!isMultiType) {
            throw new RuntimeException();
        }
        classes.add(itemClass);
        types.add(itemType);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> ItemType<T> getType(int viewType) {
        return types.get(viewType);
    }

    @Override
    public int getIndexOfType(Class<?> itemClass) {
        if (isMultiType) {
            return classes.indexOf(itemClass);
        } else {
            return 0;
        }
    }
}
