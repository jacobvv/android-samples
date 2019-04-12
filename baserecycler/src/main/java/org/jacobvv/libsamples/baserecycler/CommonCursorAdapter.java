//package org.jacobvv.libsamples.baserecycler;
//
//import android.database.Cursor;
//import android.support.annotation.IntRange;
//import android.support.annotation.NonNull;
//import android.util.SparseArray;
//
//import java.util.Collection;
//
///**
// * @author Jacob
// * @since 18-4-3
// */
//public class CommonCursorAdapter<T extends BaseItemModel> extends BaseRecyclerAdapter<T> {
//
//    private static final String TAG = "CommonCursorAdapter";
//
//    private Cursor mCursor;
//    private SparseArray<T> mData = new SparseArray<>();
//    private CommonCursorModelFactory<T> mModelFactory;
//
//    private SparseArray<T> mCache = new SparseArray<>();
//
//    public CommonCursorAdapter(BaseTypeFactory<T> factory, CommonCursorModelFactory<T> modelFactory) {
//        this(factory, modelFactory, null);
//    }
//
//    public CommonCursorAdapter(BaseTypeFactory<T> factory, CommonCursorModelFactory<T> modelFactory, Cursor mCursor) {
//        super(factory);
//        this.mModelFactory = modelFactory;
//        this.mCursor = mCursor;
//    }
//
//    @Override
//    protected T getItem(int pos) {
//        checkDataValid();
//        T item = mCache.get(pos);
//        if (item != null) {
//            return item;
//        } else {
//            if (isInCursor(pos)) {
//                int cursorPos = calculateCursorPosition(pos);
//                checkDataValid(cursorPos);
//                item = mModelFactory.fromCursor(mCursor);
//            } else {
//                item = mData.get(pos);
//            }
//            mCache.put(pos, item);
//            return item;
//        }
//    }
//
//    @Override
//    public int getItemCount() {
//        if (!isDataValid(mCursor)) {
//            return 0;
//        } else {
//            return mCursor.getCount() + mData.size();
//        }
//    }
//
//    public void setCursor(Cursor cursor) {
//        if (cursor == mCursor) {
//            return;
//        }
//        mData.clear();
//        mCache.clear();
//        if (cursor != null) {
//            mCursor = cursor;
//            // notify the observers about the new cursor
//            notifyDataSetChanged();
//        } else {
//            notifyItemRangeRemoved(0, getItemCount());
//            mCursor = null;
//        }
//    }
//
//    public void addData(@NonNull T data) {
//        checkDataValid();
//        mCache.clear();
//        int count = getItemCount();
//        mData.put(count, data);
//        if (getItemCount() == 1) {
//            notifyDataSetChanged();
//        } else {
//            notifyItemInserted(count);
//        }
//    }
//
//    public void addData(@IntRange(from = 0) int position, @NonNull T data) {
//        int count = getItemCount();
//        if (position > count || position < 0) {
//            throw new IndexOutOfBoundsException("Position out of bounds: position="
//                    + position + ", size=" + count);
//        }
//        checkDataValid();
//        mCache.clear();
//        for (int max = count - 1; max >= 0; max--) {
//            if (max >= position) {
//                T item = mData.get(max);
//                if (item != null) {
//                    mData.put(max + 1, item);
//                    mData.remove(max);
//                }
//            }
//        }
//        mData.put(position, data);
//        if (getItemCount() == 1) {
//            notifyDataSetChanged();
//        } else {
//            notifyItemInserted(position);
//        }
//    }
//
//    public void addData(@NonNull Collection<? extends T> data) {
//        checkDataValid();
//        mCache.clear();
//        int count = getItemCount();
//        int i = 0;
//        for (T o : data) {
//            mData.put(count + i, o);
//            i++;
//        }
//        if (getItemCount() == data.size()) {
//            notifyDataSetChanged();
//        } else {
//            notifyItemRangeInserted(getItemCount() - data.size(), data.size());
//        }
//    }
//
//    public void addData(@IntRange(from = 0) int position,
//                        @NonNull Collection<? extends T> data) {
//        int count = getItemCount();
//        if (position > count || position < 0) {
//            throw new IndexOutOfBoundsException("Position out of bounds: position="
//                    + position + ", size=" + count);
//        }
//        checkDataValid();
//        mCache.clear();
//        int size = data.size();
//        for (int max = count - 1; max >= 0; max--) {
//            if (max >= position) {
//                T item = mData.get(max);
//                if (item != null) {
//                    mData.put(max + size, item);
//                    mData.remove(max);
//                }
//            }
//        }
//        int i = 0;
//        for (T o : data) {
//            mData.put(position + i, o);
//            i++;
//        }
//        if (getItemCount() == size) {
//            notifyDataSetChanged();
//        } else {
//            notifyItemRangeInserted(position, size);
//        }
//    }
//
//    private boolean isInCursor(int pos) {
//        return mData.get(pos) == null;
//    }
//
//    private int calculateCursorPosition(int pos) {
//        int offset = 0;
//        for (int i = 0; i < mData.size(); i++) {
//            int key = mData.keyAt(i);
//            if (key <= pos) {
//                offset++;
//            }
//        }
//        return pos - offset;
//    }
//
//    private void checkDataValid() {
//        if (!isDataValid(mCursor)) {
//            throw new IllegalStateException(
//                    "Cannot lookup item id because the cursor is in invalid state.");
//        }
//    }
//
//    private void checkDataValid(int position) {
//        if (!isDataValid(mCursor)) {
//            throw new IllegalStateException(
//                    "Cannot lookup item id because the cursor is in invalid state.");
//        }
//        if (!mCursor.moveToPosition(position)) {
//            throw new IllegalStateException("Could not move cursor to position " + position
//                    + " when trying to get an item id");
//        }
//    }
//
//    private boolean isDataValid(Cursor cursor) {
//        return cursor != null && !cursor.isClosed();
//    }
//}
