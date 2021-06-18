package org.jacobvv.androidsamples.permission.sample;

import android.Manifest;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;

import org.jacobvv.androidsamples.R;
import org.jacobvv.androidsamples.permission.log.DeniedLog;
import org.jacobvv.androidsamples.permission.log.GrantedLog;
import org.jacobvv.androidsamples.permission.log.LogAdapterFactory;
import org.jacobvv.androidsamples.permission.util.PermissionSampleUtils;
import org.jacobvv.baserecycler.BaseArrayAdapter;
import org.jacobvv.permission.annotation.OnPermissionDenied;
import org.jacobvv.permission.annotation.OnShowRationale;
import org.jacobvv.permission.annotation.PermissionRequest;
import org.jacobvv.permission.annotation.RequestPermission;

import java.util.List;

public class MultiActivity extends AppCompatActivity {

    private BaseArrayAdapter<Object> adapter;
    private RecyclerView recycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.permission_activity_multi);

        findViewById(R.id.btn_take_photo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MultiActivity_PermissionHelper.takePhoto_WithCheck(MultiActivity.this);
            }
        });
        findViewById(R.id.btn_get_location).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MultiActivity_PermissionHelper.getLocation_WithCheck(MultiActivity.this);
            }
        });

        recycler = findViewById(R.id.rv_log);
        adapter = LogAdapterFactory.create();
        recycler.setLayoutManager(new LinearLayoutManager(this));
        recycler.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recycler.setAdapter(adapter);
    }

    @RequestPermission(
            value = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
            requestCode = 1
    )
    void takePhoto() {
        adapter.add(0, new GrantedLog("Take Photo"));
        recycler.scrollToPosition(0);
    }

    @OnShowRationale(1)
    void onTakePhotoRationale(PermissionRequest<MultiActivity> request, List<String> denied) {
        PermissionSampleUtils.getRationaleDialog(this, request, denied, "Take Photo")
                .show();
    }

    @OnPermissionDenied(1)
    void onTakePhotoDenied(List<String> denied, List<String> deniedForever) {
        adapter.add(0, new DeniedLog("Take Photo", denied, deniedForever));
        recycler.scrollToPosition(0);
    }

    @RequestPermission(
            value = {
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
            },
            requestCode = 2
    )
    void getLocation() {
        adapter.add(0, new GrantedLog("Get Location"));
        recycler.scrollToPosition(0);
    }

    @OnShowRationale(2)
    void onGetLocationRationale(PermissionRequest<MultiActivity> request, List<String> denied) {
        PermissionSampleUtils.getRationaleDialog(this, request, denied, "Get Location")
                .show();
    }

    @OnPermissionDenied(2)
    void onGetLocationDenied(List<String> denied, List<String> deniedForever) {
        adapter.add(0, new DeniedLog("Get Location", denied, deniedForever));
        recycler.scrollToPosition(0);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        MultiActivity_PermissionHelper.onRequestPermissionsResult(this, requestCode,
                permissions, grantResults);
    }
}
