package org.jacobvv.androidsamples.recycler.multi.type;

import androidx.annotation.NonNull;
import android.widget.ImageView;
import android.widget.TextView;

import org.jacobvv.androidsamples.R;
import org.jacobvv.androidsamples.recycler.multi.model.Abstract;
import org.jacobvv.baserecycler.BaseViewHolder;
import org.jacobvv.baserecycler.ItemType;

/**
 * @author jacob
 * @date 19-4-18
 */
public class AbstractType extends ItemType<Abstract, BaseViewHolder<Abstract>> {

    @Override
    public int getLayoutId(int type) {
        return R.layout.recycler_item_multi_abstract;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder<Abstract> holder,
                                 Abstract model, int position) {
        ImageView img = holder.getView(R.id.iv_item_image);
        TextView title = holder.getView(R.id.tv_item_title);
        TextView content = holder.getView(R.id.tv_item_title);
        img.setImageResource(model.image);
        title.setText(model.title);
        content.setText(model.content);
    }
}
