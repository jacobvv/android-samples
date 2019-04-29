package org.jacobvv.androidsamples.permission.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import org.jacobvv.androidsamples.R;

public class CommonActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.permission_activity_common);
    }
}
