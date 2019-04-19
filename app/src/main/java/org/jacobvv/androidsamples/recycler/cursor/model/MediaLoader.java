package org.jacobvv.androidsamples.recycler.cursor.model;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.support.v4.content.CursorLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jacob
 * @date 19-4-19
 */
public class MediaLoader extends CursorLoader {

    private static final String[] PROJECTION = {
            MediaStore.Files.FileColumns._ID,
            MediaStore.MediaColumns.DISPLAY_NAME,
            MediaStore.MediaColumns.DATA
    };

    private static final String SELECTION_TYPE = MediaStore.Files.FileColumns.MEDIA_TYPE + "=?";
    private static final String SELECTION_SIZE = MediaStore.MediaColumns.SIZE + ">? OR " +
            MediaStore.MediaColumns.SIZE + " IS NULL";
    private static final String ORDER = "datetaken DESC";

    private MediaLoader(Context context, String selection, String[] selectionArgs) {
        super(context, MediaStore.Files.getContentUri("external"), PROJECTION,
                selection, selectionArgs, ORDER);
    }

    static CursorLoader newInstance(Context context) {
        String selection = SELECTION_TYPE + " AND (" + SELECTION_SIZE + ")";
        String[] selectionArgs = new String[]{};
        List<String> args = new ArrayList<>(1);
        args.add(String.valueOf(MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE));
        args.add(String.valueOf(1000));
        return new MediaLoader(context, selection, args.toArray(selectionArgs));
    }

    @Override
    public Cursor loadInBackground() {
        return super.loadInBackground();
    }
}
