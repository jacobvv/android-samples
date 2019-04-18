package org.jacobvv.androidsamples.recycler.multi;

import android.support.annotation.NonNull;

import org.jacobvv.libsamples.baserecycler.BaseViewHolder;
import org.jacobvv.libsamples.baserecycler.ItemType;

/**
 * @author jacob
 * @date 19-4-18
 */
public class BannerType extends ItemType<Banner> {

    @Override
    public int getLayoutId(int type) {
        return 0;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder<Banner> holder, Banner model, int position) {

    }
}
