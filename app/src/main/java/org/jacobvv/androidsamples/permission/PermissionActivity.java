package org.jacobvv.androidsamples.permission;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import org.jacobvv.androidsamples.R;
import org.jacobvv.permission.annotaion.NeedPermission;

public class PermissionActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission);

        findViewById(R.id.btn_take_photo).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_take_photo:
                PermissionActivity_PermissionHelper.takePhoto_WithPermissionCheck(this);
                break;
            default:
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionActivity_PermissionHelper.onRequestPermissionsResult(this, requestCode,
                permissions, grantResults);
    }

    @NeedPermission(Manifest.permission.CAMERA)
    public void takePhoto() {
        // TODO: Sample for request permission.
    }

}
