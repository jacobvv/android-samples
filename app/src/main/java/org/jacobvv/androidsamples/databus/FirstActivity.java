package org.jacobvv.androidsamples.databus;

import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import org.jacobvv.androidsamples.R;
import org.jacobvv.libsamples.databus.DataBus;
import org.jacobvv.libsamples.databus.Observer;

/**
 * @author jacob
 */
public class FirstActivity extends AppCompatActivity {

    private static final String TAG = "Demo";
    private long mTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        DataBus.with("key_name", String.class)
                .observe(this, new Observer<String>() {
                    @Override
                    public void onChanged(@Nullable String s) {
                        Log.d(TAG, "String:" + s + "; Time=" + System.currentTimeMillis());
                    }
                });
        DataBus.with("key_name", Integer.class)
                .observe(this, new Observer<Integer>() {
                    @Override
                    public void onChanged(@Nullable Integer s) {
                        Log.d(TAG, "Integer:" + s + "; Time=" + (System.currentTimeMillis() - mTime));
                    }
                });

        View btn = findViewById(R.id.btn_test);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTime = System.currentTimeMillis();
                Log.d(TAG, "Time=" + mTime);
                DataBus.with("key_name").post("hahaha");
            }
        });
        btn.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mTime = System.currentTimeMillis();
                Log.d(TAG, "Time=" + mTime);
                DataBus.with("key_name").post(123);
                return true;
            }
        });

    }
}
