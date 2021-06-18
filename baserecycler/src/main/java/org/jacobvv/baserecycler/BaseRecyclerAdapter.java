package org.jacobvv.baserecycler;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Collection;

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
    @SuppressWarnings("unchecked")
    public BaseViewHolder<T> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemType type = mTypePool.getType(viewType);
        View view = LayoutInflater.from(parent.getContext())
                .inflate(type.getLayoutId(viewType), parent, false);
        return type.onCreateViewHolder(view, type);
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder<T> holder, int position) {
        ItemType<T, BaseViewHolder<T>> type = mTypePool.getType(holder.getItemViewType());
        T item = getItem(position);
        type.onBindViewHolder(holder, item, position);
        holder.setup(item, position);
    }

    @Override
    public int getItemViewType(int position) {
        return mTypePool.getIndexOfType(getItem(position).getClass());
    }

    public <Type, VH extends BaseViewHolder<Type>> ItemType<Type, VH> getItemType(int position) {
        return mTypePool.getType(getItemViewType(position));
    }

    public <Type> void register(@NonNull ItemType<Type, ?> itemType) {
        register(null, itemType);
    }


    @SuppressWarnings("unchecked")
    public <Type> void register(Class<? extends Type> clazz, @NonNull ItemType<Type, ?> itemType) {
        mTypePool.register(clazz, itemType);
        itemType.setAdapter((BaseRecyclerAdapter<Type>) this);
    }

    protected abstract T getItem(int position);

    public abstract void add(@NonNull T data);

    public abstract void add(@IntRange(from = 0) int position, @NonNull T data);

    public abstract void add(@NonNull Collection<? extends T> data);

    public abstract void add(@IntRange(from = 0) int position,
                             @NonNull Collection<? extends T> data);

    public abstract void remove(@IntRange(from = 0) int position);

    public abstract void clear();

    public abstract boolean isFirstItem(int position);

    public abstract boolean isLastItem(int position);

    public abstract boolean atFirstRow(int position, int spanCount);

    public abstract boolean atLastRow(int position, int spanCount);

    public abstract boolean atFirstColumn(int position, int spanCount);

    public abstract boolean atLastColumn(int position, int spanCount);

}
