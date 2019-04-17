package org.jacobvv.libsamples.baserecycler;

import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;

/**
 * Base view holder for {@link RecyclerView RecyclerView}.
 * This class provide following function:
 * 1. Cache of item view & sub views.
 * 2. Set a series of click listeners of item view or sub views.
 * 3. Set up views by item model.
 * 4. Interface of item type.
 *
 * @author Jacob
 * @date 17-12-23
 */
public abstract class BaseRecyclerViewHolder<T> extends RecyclerView.ViewHolder {

    private SparseArray<View> mViews = new SparseArray<>();

    public BaseRecyclerViewHolder(View itemView) {
        super(itemView);
    }

    @SuppressWarnings("unchecked")
    public <V extends View> V getView(int resId) {
        View view = mViews.get(resId);
        if (view == null) {
            view = itemView.findViewById(resId);
            if (view == null) {
                return null;
            }
            mViews.put(resId, view);
        }
        return (V) view;
    }

    /**
     * Set up views by item model.
     *
     * @param model    model of item.
     * @param position position of list in adapter.
     */
    public abstract void setUpView(T model, int position);

}
