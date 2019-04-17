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
public class BaseRecyclerViewHolder<T> extends RecyclerView.ViewHolder {

    private BaseRecyclerAdapter<T> mAdapter;
    private ItemType<T> mType;
    private SparseArray<View> mViews = new SparseArray<>();
    private T mItem;

    BaseRecyclerViewHolder(BaseRecyclerAdapter<T> adapter, View itemView, ItemType<T> type) {
        super(itemView);
        this.mType = type;
        this.mAdapter = adapter;
    }

    void setup(T item, int position) {
        this.mItem = item;
        mType.setupView(this, item, position);
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

    public ItemType<T> getType() {
        return mType;
    }

    public void setOnItemClickListener(final OnItemClickListener<T> l) {
        if (l != null) {
            this.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    l.onClick(mAdapter, BaseRecyclerViewHolder.this, mItem,
                            getAdapterPosition());
                }
            });
        }
    }

    public void setOnItemLongClickListener(final OnItemLongClickListener<T> l) {
        if (l != null) {
            this.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    return l.onLongClick(mAdapter, BaseRecyclerViewHolder.this, mItem,
                            getAdapterPosition());
                }
            });
        }
    }

    public void addOnClickListener(@IdRes int id, final OnViewClickListener<T> l) {
        if (l != null) {
            getView(id).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    l.onClick(mAdapter, BaseRecyclerViewHolder.this, v, mItem,
                            getAdapterPosition());
                }
            });
        }
    }

    public void addOnLongClickListener(@IdRes int id, final OnViewLongClickListener<T> l) {
        if (l != null) {
            getView(id).setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    return l.onLongClick(mAdapter, BaseRecyclerViewHolder.this, v, mItem,
                            getAdapterPosition());
                }
            });
        }
    }

}
