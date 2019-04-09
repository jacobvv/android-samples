package org.jacobvv.androidsamples.databus;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.jacobvv.androidsamples.R;
import org.jacobvv.libsamples.databus.DataBus;
import org.jacobvv.libsamples.databus.Observer;

import java.util.LinkedList;
import java.util.List;

public class ActivityB extends AppCompatActivity {

    private static final String TAG = "DataBusSample";
    private List<String> mEvents = new LinkedList<>();
    private int index = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.databus_activity_b);

        final ListView listView = findViewById(R.id.lv_list);
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, mEvents);
        listView.setAdapter(adapter);

        DataBus.with("tag_b", String.class)
                .observeSticky(this, new Observer<String>() {
                    @Override
                    public void onChanged(@Nullable String s) {
                        String msg = DateFormat.format("HH:mm:ss",
                                System.currentTimeMillis()) + ": Receive tag-B msg: " + s;
                        Log.d(TAG, msg);
                        mEvents.add(0, msg);
                        adapter.notifyDataSetChanged();
                    }
                });

        findViewById(R.id.btn_post_a).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataBus.with("tag_a").post("From Activity_B No." + index++);
            }
        });

        findViewById(R.id.btn_post_b).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataBus.with("tag_b").post("From Activity_B No." + index++);
            }
        });

        findViewById(R.id.btn_finish).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
