package org.jacobvv.androidsamples.recycler.cursor;

import android.content.ContentUris;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import org.jacobvv.androidsamples.recycler.cursor.model.Image;
import org.jacobvv.baserecycler.BaseCursorAdapter;

import java.io.File;

/**
 * @author jacob
 * @date 19-4-20
 */
public class ImageAdapter extends BaseCursorAdapter<Object> {

    @Override
    public Image fromCursor(Cursor current) {
        String name = current.getString(current.getColumnIndexOrThrow(MediaStore.MediaColumns.DISPLAY_NAME));
        long id = current.getLong(current.getColumnIndexOrThrow(MediaStore.Files.FileColumns._ID));
        String path = current.getString(current.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA));
        return new Image(name, path, getFileUri(id, path));
    }

    private Uri getFileUri(long id, String path) {
        if (id == 0) {
            return Uri.fromFile(new File(path));
        } else {
            return ContentUris.withAppendedId(
                    MediaStore.Files.getContentUri("external"), id);
        }
    }
}
