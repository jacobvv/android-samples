package org.jacobvv.androidsamples.recycler.multi;

import org.jacobvv.libsamples.baserecycler.BaseRecyclerViewHolder;
import org.jacobvv.libsamples.baserecycler.ItemType;

/**
 * @author jacob
 * @date 19-4-18
 */
public class GroupType implements ItemType<Group> {

    @Override
    public int getLayoutId(int type) {
        return 0;
    }

    @Override
    public void onCreateViewHolder(BaseRecyclerViewHolder<Group> holder) {

    }

    @Override
    public void setupView(BaseRecyclerViewHolder<Group> holder, Group model, int position) {

    }
}
