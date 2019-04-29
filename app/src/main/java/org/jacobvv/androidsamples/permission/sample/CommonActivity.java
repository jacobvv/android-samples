package org.jacobvv.androidsamples.permission.sample;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

public class CommonActivity extends AppCompatActivity {

    private BaseArrayAdapter<Object> adapter;
    private RecyclerView recycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.permission_activity_common);

        findViewById(R.id.btn_take_photo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonActivity_PermissionHelper.takePhoto_WithCheck(CommonActivity.this);
            }
        });
        findViewById(R.id.btn_get_location).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonActivity_PermissionHelper.getLocation_WithCheck(CommonActivity.this);
            }
        });

        recycler = findViewById(R.id.rv_log);
        adapter = LogAdapterFactory.create();
        recycler.setLayoutManager(new LinearLayoutManager(this));
        recycler.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recycler.setAdapter(adapter);
    }

    @RequestPermission({Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void takePhoto() {
        adapter.add(0, new GrantedLog("Take Photo"));
        recycler.scrollToPosition(0);
    }

    @RequestPermission({Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION})
    void getLocation() {
        adapter.add(0, new GrantedLog("Get Location"));
        recycler.scrollToPosition(0);
    }

    @OnShowRationale()
    void onTakePhotoRationale(PermissionRequest<CommonActivity> request, List<String> denied) {
        PermissionSampleUtils.getRationaleDialog(this, request, denied, null)
                .show();
    }

    @OnPermissionDenied()
    void onTakePhotoDenied(List<String> denied, List<String> deniedForever) {
        adapter.add(0, new DeniedLog(denied, deniedForever));
        recycler.scrollToPosition(0);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        CommonActivity_PermissionHelper.onRequestPermissionsResult(this, requestCode,
                permissions, grantResults);
    }
}
