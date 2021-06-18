package org.jacobvv.androidsamples.recycler.cursor.model;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

/**
 * @author Jacob
 * @date 19-4-19
 */
public class MediaRepository {

    private static final int LOADER_ID_MEDIA = 1000;

    private Context mContext;
    private LoaderManager mLoaderManager;

    public MediaRepository(FragmentActivity activity) {
        mContext = activity.getApplicationContext();
        mLoaderManager = activity.getSupportLoaderManager();
    }

    public void loadMedia(@NonNull MediaCallbacks callbacks) {
        Bundle args = new Bundle();
        if (mLoaderManager.getLoader(LOADER_ID_MEDIA) != null) {
            mLoaderManager.restartLoader(LOADER_ID_MEDIA, args, new MediaLoaderCallbacks(callbacks));
        } else {
            mLoaderManager.initLoader(LOADER_ID_MEDIA, args, new MediaLoaderCallbacks(callbacks));
        }
    }

    public interface MediaCallbacks {
        /**
         * Called when media load complete and return media item data.
         *
         * @param cursor media items cursor of given bucket id load from db.
         */
        void onMediaLoaded(Cursor cursor);

        /**
         * Called when a previously data loaded is being reset, and thus
         * making its data unavailable.  The application should at this point
         * remove any references it has to the Loader's data.
         */
        void onMediaReset();
    }

    private class MediaLoaderCallbacks implements LoaderManager.LoaderCallbacks<Cursor> {

        private MediaCallbacks mCallbacks;

        private MediaLoaderCallbacks(MediaCallbacks callbacks) {
            mCallbacks = callbacks;
        }

        @NonNull
        @Override
        public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
            return MediaLoader.newInstance(mContext);
        }

        @Override
        public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
            mCallbacks.onMediaLoaded(data);
        }

        @Override
        public void onLoaderReset(@NonNull Loader<Cursor> loader) {
            mCallbacks.onMediaReset();
        }
    }

}
