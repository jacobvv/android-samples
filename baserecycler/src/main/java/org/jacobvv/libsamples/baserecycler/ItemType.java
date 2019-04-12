package org.jacobvv.libsamples.baserecycler;

import android.support.annotation.IdRes;
import android.view.View;

/**
 * @author jacob
 * @since 19-4-13
 */
public interface ItemType {

    int getLayoutId(int type);

    <T> BaseRecyclerViewHolder<T> createViewHolder(int viewType, View view);
}
