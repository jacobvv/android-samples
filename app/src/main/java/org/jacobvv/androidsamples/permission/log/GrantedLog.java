package org.jacobvv.androidsamples.permission.log;

import androidx.annotation.NonNull;
import android.widget.TextView;

import org.jacobvv.androidsamples.R;
import org.jacobvv.baserecycler.BaseViewHolder;
import org.jacobvv.baserecycler.ItemType;

/**
 * @author jacob
 * @date 19-4-28
 */
public class GrantedLog {
    String name;

    public GrantedLog() {
    }

    public GrantedLog(String name) {
        this.name = name;
    }

    public static class GrantedType extends ItemType<GrantedLog, BaseViewHolder<GrantedLog>> {

        @Override
        public int getLayoutId(int type) {
            return R.layout.permission_item_granted;
        }

        @Override
        public void onBindViewHolder(@NonNull BaseViewHolder<GrantedLog> holder, GrantedLog model, int position) {
            if (model.name != null && !model.name.isEmpty()) {
                String title = model.name + ": Granted.";
                TextView titleView = holder.getView(R.id.tv_item_title);
                titleView.setText(title);
            }
        }
    }
}
