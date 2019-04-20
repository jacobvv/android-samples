package org.jacobvv.libsamples.utils;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.Collection;
import java.util.Map;

/**
 * @author yinhui
 * @date 18-6-21
 */
@SuppressWarnings({"unused", "SameParameterValue", "ObsoleteSdkInt"})
public final class CheckUtils {

    private static final String TAG = CheckUtils.class.getSimpleName();

    private CheckUtils() {
        throw new UnsupportedOperationException("Utils CANNOT be instantiated!");
    }

    public static boolean isEmpty(@Nullable CharSequence str) {
        return str == null || str.length() == 0;
    }

    public static boolean isEmpty(@Nullable Collection<?> col) {
        return col == null || col.isEmpty();
    }

    public static boolean isEmpty(@Nullable Map<?, ?> col) {
        return col == null || col.isEmpty();
    }

    public static boolean isEmpty(Object[] array) {
        return array == null || array.length == 0;
    }

    /**
     * Ensures that an object reference passed as a parameter to the calling
     * method is not null.
     *
     * @param reference an object reference
     * @return the non-null reference that was validated
     * @throws NullPointerException if {@code reference} is null
     */
    public static @NonNull
    <T> T checkNotNull(final T reference) {
        if (reference == null) {
            throw new NullPointerException();
        }
        return reference;
    }

}
