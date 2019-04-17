package org.jacobvv.libsamples.baserecycler;

import android.view.View;

/**
 * @author jacob
 * @date 19-4-13
 */
public interface ItemType<T> {

    int getLayoutId(int type);

    BaseRecyclerViewHolder<T> createViewHolder(int viewType, View view);
}
