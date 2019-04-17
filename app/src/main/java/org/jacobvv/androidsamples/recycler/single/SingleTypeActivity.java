package org.jacobvv.androidsamples.recycler.single;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import org.jacobvv.androidsamples.R;
import org.jacobvv.androidsamples.recycler.Model;
import org.jacobvv.libsamples.baserecycler.BaseArrayAdapter;
import org.jacobvv.libsamples.baserecycler.BaseRecyclerAdapter;
import org.jacobvv.libsamples.baserecycler.BaseRecyclerViewHolder;
import org.jacobvv.libsamples.baserecycler.ItemType;
import org.jacobvv.libsamples.baserecycler.listener.OnItemClickListener;

public class SingleTypeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_type);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        RecyclerView recycler = findViewById(R.id.rv_list);
        final BaseArrayAdapter<Model> adapter = new BaseArrayAdapter<>();
        adapter.register(new SingleType());
        recycler.setLayoutManager(new LinearLayoutManager(this));
        recycler.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recycler.setAdapter(adapter);
        adapter.setData(Model.buildList(5));

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapter.addData(Model.buildItem());
            }
        });
    }

    private class SingleType implements ItemType<Model> {

        @Override
        public int getLayoutId(int type) {
            return R.layout.item_single;
        }

        @Override
        public void onCreateViewHolder(BaseRecyclerViewHolder<Model> holder) {
            holder.setOnItemClickListener(new OnItemClickListener<Model>() {
                @Override
                public void onClick(BaseRecyclerAdapter<Model> adapter,
                                    BaseRecyclerViewHolder<Model> holder,
                                    Model model, int position) {
                    Snackbar.make(holder.itemView, "On click: " + model.title,
                            Snackbar.LENGTH_SHORT).show();
                }
            });
        }

        @Override
        public void setupView(BaseRecyclerViewHolder<Model> holder, Model model, int position) {
            TextView img = holder.getView(R.id.tv_img);
            TextView title = holder.getView(R.id.tv_title);
            TextView content = holder.getView(R.id.tv_content);
            img.setText(model.img);
            title.setText(model.title);
            content.setText(model.content);
        }
    }

}
