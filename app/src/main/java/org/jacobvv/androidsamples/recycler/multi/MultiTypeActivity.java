package org.jacobvv.androidsamples.recycler.multi;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import org.jacobvv.androidsamples.R;
import org.jacobvv.androidsamples.recycler.multi.model.Abstract;
import org.jacobvv.androidsamples.recycler.multi.model.Banner;
import org.jacobvv.androidsamples.recycler.multi.model.Group;
import org.jacobvv.androidsamples.recycler.multi.type.AbstractType;
import org.jacobvv.androidsamples.recycler.multi.type.BannerType;
import org.jacobvv.androidsamples.recycler.multi.type.GroupType;
import org.jacobvv.baserecycler.BaseArrayAdapter;
import org.jacobvv.baserecycler.BaseRecyclerAdapter;
import org.jacobvv.baserecycler.BaseViewHolder;
import org.jacobvv.baserecycler.listener.OnItemLongClickListener;
import org.jacobvv.baserecycler.listener.OnViewClickListener;

import java.util.ArrayList;
import java.util.List;

public class MultiTypeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycler_activity_multi_type);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        AbstractType abstractType = new AbstractType();
        BannerType bannerType = new BannerType();

        abstractType.addOnViewClickListener(R.id.iv_item_image, new OnViewClickListener<Abstract>() {
            @Override
            public void onClick(BaseRecyclerAdapter<Abstract> adapter,
                                BaseViewHolder<Abstract> holder, View v,
                                Abstract model, int position) {
                Toast.makeText(MultiTypeActivity.this,
                        "Image on click: " + model.title, Toast.LENGTH_SHORT).show();
            }
        });
        abstractType.addOnViewClickListener(R.id.iv_item_delete, new OnViewClickListener<Abstract>() {
            @Override
            public void onClick(BaseRecyclerAdapter<Abstract> adapter,
                                BaseViewHolder<Abstract> holder,
                                View v, Abstract model, int position) {
                adapter.remove(position);
            }
        });
        bannerType.setOnItemLongClickListener(new OnItemLongClickListener<Banner>() {
            @Override
            public boolean onLongClick(BaseRecyclerAdapter<Banner> adapter,
                                       BaseViewHolder<Banner> holder, Banner model, int position) {
                Toast.makeText(MultiTypeActivity.this,
                        "Banner on long click position: " + position, Toast.LENGTH_SHORT)
                        .show();
                return true;
            }
        });

        RecyclerView recycler = findViewById(R.id.rv_list);
        final BaseArrayAdapter<Object> adapter = new BaseArrayAdapter<>();
        adapter.register(Abstract.class, abstractType);
        adapter.register(Banner.class, bannerType);
        adapter.register(Group.class, new GroupType());
        adapter.setData(buildList());
        recycler.setLayoutManager(new LinearLayoutManager(this));
        recycler.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recycler.setAdapter(adapter);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapter.add(buildItem());
            }
        });
    }

    private List<Object> buildList() {
        List<Object> list = new ArrayList<>();
        list.add(Banner.buildItem());
        for (int i = 0; i < 5; i++) {
            list.addAll(Abstract.buildList((int) (Math.random() * 5)));
            if (Math.random() - 0.5 > 0) {
                list.add(Banner.buildItem());
            } else {
                list.add(Group.buildItem((int) (4 + Math.random() * 10)));
            }
        }
        return list;
    }

    private Object buildItem() {
        int type = (int) (Math.random() * 10000) % 3;
        switch (type) {
            case 0:
                return Banner.buildItem();
            case 1:
                return Abstract.buildItem();
            default:
                return Group.buildItem((int) (4 + Math.random() * 10));
        }
    }

}
