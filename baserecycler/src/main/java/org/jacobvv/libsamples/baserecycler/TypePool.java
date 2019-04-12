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

    public TypePool() {
        this.classes = new ArrayList<>();
        this.types = new ArrayList<>();
    }

    @Override
    public void register(Class<?> itemClass, ItemType itemType) {
        classes.add(itemClass);
        types.add(itemType);
    }

    @Override
    public int size() {
        return types.size();
    }

    @Override
    public ItemType getType(int viewType) {
        return types.get(viewType);
    }

    @Override
    public ItemType getType(Class<?> itemClass) {
        return types.get(classes.indexOf(itemClass));
    }

    @Override
    public int getIndexOfType(Class<?> itemClass) {
        return classes.indexOf(itemClass);
    }
}
