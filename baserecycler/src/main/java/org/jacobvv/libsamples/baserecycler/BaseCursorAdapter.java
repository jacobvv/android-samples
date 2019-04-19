package org.jacobvv.libsamples.baserecycler;

import android.database.Cursor;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.util.SparseArray;

import java.util.Collection;

/**
 * 可插入移除的Cursor适配器，支持缓存。
 * 额外插入的数据使用Map保存位置，并依据该位置，计算Cursor数据的位置偏移。
 *
 * @author Jacob
 * @date 18-4-3
 */
public abstract class BaseCursorAdapter<T> extends BaseRecyclerAdapter<T> {

    private Cursor mCursor;
    private SparseArray<T> mData = new SparseArray<>();
    private SparseArray<T> mCache = new SparseArray<>();

    public BaseCursorAdapter() {
        super();
    }

    public BaseCursorAdapter(Cursor mCursor) {
        super();
        this.mCursor = mCursor;
    }

    @Override
    public T getItem(int pos) {
        checkDataValid();
        T item = mCache.get(pos);
        if (item != null) {
            return item;
        } else if (isInCursor(pos)) {
            int cursorPos = calculateCursorPosition(pos);
            checkDataValid(cursorPos);
            item = fromCursor(mCursor);
            mCache.put(pos, item);
        } else {
            item = mData.get(pos);
        }
        return item;
    }

    @Override
    public int getItemCount() {
        if (isDataInvalid(mCursor)) {
            return 0;
        } else {
            return mCursor.getCount() + mData.size();
        }
    }

    public void setCursor(Cursor cursor) {
        if (cursor == mCursor) {
            return;
        }
        mData.clear();
        mCache.clear();
        if (cursor != null) {
            mCursor = cursor;
            // notify the observers about the new cursor
            notifyDataSetChanged();
        } else {
            notifyItemRangeRemoved(0, getItemCount());
            mCursor = null;
        }
    }

    @Override
    public void add(@NonNull T data) {
        checkDataValid();
        mCache.clear();
        int count = getItemCount();
        mData.put(count, data);
        if (getItemCount() == 1) {
            notifyDataSetChanged();
        } else {
            notifyItemInserted(count);
        }
    }

    @Override
    public void add(@IntRange(from = 0) int position, @NonNull T data) {
        int count = getItemCount();
        if (position > count || position < 0) {
            throw new IndexOutOfBoundsException("Position out of bounds: position="
                    + position + ", size=" + count);
        }
        checkDataValid();
        mCache.clear();
        for (int max = count - 1; max >= 0; max--) {
            if (max >= position) {
                T item = mData.get(max);
                if (item != null) {
                    mData.put(max + 1, item);
                    mData.remove(max);
                }
            }
        }
        mData.put(position, data);
        if (getItemCount() == 1) {
            notifyDataSetChanged();
        } else {
            notifyItemInserted(position);
        }
    }

    @Override
    public void add(@NonNull Collection<? extends T> data) {
        checkDataValid();
        mCache.clear();
        int count = getItemCount();
        int i = 0;
        for (T o : data) {
            mData.put(count + i, o);
            i++;
        }
        if (getItemCount() == data.size()) {
            notifyDataSetChanged();
        } else {
            notifyItemRangeInserted(getItemCount() - data.size(), data.size());
        }
    }

    @Override
    public void add(@IntRange(from = 0) int position,
                    @NonNull Collection<? extends T> data) {
        int count = getItemCount();
        if (position > count || position < 0) {
            throw new IndexOutOfBoundsException("Position out of bounds: position="
                    + position + ", size=" + count);
        }
        checkDataValid();
        mCache.clear();
        int size = data.size();
        for (int max = count - 1; max >= 0; max--) {
            if (max >= position) {
                T item = mData.get(max);
                if (item != null) {
                    mData.put(max + size, item);
                    mData.remove(max);
                }
            }
        }
        int i = 0;
        for (T o : data) {
            mData.put(position + i, o);
            i++;
        }
        if (getItemCount() == size) {
            notifyDataSetChanged();
        } else {
            notifyItemRangeInserted(position, size);
        }
    }

    @Override
    public void remove(int position) {
        // TODO: Not completed yet.
    }

    @Override
    public void clear() {
        mCache.clear();
        mData.clear();
        mCursor.close();
        mCursor = null;
    }

    private boolean isInCursor(int pos) {
        return mData.get(pos) == null;
    }

    private int calculateCursorPosition(int pos) {
        int offset = 0;
        for (int i = 0; i < mData.size(); i++) {
            int key = mData.keyAt(i);
            if (key <= pos) {
                offset++;
            }
        }
        return pos - offset;
    }

    private void checkDataValid() {
        if (isDataInvalid(mCursor)) {
            throw new IllegalStateException(
                    "Cannot lookup item id because the cursor is in invalid state.");
        }
    }

    private void checkDataValid(int position) {
        if (isDataInvalid(mCursor)) {
            throw new IllegalStateException(
                    "Cannot lookup item id because the cursor is in invalid state.");
        }
        if (!mCursor.moveToPosition(position)) {
            throw new IllegalStateException("Could not move cursor to position " + position
                    + " when trying to get an item id");
        }
    }

    private boolean isDataInvalid(Cursor cursor) {
        return cursor == null || cursor.isClosed();
    }

    public abstract T fromCursor(Cursor current);
}
