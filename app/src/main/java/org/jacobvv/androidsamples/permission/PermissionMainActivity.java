package org.jacobvv.androidsamples.permission;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.jacobvv.androidsamples.R;
import org.jacobvv.androidsamples.databus.ActivityA;
import org.jacobvv.androidsamples.recycler.RecyclerActivity;

import java.util.ArrayList;
import java.util.List;

public class PermissionMainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private List<String> mItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.permission_activity_main);

        mItems.add("DataBus Sample");
        mItems.add("BaseRecyclerView Sample");
        mItems.add("Runtime permission Sample");

        ListView listView = findViewById(R.id.lv_list);
        listView.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, mItems));
        listView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent i = new Intent();
        switch (position) {
            case 0:
                i.setClass(this, ActivityA.class);
                break;
            case 1:
                i.setClass(this, RecyclerActivity.class);
                break;
            case 2:
                i.setClass(this, PermissionMainActivity.class);
                break;
            default:
        }
        startActivity(i);
    }

}
