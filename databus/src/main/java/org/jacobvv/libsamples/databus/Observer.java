package org.jacobvv.libsamples.databus;

import android.support.annotation.Nullable;

/**
 * @author jacob
 * @date 19-4-5
 */
public abstract class Observer<T> {

    public static final int STATE_DESTROYED = 0;
    public static final int STATE_INITIALIZED = 1;
    public static final int STATE_CREATED = 2;
    public static final int STATE_STARTED = 3;
    public static final int STATE_RESUMED = 4;

    private int mState = STATE_INITIALIZED;
    private int mLevel = STATE_STARTED;
    private boolean isSticky = false;

    void setState(int state) {
        this.mState = state;
    }

    void setLevel(int level) {
        this.mLevel = level;
    }

    void setSticky() {
        this.isSticky = true;
    }

    boolean isActive() {
        return mState >= mLevel;
    }

    boolean isSticky() {
        return isSticky;
    }

    /**
     * Called when the data is changed.
     * @param t  The new data
     */
    protected abstract void onChanged(@Nullable T t);
}
