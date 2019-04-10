package org.jacobvv.libsamples.baserecycler;

import android.database.Cursor;

/**
 * @author Jacob
 * @since 18-4-3
 */

public interface CommonCursorModelFactory<T> {

    /**
     * Create item model by cursor.
     *
     * @param cursor cursor which provide data.
     * @return item model from cursor.
     */
    T fromCursor(Cursor cursor);
}
