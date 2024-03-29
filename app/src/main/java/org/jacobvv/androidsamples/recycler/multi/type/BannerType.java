package org.jacobvv.androidsamples.recycler.multi.type;

import androidx.annotation.NonNull;
import android.widget.ImageView;

import org.jacobvv.androidsamples.R;
import org.jacobvv.androidsamples.recycler.multi.model.Banner;
import org.jacobvv.baserecycler.BaseViewHolder;
import org.jacobvv.baserecycler.ItemType;

/**
 * @author jacob
 * @date 19-4-18
 */
public class BannerType extends ItemType<Banner, BaseViewHolder<Banner>> {

    @Override
    public int getLayoutId(int type) {
        return R.layout.recycler_item_multi_banner;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder<Banner> holder, Banner model, int position) {
        ImageView img = holder.getView(R.id.iv_item_banner);
        img.setImageResource(model.banner);
    }
}
