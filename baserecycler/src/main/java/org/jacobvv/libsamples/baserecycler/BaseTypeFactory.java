package org.jacobvv.libsamples.baserecycler;

import android.support.annotation.LayoutRes;
import android.view.View;

/**
 * Created by Jacob on 18-4-3.
 */
public interface BaseTypeFactory<T extends BaseItemModel> {
    @LayoutRes
    int getLayoutId(int type);

    BaseRecyclerViewHolder<T> createViewHolder(int viewType, View itemView);
}
