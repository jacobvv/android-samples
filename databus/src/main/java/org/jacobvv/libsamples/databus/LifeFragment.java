package org.jacobvv.libsamples.databus;


import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;

/**
 * @author jacob
 * @date 19-4-5
 */
public class LifeFragment extends Fragment {

    private static final String REPORT_FRAGMENT_TAG = "org.jacobvv.libsamples.databus";

    private Lifecycle mCallback;
    private int mActivityHash;

    /**
     * 通过添加无UI的Fragment的方式，对Activity绑定生命周期回调
     *
     * @param activity 需要绑定生命周期的Activity
     * @param callback 生命周期回调
     */
    static void inject(Activity activity, Lifecycle callback) {
        FragmentManager manager = activity.getFragmentManager();
        LifeFragment fragment = (LifeFragment) manager.findFragmentByTag(REPORT_FRAGMENT_TAG);
        if (fragment == null) {
            fragment = new LifeFragment();
            manager.beginTransaction().add(fragment, REPORT_FRAGMENT_TAG).commitAllowingStateLoss();
            manager.executePendingTransactions();
        }
        fragment.setLifecycleCallback(callback);
        fragment.setActivityHash(activity.hashCode());
    }

    private void setLifecycleCallback(Lifecycle callback) {
        this.mCallback = callback;
    }

    public void setActivityHash(int hash) {
        this.mActivityHash = hash;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (mCallback != null) {
            mCallback.onCreate(mActivityHash);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (mCallback != null) {
            mCallback.onCreate(mActivityHash);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mCallback != null) {
            mCallback.onStart(mActivityHash);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mCallback != null) {
            mCallback.onResume(mActivityHash);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mCallback != null) {
            mCallback.onPause(mActivityHash);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mCallback != null) {
            mCallback.onStop(mActivityHash);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (mCallback != null) {
            mCallback.onDestroy(mActivityHash);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mCallback != null) {
            mCallback.onDestroy(mActivityHash);
        }
    }
}
