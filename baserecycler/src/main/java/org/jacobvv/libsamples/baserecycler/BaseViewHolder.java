package org.jacobvv.libsamples.baserecycler;

import android.support.annotation.IdRes;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;

import org.jacobvv.libsamples.baserecycler.listener.OnItemClickListener;
import org.jacobvv.libsamples.baserecycler.listener.OnItemLongClickListener;
import org.jacobvv.libsamples.baserecycler.listener.OnViewClickListener;
import org.jacobvv.libsamples.baserecycler.listener.OnViewLongClickListener;

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
public class BaseViewHolder<T> extends RecyclerView.ViewHolder {

    private SparseArray<View> mViews = new SparseArray<>();
    private T mItem;
    private int mPosition;

    BaseViewHolder(View itemView, ItemType<T> type) {
        super(itemView);
        BaseRecyclerAdapter<T> adapter = type.getAdapter();
        setOnItemClickListener(adapter, type.mItemClickListener);
        setOnItemLongClickListener(adapter, type.mItemLongClickListener);
        SparseArray<OnViewClickListener<T>> clickListeners = type.mViewClickListeners;
        for (int i = clickListeners.size() - 1; i >= 0; i--) {
            addOnClickListener(adapter, clickListeners.keyAt(i), clickListeners.valueAt(i));
        }
        SparseArray<OnViewLongClickListener<T>> longClickListeners = type.mViewLongClickListeners;
        for (int i = longClickListeners.size() - 1; i >= 0; i--) {
            addOnLongClickListener(adapter, longClickListeners.keyAt(i),
                    longClickListeners.valueAt(i));
        }
    }

    void setup(T item, int pos) {
        this.mItem = item;
        this.mPosition = pos;
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

    private void setOnItemClickListener(final BaseRecyclerAdapter<T> adapter,
                                        final OnItemClickListener<T> l) {
        if (l != null) {
            this.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    l.onClick(adapter, BaseViewHolder.this, mItem,
                            getAdapterPosition());
                }
            });
        }
    }

    private void setOnItemLongClickListener(final BaseRecyclerAdapter<T> adapter,
                                            final OnItemLongClickListener<T> l) {
        if (l != null) {
            this.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    return l.onLongClick(adapter, BaseViewHolder.this, mItem,
                            getAdapterPosition());
                }
            });
        }
    }

    private void addOnClickListener(final BaseRecyclerAdapter<T> adapter,
                                    @IdRes int id, final OnViewClickListener<T> l) {
        if (l != null) {
            getView(id).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    l.onClick(adapter, BaseViewHolder.this, v, mItem,
                            getAdapterPosition());
                }
            });
        }
    }

    private void addOnLongClickListener(final BaseRecyclerAdapter<T> adapter,
                                        @IdRes int id, final OnViewLongClickListener<T> l) {
        if (l != null) {
            getView(id).setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    return l.onLongClick(adapter, BaseViewHolder.this, v, mItem,
                            getAdapterPosition());
                }
            });
        }
    }

}
