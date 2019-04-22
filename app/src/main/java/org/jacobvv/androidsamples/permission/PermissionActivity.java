package org.jacobvv.androidsamples.permission;

import android.Manifest;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import org.jacobvv.androidsamples.R;
import org.jacobvv.permission.annotaion.NeedPermission;

public class PermissionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission);
    }

    @NeedPermission(Manifest.permission.CAMERA)
    public void requestPermission() {
        // TODO: Sample for request permission.
    }
}
