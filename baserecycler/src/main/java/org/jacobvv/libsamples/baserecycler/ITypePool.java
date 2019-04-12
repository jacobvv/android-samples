package org.jacobvv.libsamples.baserecycler;

/**
 * @author jacob
 * @date 19-4-13
 */
public interface ITypePool {

    void register(ItemType itemType);

    void register(Class<?> itemClass, ItemType itemType);

    ItemType getType(int viewType);

    int getIndexOfType(Class<?> itemClass);
}
