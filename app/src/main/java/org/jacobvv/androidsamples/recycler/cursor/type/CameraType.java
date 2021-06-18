package org.jacobvv.androidsamples.recycler.cursor.type;

import androidx.annotation.NonNull;

import org.jacobvv.androidsamples.R;
import org.jacobvv.androidsamples.recycler.cursor.model.Camera;
import org.jacobvv.baserecycler.BaseViewHolder;
import org.jacobvv.baserecycler.ItemType;

/**
 * @author jacob
 * @date 19-4-20
 */
public class CameraType extends ItemType<Camera, BaseViewHolder<Camera>> {

    @Override
    public int getLayoutId(int type) {
        return R.layout.recycler_item_cursor_image;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder<Camera> holder,
                                 Camera model, int position) {
    }
}
