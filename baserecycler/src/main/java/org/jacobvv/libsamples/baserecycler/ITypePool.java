package org.jacobvv.libsamples.baserecycler;

/**
 * @author jacob
 * @date 19-4-13
 */
interface ITypePool {

    void register(ItemType itemType);

    void register(Class<?> itemClass, ItemType itemType);

    <T> ItemType<T> getType(int viewType);

    int getIndexOfType(Class<?> itemClass);
}
