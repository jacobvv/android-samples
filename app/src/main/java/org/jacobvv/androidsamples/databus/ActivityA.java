package org.jacobvv.androidsamples.databus;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.jacobvv.androidsamples.Constants;
import org.jacobvv.androidsamples.R;
import org.jacobvv.databus.DataBus;
import org.jacobvv.databus.Observer;

import java.util.LinkedList;
import java.util.List;

/**
 * @author jacob
 */
public class ActivityA extends AppCompatActivity {

    private List<String> mEvents = new LinkedList<>();
    private int index = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.databus_activity_a);

        final ListView listView = findViewById(R.id.lv_list);
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, mEvents);
        listView.setAdapter(adapter);

        DataBus.with("tag_a", String.class)
                .observe(this, new Observer<String>() {
                    @Override
                    public void onChanged(@Nullable String s) {
                        String msg = DateFormat.format("HH:mm:ss",
                                System.currentTimeMillis()) + ": Receive tag-A msg: " + s;
                        Log.d(Constants.TAG, msg);
                        mEvents.add(0, msg);
                        adapter.notifyDataSetChanged();
                    }
                });

        findViewById(R.id.btn_post_a).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataBus.with("tag_a").post("From Activity_A No." + index++);
            }
        });

        findViewById(R.id.btn_post_b).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataBus.with("tag_b").post("From Activity_A No." + index++);
            }
        });

        findViewById(R.id.btn_go_b).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ActivityA.this, ActivityB.class));
            }
        });

    }
}
