package org.jacobvv.libsamples.baserecycler;

import android.content.Context;
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
 * @since 17-12-23
 */
public abstract class BaseRecyclerViewHolder<T> extends RecyclerView.ViewHolder {

    protected Context mContext;
    private SparseArray<View> mViews = new SparseArray<>();
    private T mModel;
    private int mPosition;

    @SuppressWarnings("unchecked")
    protected BaseRecyclerViewHolder(View v, final OnItemClickListener<T> listener) {
        super(v);
        mContext = v.getContext();
        if (listener == null) {
            return;
        }
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(BaseRecyclerViewHolder.this, mModel, mPosition);
            }
        });
    }

    @SuppressWarnings("unchecked")
    protected BaseRecyclerViewHolder(View v, SparseArray<OnItemClickListener<T>> listeners) {
        super(v);
        mContext = v.getContext();
        if (listeners == null) {
            return;
        }
        for (int i = 0; i < listeners.size(); i++) {
            int resId = listeners.keyAt(i);
            final OnItemClickListener<T> l = listeners.get(resId);
            View child = getView(resId);
            if (child == null) {
                continue;
            }
            child.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    l.onClick(BaseRecyclerViewHolder.this, mModel, mPosition);
                }
            });
        }
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

    public T getModel() {
        return mModel;
    }

    public void setModel(T mModel, int position) {
        this.mModel = mModel;
        this.mPosition = position;
    }

    /**
     * Set up views by item model.
     *
     * @param model    model of item.
     * @param position position of list in adapter.
     */
    public abstract void setUpView(T model, int position);

    public interface OnItemClickListener<T> {

        /**
         * Called when a view has been clicked.
         *
         * @param holder   clicked view holder.
         * @param model    the item model attach to clicked view.
         * @param position position in list of clicked view.
         */
        void onClick(BaseRecyclerViewHolder<T> holder, T model, int position);
    }

}
