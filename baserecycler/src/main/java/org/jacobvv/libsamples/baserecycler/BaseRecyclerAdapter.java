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
 * @since 17-12-22
 */
public abstract class BaseRecyclerAdapter<T extends BaseItemModel>
        extends RecyclerView.Adapter<BaseRecyclerViewHolder<T>> {

    private BaseTypeFactory<T> mTypeFactory;

    protected BaseRecyclerAdapter(BaseTypeFactory<T> factory) {
        mTypeFactory = factory;
    }

    @NonNull
    @Override
    public BaseRecyclerViewHolder<T> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layoutId = mTypeFactory.getLayoutId(viewType);
        View itemView = LayoutInflater.from(parent.getContext()).inflate(layoutId, null);
        return mTypeFactory.createViewHolder(viewType, itemView);
    }

    @Override
    public void onBindViewHolder(BaseRecyclerViewHolder<T> holder, int position) {
        T model = getItem(position);
        holder.setModel(model, position);
        holder.setUpView(model, position);
    }

    @Override
    public int getItemViewType(int position) {
        return getItem(position).getType();
    }

    /**
     * Get item model of given position.
     *
     * @param pos position of request model.
     * @return item model of given position.
     */
    protected abstract T getItem(int pos);
}
