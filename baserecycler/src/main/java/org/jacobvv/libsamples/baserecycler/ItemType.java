package org.jacobvv.libsamples.baserecycler;

import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.util.SparseArray;
import android.view.View;

import org.jacobvv.libsamples.baserecycler.listener.OnItemClickListener;
import org.jacobvv.libsamples.baserecycler.listener.OnItemLongClickListener;
import org.jacobvv.libsamples.baserecycler.listener.OnViewClickListener;
import org.jacobvv.libsamples.baserecycler.listener.OnViewLongClickListener;

/**
 * @author jacob
 * @date 19-4-13
 */
public abstract class ItemType<T, VH extends BaseViewHolder<T>> {

    OnItemClickListener<T> mItemClickListener;
    OnItemLongClickListener<T> mItemLongClickListener;
    SparseArray<OnViewClickListener<T>> mViewClickListeners = new SparseArray<>();
    SparseArray<OnViewLongClickListener<T>> mViewLongClickListeners = new SparseArray<>();
    private BaseRecyclerAdapter<T> mAdapter;

    public void setOnItemClickListener(OnItemClickListener<T> l) {
        this.mItemClickListener = l;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener<T> l) {
        this.mItemLongClickListener = l;
    }

    public void addOnViewClickListener(@IdRes int id, OnViewClickListener<T> l) {
        if (l != null) {
            this.mViewClickListeners.put(id, l);
        }
    }

    public void addOnViewLongClickListener(@IdRes int id, OnViewLongClickListener<T> l) {
        if (l != null) {
            this.mViewLongClickListeners.put(id, l);
        }
    }

    public abstract int getLayoutId(int type);

    @NonNull
    @SuppressWarnings("unchecked")
    public VH onCreateViewHolder(@NonNull View view,
                                 @NonNull ItemType<T, VH> type) {
        return (VH) new BaseViewHolder<>(view, type);
    }

    public abstract void onBindViewHolder(@NonNull VH holder, T model, int position);

    void setAdapter(BaseRecyclerAdapter<T> adapter) {
        this.mAdapter = adapter;
    }

    public BaseRecyclerAdapter<T> getAdapter() {
        return mAdapter;
    }
}
