package org.jacobvv.libsamples.baserecycler;

import android.support.annotation.IntRange;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by Jacob on 18-4-3.
 */

public class BaseArrayAdapter<T> extends BaseRecyclerAdapter<T> {

    private List<T> mData = new ArrayList<>();

    public BaseArrayAdapter() {
        super();
    }

    public BaseArrayAdapter(List<T> data) {
        super();
        mData.addAll(data);
    }

    @Override
    public T getItem(int pos) {
        return mData.get(pos);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public List<T> getData() {
        return mData;
    }

    public void setData(@NonNull Collection<? extends T> data) {
        mData.clear();
        mData.addAll(data);
        notifyDataSetChanged();
    }

    @Override
    public void add(@NonNull T data) {
        mData.add(data);
        if (mData.size() == 1) {
            notifyDataSetChanged();
        } else {
            notifyItemInserted(mData.size());
        }
    }

    @Override
    public void add(@IntRange(from = 0) int position, @NonNull T data) {
        mData.add(position, data);
        if (mData.size() == 1) {
            notifyDataSetChanged();
        } else {
            notifyItemInserted(position);
        }
    }

    @Override
    public void add(@NonNull Collection<? extends T> data) {
        mData.addAll(data);
        if (mData.size() == data.size()) {
            notifyDataSetChanged();
        } else {
            notifyItemRangeInserted(mData.size() - data.size(), data.size());
        }
    }

    @Override
    public void add(@IntRange(from = 0) int position,
                    @NonNull Collection<? extends T> data) {
        mData.addAll(position, data);
        if (mData.size() == data.size()) {
            notifyDataSetChanged();
        } else {
            notifyItemRangeInserted(position, data.size());
        }
    }

    @Override
    public void remove(@IntRange(from = 0) int position) {
        mData.remove(position);
        if (mData.isEmpty()) {
            notifyDataSetChanged();
        } else {
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, mData.size() - position);
        }
    }

    @Override
    public void clear() {
        mData.clear();
        notifyDataSetChanged();
    }
}
