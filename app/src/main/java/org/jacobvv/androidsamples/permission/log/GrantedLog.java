package org.jacobvv.androidsamples.permission.log;

import android.support.annotation.NonNull;

import org.jacobvv.androidsamples.R;
import org.jacobvv.baserecycler.BaseViewHolder;
import org.jacobvv.baserecycler.ItemType;

/**
 * @author jacob
 * @date 19-4-28
 */
public class GrantedLog {

    public static class GrantedType extends ItemType<GrantedLog, BaseViewHolder<GrantedLog>> {

        @Override
        public int getLayoutId(int type) {
            return R.layout.permission_item_granted;
        }

        @Override
        public void onBindViewHolder(@NonNull BaseViewHolder<GrantedLog> holder, GrantedLog model, int position) {
        }
    }
}
