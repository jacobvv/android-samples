package org.jacobvv.androidsamples.recycler.multi;

import org.jacobvv.libsamples.baserecycler.BaseRecyclerViewHolder;
import org.jacobvv.libsamples.baserecycler.ItemType;

/**
 * @author jacob
 * @date 19-4-18
 */
public class AbstractType implements ItemType<Abstract> {

    @Override
    public int getLayoutId(int type) {
        return 0;
    }

    @Override
    public void onCreateViewHolder(BaseRecyclerViewHolder<Abstract> holder) {

    }

    @Override
    public void setupView(BaseRecyclerViewHolder<Abstract> holder, Abstract model, int position) {

    }
}
