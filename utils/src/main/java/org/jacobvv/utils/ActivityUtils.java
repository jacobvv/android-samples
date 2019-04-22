package org.jacobvv.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;

/**
 * ActivityUtils，
 *
 * @author weihong
 * @date 18-6-7
 */
@SuppressWarnings({"WeakerAccess", "unused", "SameParameterValue", "ObsoleteSdkInt"})
public final class ActivityUtils {

    private static final String TAG = ActivityUtils.class.getSimpleName();

    private ActivityUtils() {
        throw new UnsupportedOperationException("Utils CANNOT be instantiated!");
    }

    // 使用Context开启新活动

    public static void startActivity(Context context, Class<? extends Activity> clazz) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setClass(context, clazz);
        ActivityCompat.startActivity(context, intent, null);
    }

    public static void startActivity(Context context, Class<? extends Activity> clazz,
                                     Bundle extras) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        if (extras != null) {
            intent.putExtras(extras);
        }
        intent.setClass(context, clazz);
        ActivityCompat.startActivity(context, intent, null);
    }

    public static void startActivity(Context context, Class<? extends Activity> clazz,
                                     Bundle extras, Bundle opts) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        if (extras != null) {
            intent.putExtras(extras);
        }
        intent.setClass(context, clazz);
        ActivityCompat.startActivity(context, intent, opts);
    }

    public static void startActivity(Context context, Intent intent) {
        try {
            ActivityCompat.startActivity(context, intent, null);
        } catch (Exception e) {
            Log.e(TAG, "Start activity failed. " + e.getMessage());
        }
    }

    public static void startActivity(Context context, Intent intent, Bundle opts) {
        try {
            ActivityCompat.startActivity(context, intent, opts);
        } catch (Exception e) {
            Log.e(TAG, "Start activity failed. " + e.getMessage());
        }
    }

    // 使用Fragment开启新活动

    public static void startActivity(Fragment fragment, Class<? extends Activity> clazz) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setClass(fragment.getContext(), clazz);
        fragment.startActivity(intent);
    }

    public static void startActivity(Fragment fragment, Class<? extends Activity> clazz,
                                     Bundle extras) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        if (extras != null) {
            intent.putExtras(extras);
        }
        intent.setClass(fragment.getContext(), clazz);
        fragment.startActivity(intent);
    }

    public static void startActivity(Fragment fragment, Class<? extends Activity> clazz,
                                     Bundle extras, Bundle opts) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        if (extras != null) {
            intent.putExtras(extras);
        }
        intent.setClass(fragment.getContext(), clazz);
        fragment.startActivity(intent, opts);
    }

    public static void startActivity(Fragment fragment, Intent intent) {
        try {
            fragment.startActivity(intent);
        } catch (Exception e) {
            Log.e(TAG, "Start activity failed. " + e.getMessage());
        }
    }

    public static void startActivity(Fragment fragment, Intent intent, Bundle opts) {
        try {
            fragment.startActivity(intent, opts);
        } catch (Exception e) {
            Log.e(TAG, "Start activity failed. " + e.getMessage());
        }
    }

    // 使用ContextWrap开启新活动

    public static void startActivity(ContextWrap context, Class<? extends Activity> clazz) {
        if (context.getFragment() != null) {
            startActivity(context.getFragment(), clazz);
        } else {
            startActivity(context.getActivity(), clazz);
        }
    }

    public static void startActivity(ContextWrap context, Class<? extends Activity> clazz,
                                     Bundle extras) {
        if (context.getFragment() != null) {
            startActivity(context.getFragment(), clazz, extras);
        } else {
            startActivity(context.getActivity(), clazz, extras);
        }
    }

    public static void startActivity(ContextWrap context, Class<? extends Activity> clazz,
                                     Bundle extras, Bundle opts) {
        if (context.getFragment() != null) {
            startActivity(context.getFragment(), clazz, extras, opts);
        } else {
            startActivity(context.getActivity(), clazz, extras, opts);
        }
    }

    public static void startActivity(ContextWrap context, Intent intent) {
        try {
            if (context.getFragment() != null) {
                startActivity(context.getFragment(), intent);
            } else {
                startActivity(context.getActivity(), intent);
            }
        } catch (Exception e) {
            Log.e(TAG, "Start activity failed. " + e.getMessage());
        }
    }

    public static void startActivity(ContextWrap context, Intent intent, Bundle opts) {
        try {
            if (context.getFragment() != null) {
                startActivity(context.getFragment(), intent, opts);
            } else {
                startActivity(context.getActivity(), intent, opts);
            }
        } catch (Exception e) {
            Log.e(TAG, "Start activity failed. " + e.getMessage());
        }
    }

    // 使用Activity开启新活动并返回结果

    public static void startActivityForResult(Activity activity, Class<? extends Activity> clazz,
                                              int requestCode) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setClass(activity, clazz);
        ActivityCompat.startActivityForResult(activity, intent, requestCode, null);
    }

    public static void startActivityForResult(Activity activity, Class<? extends Activity> clazz,
                                              int requestCode, Bundle extras) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        if (extras != null) {
            intent.putExtras(extras);
        }
        intent.setClass(activity, clazz);
        ActivityCompat.startActivityForResult(activity, intent, requestCode, null);
    }

    public static void startActivityForResult(Activity activity, Class<? extends Activity> clazz,
                                              int requestCode, Bundle extras, Bundle opts) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        if (extras != null) {
            intent.putExtras(extras);
        }
        intent.setClass(activity, clazz);
        ActivityCompat.startActivityForResult(activity, intent, requestCode, opts);
    }

    public static void startActivityForResult(Activity activity, Intent intent, int requestCode) {
        try {
            ActivityCompat.startActivityForResult(activity, intent, requestCode, null);
        } catch (Exception e) {
            Log.e(TAG, "Start activity failed. " + e.getMessage());
        }
    }

    public static void startActivityForResult(Activity activity, Intent intent, int requestCode,
                                              Bundle opts) {
        try {
            ActivityCompat.startActivityForResult(activity, intent, requestCode, opts);
        } catch (Exception e) {
            Log.e(TAG, "Start activity failed. " + e.getMessage());
        }
    }

    // 使用Fragment开启新活动并返回结果

    public static void startActivityForResult(Fragment fragment, Class<? extends Activity> clazz,
                                              int requestCode) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setClass(fragment.getContext(), clazz);
        fragment.startActivityForResult(intent, requestCode);
    }

    public static void startActivityForResult(Fragment fragment, Class<? extends Activity> clazz,
                                              int requestCode, Bundle extras) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        if (extras != null) {
            intent.putExtras(extras);
        }
        intent.setClass(fragment.getContext(), clazz);
        fragment.startActivityForResult(intent, requestCode);
    }

    public static void startActivityForResult(Fragment fragment, Class<? extends Activity> clazz,
                                              int requestCode, Bundle extras, Bundle opts) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        if (extras != null) {
            intent.putExtras(extras);
        }
        intent.setClass(fragment.getContext(), clazz);
        fragment.startActivityForResult(intent, requestCode, opts);
    }

    public static void startActivityForResult(Fragment fragment, Intent intent, int requestCode) {
        try {
            fragment.startActivityForResult(intent, requestCode);
        } catch (Exception e) {
            Log.e(TAG, "Start activity failed. " + e.getMessage());
        }
    }

    public static void startActivityForResult(Fragment fragment, Intent intent, int requestCode,
                                              Bundle opts) {
        try {
            fragment.startActivityForResult(intent, requestCode, opts);
        } catch (Exception e) {
            Log.e(TAG, "Start activity failed. " + e.getMessage());
        }
    }

    // 使用ContextWrap开启新活动并返回结果

    public static void startActivityForResult(ContextWrap context, Class<? extends Activity> clazz,
                                              int requestCode) {
        if (context.getFragment() != null) {
            startActivityForResult(context.getFragment(), clazz, requestCode);
        } else {
            startActivityForResult(context.getActivity(), clazz, requestCode);
        }
    }

    public static void startActivityForResult(ContextWrap context, Class<? extends Activity> clazz,
                                              int requestCode, Bundle extras) {
        if (context.getFragment() != null) {
            startActivityForResult(context.getFragment(), clazz, requestCode, extras);
        } else {
            startActivityForResult(context.getActivity(), clazz, requestCode, extras);
        }
    }

    public static void startActivityForResult(ContextWrap context, Class<? extends Activity> clazz,
                                              int requestCode, Bundle extras, Bundle opts) {
        if (context.getFragment() != null) {
            startActivityForResult(context.getFragment(), clazz, requestCode, extras, opts);
        } else {
            startActivityForResult(context.getActivity(), clazz, requestCode, extras, opts);
        }
    }

    public static void startActivityForResult(ContextWrap context, Intent intent, int requestCode) {
        try {
            if (context.getFragment() != null) {
                startActivityForResult(context.getFragment(), intent, requestCode);
            } else {
                startActivityForResult(context.getActivity(), intent, requestCode);
            }
        } catch (Exception e) {
            Log.e(TAG, "Start activity failed. " + e.getMessage());
        }
    }

    public static void startActivityForResult(ContextWrap context, Intent intent, int requestCode,
                                              Bundle opts) {
        try {
            if (context.getFragment() != null) {
                startActivityForResult(context.getFragment(), intent, requestCode, opts);
            } else {
                startActivityForResult(context.getActivity(), intent, requestCode, opts);
            }
        } catch (Exception e) {
            Log.e(TAG, "Start activity failed. " + e.getMessage());
        }
    }

}
