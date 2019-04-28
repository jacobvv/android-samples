package org.jacobvv.androidsamples.recycler.multi.type;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.jacobvv.androidsamples.R;
import org.jacobvv.androidsamples.recycler.multi.model.Group;
import org.jacobvv.baserecycler.BaseArrayAdapter;
import org.jacobvv.baserecycler.BaseRecyclerAdapter;
import org.jacobvv.baserecycler.BaseViewHolder;
import org.jacobvv.baserecycler.ItemType;
import org.jacobvv.baserecycler.listener.OnItemClickListener;

import static org.jacobvv.androidsamples.recycler.multi.type.GroupType.GroupViewHolder;

/**
 * @author jacob
 * @date 19-4-18
 */
public class GroupType extends ItemType<Group, GroupViewHolder> {

    @Override
    public int getLayoutId(int type) {
        return R.layout.recycler_item_multi_list;
    }

    @NonNull
    @Override
    public GroupViewHolder onCreateViewHolder(@NonNull View view,
                                              @NonNull ItemType<Group, GroupViewHolder> type) {
        return new GroupViewHolder(view, type);
    }

    @Override
    public void onBindViewHolder(@NonNull GroupViewHolder holder, Group model, int position) {
        holder.setList(model);
    }

    static class GroupViewHolder extends BaseViewHolder<Group> {

        BaseArrayAdapter<Group.Item> mAdapter;

        GroupViewHolder(final View itemView, ItemType<Group, ?> listType) {
            super(itemView, listType);

            final Context context = itemView.getContext();
            GroupItemType type = new GroupItemType();
            type.setOnItemClickListener(new OnItemClickListener<Group.Item>() {

                @Override
                public void onClick(BaseRecyclerAdapter<Group.Item> adapter,
                                    BaseViewHolder<Group.Item> holder,
                                    Group.Item model, int position) {
                    Toast.makeText(context, "List item on click: " + model.title,
                            Toast.LENGTH_SHORT).show();
                }
            });

            RecyclerView recycler = getView(R.id.rv_item_list);
            mAdapter = new BaseArrayAdapter<>();
            mAdapter.register(type);
            recycler.setLayoutManager(new LinearLayoutManager(context,
                    LinearLayoutManager.HORIZONTAL, false));
            recycler.setAdapter(mAdapter);
        }

        private void setList(Group group) {
            mAdapter.setData(group.list);
        }
    }

    private static class GroupItemType extends ItemType<Group.Item, BaseViewHolder<Group.Item>> {

        @Override
        public int getLayoutId(int type) {
            return R.layout.recycler_item_multi_list_item;
        }

        @Override
        public void onBindViewHolder(@NonNull BaseViewHolder<Group.Item> holder, Group.Item model, int position) {
            TextView icon = holder.getView(R.id.tv_item_icon);
            icon.setCompoundDrawablesWithIntrinsicBounds(0, model.image, 0, 0);
            icon.setText(model.title);
        }
    }
}
