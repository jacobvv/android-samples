package org.jacobvv.baserecycler.listener;

import org.jacobvv.baserecycler.BaseRecyclerAdapter;
import org.jacobvv.baserecycler.BaseViewHolder;

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
    void onClick(BaseRecyclerAdapter<T> adapter, BaseViewHolder<T> holder,
                 T model, int position);
}
