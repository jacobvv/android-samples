package org.jacobvv.libsamples.baserecycler.listener;

import org.jacobvv.libsamples.baserecycler.BaseRecyclerAdapter;
import org.jacobvv.libsamples.baserecycler.BaseRecyclerViewHolder;

/**
 * @author jacob
 * @date 19-4-18
 */
public interface OnItemClickListener<T> {

    /**
     * Called when a view has been clicked.
     *
     * @param holder   clicked view holder.
     * @param model    the item model attach to clicked view.
     * @param position position in list of clicked view.
     */
    void onClick(BaseRecyclerAdapter<T> adapter, BaseRecyclerViewHolder<T> holder,
                 T model, int position);
}
