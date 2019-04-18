package org.jacobvv.libsamples.baserecycler.listener;

import org.jacobvv.libsamples.baserecycler.BaseRecyclerAdapter;
import org.jacobvv.libsamples.baserecycler.BaseViewHolder;

/**
 * @author jacob
 * @date 19-4-18
 */
public interface OnItemLongClickListener<T> {

    /**
     * Called when a view has been clicked.
     *
     * @param holder   clicked view holder.
     * @param model    the item model attach to clicked view.
     * @param position position in list of clicked view.
     */
    boolean onLongClick(BaseRecyclerAdapter<T> adapter, BaseViewHolder<T> holder,
                        T model, int position);
}