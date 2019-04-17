package org.jacobvv.androidsamples.recycler.multi;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import org.jacobvv.androidsamples.R;
import org.jacobvv.libsamples.baserecycler.BaseArrayAdapter;

public class MultiTypeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_type);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        RecyclerView recycler = findViewById(R.id.rv_list);
        final BaseArrayAdapter<Object> adapter = new BaseArrayAdapter<>();
        adapter.register(Abstract.class, new AbstractType());
        adapter.register(Banner.class, new BannerType());
        adapter.register(Group.class, new GroupType());
        recycler.setLayoutManager(new LinearLayoutManager(this));
        recycler.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recycler.setAdapter(adapter);
//        adapter.setData();

    }

}
