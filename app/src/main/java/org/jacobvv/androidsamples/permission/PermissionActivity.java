package org.jacobvv.androidsamples.permission;

import android.Manifest;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import org.jacobvv.androidsamples.R;
import org.jacobvv.permission.annotation.OnPermissionDenied;
import org.jacobvv.permission.annotation.OnShowRationale;
import org.jacobvv.permission.annotation.PermissionRequest;
import org.jacobvv.permission.annotation.RequiresPermission;

import java.util.List;

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
                PermissionActivity_PermissionHelper.takePhoto_WithCheck(this);
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

    @RequiresPermission(value = Manifest.permission.CAMERA)
    public void takePhoto() {
        Toast.makeText(this, "Request permissions granted.",
                Toast.LENGTH_SHORT).show();
    }

    @OnShowRationale()
    public void takePhotoRationale(final PermissionRequest<PermissionActivity> request, List<String> permissions) {
        showRationaleDialog(request, permissions);
    }

    @OnPermissionDenied()
    public void takePhotoDenied(List<String> denied, List<String> deniedForever) {
        Toast.makeText(this, "Request permissions denied.",
                Toast.LENGTH_SHORT).show();
    }

    private void showRationaleDialog(final PermissionRequest<PermissionActivity> request, List<String> permissions) {
        StringBuilder msgBuilder = new StringBuilder(getString(R.string.permission_rationale_msg));
        for (String permission : permissions) {
            msgBuilder.append("\n").append(permission);
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(msgBuilder.toString())
                .setPositiveButton(R.string.permission_rationale_proceed, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        request.proceed(PermissionActivity.this);
                    }
                })
                .setNegativeButton(R.string.permission_rationale_cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        request.cancel(PermissionActivity.this);
                    }
                });
        builder.create().show();
    }

}
