package org.jacobvv.utils;

import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.TypedValue;

/**
 * @author yinhui
 * @date 18-6-4
 */
@SuppressWarnings({"unused", "SameParameterValue", "ObsoleteSdkInt"})
public final class UnitUtils {

    private static final String TAG = UnitUtils.class.getSimpleName();

    private UnitUtils() {
        throw new UnsupportedOperationException("Utils CANNOT be instantiated!");
    }

    public static float dip2px(float dipValue) {
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dipValue, metrics);
    }

    public static float px2dip(float pxValue) {
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        return pxValue / metrics.density;
    }

    public static float sp2px(float spValue) {
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, spValue, metrics);
    }

    public static float px2sp(float pxValue) {
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        return pxValue / metrics.scaledDensity;
    }

    public static float kg2lb(float kgValue) {
        return kgValue * 2.2046226f;
    }

    public static float lb2kg(float lbValue) {
        return lbValue * 0.4535924f;
    }


}
