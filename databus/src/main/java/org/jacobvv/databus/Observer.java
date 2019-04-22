package org.jacobvv.databus;

import android.support.annotation.MainThread;
import android.support.annotation.Nullable;

/**
 * @author jacob
 * @date 19-4-5
 */
public abstract class Observer<T> {

    private int mState = Lifecycle.STATE_INITIALIZED;
    private int mLevel = Lifecycle.STATE_STARTED;

    /**
     * 设置Observer的状态
     * @param state 具体状态
     * @return 如果Observer的活跃发生变化，则返回True
     */
    boolean setState(int state) {
        boolean isActive = isActive();
        this.mState = state;
        return !isActive && isActive();
    }

    void setLevel(int level) {
        this.mLevel = level;
    }

    /**
     * 判断该观察者是否处于活跃状态，如果当前状态等级高于设定的活跃等级及认定为活跃
     *
     * @return 观察者是否处于活跃状态
     */
    boolean isActive() {
        return mState >= mLevel;
    }

    /**
     * 接收事件的消息回调，只会在主线程中触发
     *
     * @param t 事件数据
     */
    @MainThread
    protected abstract void onChanged(@Nullable T t);
}
