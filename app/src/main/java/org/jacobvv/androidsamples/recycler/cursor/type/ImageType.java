package org.jacobvv.androidsamples.recycler.cursor.type;

import androidx.annotation.NonNull;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import org.jacobvv.androidsamples.R;
import org.jacobvv.androidsamples.recycler.cursor.model.Image;
import org.jacobvv.baserecycler.BaseViewHolder;
import org.jacobvv.baserecycler.ItemType;

/**
 * @author jacob
 * @date 19-4-20
 */
public class ImageType extends ItemType<Image, BaseViewHolder<Image>> {

    @Override
    public int getLayoutId(int type) {
        return R.layout.recycler_item_cursor_image_check;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder<Image> holder, Image model, int position) {
        ImageView image = holder.getView(R.id.iv_item_image);
        Glide.with(holder.itemView.getContext())
                .load(model.uri)
                .into(image);
    }
}
