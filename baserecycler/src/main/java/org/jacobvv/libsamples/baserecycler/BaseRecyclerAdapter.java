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
        extends RecyclerView.Adapter<BaseRecyclerViewHolder<T>> {

    private TypePool typePool;

    BaseRecyclerAdapter() {
        this.typePool = new TypePool();
    }

    @NonNull
    @Override
    public BaseRecyclerViewHolder<T> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemType<T> type = typePool.getType(viewType);
        View view = LayoutInflater.from(parent.getContext())
                .inflate(type.getLayoutId(viewType), null);
        return type.createViewHolder(viewType, view);
    }

    @Override
    public void onBindViewHolder(@NonNull BaseRecyclerViewHolder<T> holder, int position) {
        holder.setUpView(getItem(position), position);
    }

    @Override
    public int getItemViewType(int position) {
        return typePool.getIndexOfType(getItem(position).getClass());
    }

    public void register(@NonNull ItemType itemType) {
        typePool.register(itemType);
    }

    public void register(@NonNull Class<? extends T> clazz, @NonNull ItemType itemType) {
        typePool.register(clazz, itemType);
    }

    protected abstract T getItem(int position);

}
