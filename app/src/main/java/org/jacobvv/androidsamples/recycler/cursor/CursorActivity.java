package org.jacobvv.androidsamples.recycler.cursor;

import android.Manifest;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Toast;

import org.jacobvv.androidsamples.R;
import org.jacobvv.androidsamples.recycler.cursor.model.Camera;
import org.jacobvv.androidsamples.recycler.cursor.model.Image;
import org.jacobvv.androidsamples.recycler.cursor.model.MediaRepository;
import org.jacobvv.androidsamples.recycler.cursor.type.CameraType;
import org.jacobvv.androidsamples.recycler.cursor.type.ImageType;
import org.jacobvv.baserecycler.BaseRecyclerAdapter;
import org.jacobvv.baserecycler.BaseViewHolder;
import org.jacobvv.baserecycler.listener.OnItemClickListener;
import org.jacobvv.baserecycler.listener.OnViewClickListener;
import org.jacobvv.permission.annotation.RequestPermission;

public class CursorActivity extends AppCompatActivity {

    private ImageAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycler_activity_cursor);
        CameraType cameraType = new CameraType();
        cameraType.setOnItemClickListener(new OnItemClickListener<Camera>() {
            @Override
            public void onClick(BaseRecyclerAdapter<Camera> adapter,
                                BaseViewHolder<Camera> holder, Camera model, int position) {
                Toast.makeText(CursorActivity.this, "Camera on click.",
                        Toast.LENGTH_SHORT).show();
            }
        });
        ImageType imageType = new ImageType();
        imageType.addOnViewClickListener(R.id.view_item_region, new OnViewClickListener<Image>() {
            @Override
            public void onClick(BaseRecyclerAdapter<Image> adapter,
                                BaseViewHolder<Image> holder, View v, Image model, int position) {
                ImageView image = holder.getView(R.id.iv_item_image);
                CheckBox box = holder.getView(R.id.cb_item_check);
                box.setChecked(!box.isChecked());
                if (box.isChecked()) {
                    image.setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);
                } else {
                    image.setColorFilter(0xFFEEEEEE, PorterDuff.Mode.MULTIPLY);
                }
            }
        });

        RecyclerView recycler = findViewById(R.id.rv_list);
        adapter = new ImageAdapter();
        adapter.register(Camera.class, cameraType);
        adapter.register(Image.class, imageType);
        recycler.setLayoutManager(new GridLayoutManager(this, 4));
        recycler.addItemDecoration(new ImageItemDecoration());
        recycler.setAdapter(adapter);
        CursorActivity_PermissionHelper.init_WithCheck(this);
    }

    @RequestPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
    void init() {
        new MediaRepository(this).loadMedia(new MediaRepository.MediaCallbacks() {
            @Override
            public void onMediaLoaded(Cursor cursor) {
                adapter.setCursor(cursor);
                adapter.add(0, new Camera());
                findViewById(R.id.progress_bar).setVisibility(View.GONE);
            }

            @Override
            public void onMediaReset() {
            }
        });
    }

    private static class ImageItemDecoration extends RecyclerView.ItemDecoration {
        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            outRect.set(4, 4, 4, 4);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        CursorActivity_PermissionHelper.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }
}
