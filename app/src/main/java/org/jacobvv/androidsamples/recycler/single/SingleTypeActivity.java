package org.jacobvv.androidsamples.recycler.single;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.jacobvv.androidsamples.R;
import org.jacobvv.baserecycler.BaseArrayAdapter;
import org.jacobvv.baserecycler.BaseRecyclerAdapter;
import org.jacobvv.baserecycler.BaseViewHolder;
import org.jacobvv.baserecycler.ItemType;
import org.jacobvv.baserecycler.listener.OnItemClickListener;
import org.jacobvv.baserecycler.listener.OnViewClickListener;

public class SingleTypeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycler_activity_single_type);

        SingleType type = new SingleType();
        type.setOnItemClickListener(new OnItemClickListener<SingleModel>() {
            @Override
            public void onClick(BaseRecyclerAdapter<SingleModel> adapter,
                                BaseViewHolder<SingleModel> holder,
                                SingleModel model, int position) {
                Toast.makeText(SingleTypeActivity.this, "On click: " + model.title,
                        Toast.LENGTH_SHORT).show();
            }
        });
        type.addOnViewClickListener(R.id.iv_item_delete, new OnViewClickListener<SingleModel>() {
            @Override
            public void onClick(BaseRecyclerAdapter<SingleModel> adapter,
                                BaseViewHolder<SingleModel> holder,
                                View v, SingleModel model, int position) {
                adapter.remove(position);
            }
        });

        RecyclerView recycler = findViewById(R.id.rv_list);
        final BaseArrayAdapter<SingleModel> adapter = new BaseArrayAdapter<>();
        adapter.register(type);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        recycler.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recycler.setAdapter(adapter);
        adapter.setData(SingleModel.buildList(5));

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapter.add(SingleModel.buildItem());
            }
        });
    }

    private class SingleType extends ItemType<SingleModel, BaseViewHolder<SingleModel>> {

        @Override
        public int getLayoutId(int type) {
            return R.layout.recycler_item_single;
        }

        @Override
        public void onBindViewHolder(@NonNull BaseViewHolder<SingleModel> holder, SingleModel model, int position) {
            ImageView img = holder.getView(R.id.iv_item_image);
            TextView title = holder.getView(R.id.tv_item_title);
            TextView content = holder.getView(R.id.tv_item_title);
            img.setImageResource(model.image);
            title.setText(model.title);
            content.setText(model.content);
        }

    }

}
