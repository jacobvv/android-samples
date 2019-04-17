package org.jacobvv.androidsamples.recycler.multi;

import org.jacobvv.libsamples.baserecycler.BaseRecyclerViewHolder;
import org.jacobvv.libsamples.baserecycler.ItemType;

/**
 * @author jacob
 * @date 19-4-18
 */
public class BannerType implements ItemType<Banner> {

    @Override
    public int getLayoutId(int type) {
        return 0;
    }

    @Override
    public void onCreateViewHolder(BaseRecyclerViewHolder<Banner> holder) {

    }

    @Override
    public void setupView(BaseRecyclerViewHolder<Banner> holder, Banner model, int position) {

    }
}
