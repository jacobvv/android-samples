package org.jacobvv.libsamples.baserecycler;

/**
 * @author jacob
 * @date 19-4-13
 */
public interface ItemType<T> {
    //
//    private OnItemClickListener<T> mItemClickListener;
//    private OnItemLongClickListener<T> mItemLongClickListener;
//    private SparseArray<OnViewClickListener<T>> mViewClickListeners = new SparseArray<>();
//    private SparseArray<OnViewLongClickListener<T>> mViewLongClickListeners = new SparseArray<>();
//
//    public void setOnItemClickListener(OnItemClickListener<T> l) {
//        this.mItemClickListener = l;
//    }
//
//    public void setOnItemLongClickListener(OnItemLongClickListener<T> l) {
//        this.mItemLongClickListener = l;
//    }
//
//    public void addOnViewClickListener(@IdRes int id, OnViewClickListener<T> l) {
//        if (l != null) {
//            this.mViewClickListeners.put(id, l);
//        }
//    }
//
//    public void addOnViewLongClickListener(@IdRes int id, OnViewLongClickListener<T> l) {
//        if (l != null) {
//            this.mViewLongClickListeners.put(id, l);
//        }
//    }
//
    int getLayoutId(int type);

    void onCreateViewHolder(BaseRecyclerViewHolder<T> holder);

    void setupView(BaseRecyclerViewHolder<T> holder, T model, int position);
}
