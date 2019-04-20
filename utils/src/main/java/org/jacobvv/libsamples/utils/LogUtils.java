package org.jacobvv.libsamples.utils;

import android.util.Log;

/**
 * Log打印工具类
 *
 * @author yinhui
 * @date 18-6-1
 */
@SuppressWarnings({"WeakerAccess", "unused", "SameParameterValue", "ObsoleteSdkInt"})
public final class LogUtils {

    public static final int V = Log.VERBOSE;
    public static final int D = Log.DEBUG;
    public static final int I = Log.INFO;
    public static final int W = Log.WARN;
    public static final int E = Log.ERROR;

    private static int LEVEL = I;

    private LogUtils() {
        throw new UnsupportedOperationException("Utils CANNOT be instantiated!");
    }

    /**
     * Debug情况下在Application中设置level，level可选值为{@link #V}, {@link #D}, {@link #I}, {@link #W}, {@link #E}
     * 不设置默认为{@link #I}
     *
     * @param level log级别
     */
    public static void initLevel(int level) {
        LEVEL = level;
    }

    public static void v(String tag, final Object... contents) {
        if (LEVEL >= V || contents == null) {
            return;
        }
        Log.v(tag, buildString(contents));
    }

    public static void d(String tag, final Object... contents) {
        if (LEVEL >= D || contents == null) {
            return;
        }
        Log.d(tag, buildString(contents));
    }

    public static void i(String tag, final Object... contents) {
        if (LEVEL >= I || contents == null) {
            return;
        }
        Log.i(tag, buildString(contents));
    }

    public static void w(String tag, final Object... contents) {
        if (LEVEL >= W || contents == null) {
            return;
        }
        Log.w(tag, buildString(contents));
    }

    public static void e(String tag, final Object... contents) {
        if (LEVEL >= E || contents == null) {
            return;
        }
        Log.e(tag, buildString(contents));
    }

    private static String buildString(Object... contents) {
        if (contents == null || contents.length == 0) {
            return "null";
        }
        if (contents.length == 1) {
            return String.valueOf(contents[0]);
        }
        StringBuilder sb = new StringBuilder();
        boolean isFirst = true;
        sb.append("[");
        for (Object o : contents) {
            if (!isFirst) {
                sb.append(", ");
            }
            sb.append(o);
            isFirst = false;
        }
        return sb.append("]").toString();
    }

}
