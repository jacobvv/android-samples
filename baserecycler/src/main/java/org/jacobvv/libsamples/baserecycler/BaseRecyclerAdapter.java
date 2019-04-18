package org.jacobvv.libsamples.baserecycler;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Common base class of common implementation for an {@link RecyclerView.Adapter Adapter}
 * that can be used in {@link RecyclerView RecyclerView}.
 * <p>
 *
 * @author Jacob
 * @date 17-12-22
 */
public abstract class BaseRecyclerAdapter<T>
        extends RecyclerView.Adapter<BaseViewHolder<T>> {

    private TypePool mTypePool;

    BaseRecyclerAdapter() {
        this.mTypePool = new TypePool();
    }

    @NonNull
    @Override
    public BaseViewHolder<T> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemType<T> type = mTypePool.getType(viewType);
        View view = LayoutInflater.from(parent.getContext())
                .inflate(type.getLayoutId(viewType), null);
        BaseViewHolder<T> holder = new BaseViewHolder<>(view, type);
        type.onCreateViewHolder(holder, parent);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder<T> holder, int position) {
        ItemType<T> type = mTypePool.getType(holder.getItemViewType());
        T item = getItem(position);
        type.onBindViewHolder(holder, item, position);
        holder.setup(item, position);
    }

    @Override
    public int getItemViewType(int position) {
        return mTypePool.getIndexOfType(getItem(position).getClass());
    }

    public void register(@NonNull ItemType itemType) {
        register(null, itemType);
    }

    public void register(Class<? extends T> clazz, @NonNull ItemType itemType) {
        mTypePool.register(clazz, itemType);
    }

    protected abstract T getItem(int position);

}
