package org.jacobvv.libsamples.baserecycler;

/**
 * @author jacob
 * @date 19-4-13
 */
interface ITypePool {

    void register(Class<?> itemClass, ItemType itemType);

    <T, VH extends BaseViewHolder<T>> ItemType<T, VH> getType(int viewType);

    int getIndexOfType(Class<?> itemClass);
}
