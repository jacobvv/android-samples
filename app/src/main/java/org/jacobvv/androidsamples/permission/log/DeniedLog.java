package org.jacobvv.androidsamples.permission.log;

import android.support.annotation.NonNull;
import android.widget.TextView;

import org.jacobvv.androidsamples.R;
import org.jacobvv.baserecycler.BaseViewHolder;
import org.jacobvv.baserecycler.ItemType;

import java.util.List;

/**
 * @author jacob
 * @date 19-4-28
 */
public class DeniedLog {
    List<String> denied;
    List<String> deniedForever;

    public DeniedLog(List<String> denied, List<String> deniedForever) {
        this.denied = denied;
        this.deniedForever = deniedForever;
    }

    public static class DeniedType extends ItemType<DeniedLog, BaseViewHolder<DeniedLog>> {

        @Override
        public int getLayoutId(int type) {
            return R.layout.permission_item_denied;
        }

        @Override
        public void onBindViewHolder(@NonNull BaseViewHolder<DeniedLog> holder, DeniedLog model, int position) {
            TextView denied = holder.getView(R.id.tv_item_denied);
            TextView deniedForever = holder.getView(R.id.tv_item_denied_forever);
            StringBuilder builder = new StringBuilder();
            boolean isNotFirst = false;
            for (String s : model.denied) {
                if (isNotFirst) {
                    builder.append("\n");
                }
                builder.append(s);
                isNotFirst = true;
            }
            if (builder.length() > 0) {
                denied.setText(builder.toString());
            }
            builder = new StringBuilder();
            isNotFirst = false;
            for (String s : model.deniedForever) {
                if (isNotFirst) {
                    builder.append("\n");
                }
                builder.append(s);
                isNotFirst = true;
            }
            if (builder.length() > 0) {
                deniedForever.setText(builder.toString());
            }
        }
    }
}
