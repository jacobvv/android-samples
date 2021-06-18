package org.jacobvv.baserecycler;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;

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

    @Override
    public boolean isFirstItem(int position) {
        return position == 0;
    }

    @Override
    public boolean isLastItem(int position) {
        return position == mData.size() - 1;
    }

    @Override
    public boolean atFirstRow(int position, int spanCount) {
        if (isFirstItem(position)) {
            return true;
        }
        for (int pos = 0; pos <= position; pos++) {
            spanCount -= getItemType(pos).getSpanSize();
            if (spanCount < 0) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean atLastRow(int position, int spanCount) {
        if (isLastItem(position)) {
            return true;
        }
        int span = spanCount;
        int size = mData.size();
        for (int pos = mData.size() - 1; pos >= position; pos--) {
            span -= getItemType(pos).getSpanSize();
            if (span < 0) {
                return false;
            }
        }
        if (isFirstItem(position)) {
            return true;
        }
        int spanTotal = 0;
        for (int pos = position - 1; pos >= 0; pos--) {
            span = getItemType(pos).getSpanSize();
            if (span == spanCount) {
                break;
            }
            spanTotal += span;
        }
        spanTotal = spanTotal % spanCount;
        for (int pos = position; pos < size; pos++) {
            span = getItemType(pos).getSpanSize();
            spanTotal += span;
            if (spanTotal > spanCount) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean atFirstColumn(int position, int spanCount) {
        return false;
    }

    @Override
    public boolean atLastColumn(int position, int spanCount) {
        return false;
    }

}
