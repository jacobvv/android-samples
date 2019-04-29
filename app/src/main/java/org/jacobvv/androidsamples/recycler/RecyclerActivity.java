package org.jacobvv.androidsamples.recycler;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.jacobvv.androidsamples.R;
import org.jacobvv.androidsamples.recycler.cursor.CursorActivity;
import org.jacobvv.androidsamples.recycler.multi.MultiTypeActivity;
import org.jacobvv.androidsamples.recycler.single.SingleTypeActivity;
import org.jacobvv.androidsamples.util.PermissionUtils;

import java.util.ArrayList;
import java.util.List;

public class RecyclerActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private List<String> mItems = new ArrayList<>();
    private PermissionUtils.PermissionHelper mPermission;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycler_activity_main);

        mItems.add("Single type list.");
        mItems.add("Multi type list.");
        mItems.add("Cursor list.");

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
                i.setClass(this, SingleTypeActivity.class);
                break;
            case 1:
                i.setClass(this, MultiTypeActivity.class);
                break;
            case 2:
                mPermission = PermissionUtils.of(this,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                        .setCallback(new PermissionUtils.PermissionCallback() {
                            @Override
                            public void onResult(List<String> granted, List<String> denied,
                                                 List<String> deniedForever) {
                                if (denied.isEmpty() && deniedForever.isEmpty()) {
                                    Intent i = new Intent(RecyclerActivity.this,
                                            CursorActivity.class);
                                    startActivity(i);
                                }
                            }
                        })
                        .request(1000);
                return;
            default:
        }
        startActivity(i);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mPermission.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
