package org.jacobvv.androidsamples.utils;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;

/**
 * Context 封装类
 *
 * @author yinhui
 * @date 18-6-8
 */
@SuppressWarnings({"WeakerAccess", "unused", "SameParameterValue", "ObsoleteSdkInt"})
public class ContextWrap {
    private Activity activity;
    private Fragment fragment;

    public ContextWrap(Activity activity) {
        this.activity = activity;
    }

    public ContextWrap(Fragment fragment) {
        this.activity = fragment.getActivity();
        this.fragment = fragment;
    }

    public Context getContext() {
        return activity.getApplicationContext();
    }

    public Activity getActivity() {
        return activity;
    }

    public Fragment getFragment() {
        return fragment;
    }
}
