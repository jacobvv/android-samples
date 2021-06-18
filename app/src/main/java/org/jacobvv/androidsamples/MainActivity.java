package org.jacobvv.androidsamples;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import org.jacobvv.androidsamples.databus.ActivityA;
import org.jacobvv.androidsamples.permission.PermissionMainActivity;
import org.jacobvv.androidsamples.recycler.RecyclerActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private List<String> mItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
