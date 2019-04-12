package org.jacobvv.libsamples.baserecycler;

/**
 * @author jacob
 * @since 19-4-13
 */
public interface ITypePool {

    void register(Class<?> itemClass, ItemType itemType);

    int size();

    ItemType getType(int viewType);

    ItemType getType(Class<?> itemClass);

    int getIndexOfType(Class<?> itemClass);
}
