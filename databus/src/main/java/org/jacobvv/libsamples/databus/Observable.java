package org.jacobvv.libsamples.databus;

import android.app.Activity;
import android.support.annotation.NonNull;

/**
 * @author jacob
 * @date 19-3-29
 */
public interface Observable<T> {

    /**
     * 注册一个Observer，支持Activity生命周期绑定，销毁自动取消注册
     * 默认事件等级为{@link Observer#STATE_STARTED}
     *
     * @param activity 观察者所在的Activity
     * @param observer 用于接收事件的观察者
     */
    void observe(@NonNull Activity activity, @NonNull Observer<T> observer);

    /**
     * 注册一个Observer，支持Activity生命周期绑定，支持自定义事件等级，销毁自动取消注册
     *
     * @param activity 观察者所在的Activity
     * @param level    观察等级，当观察者所在Activity活跃等级等于或者高于level时，才能接收到事件
     * @param observer 用于接收事件的观察者
     */
    void observe(@NonNull Activity activity, int level, @NonNull Observer<T> observer);

    /**
     * 注册一个粘性事件Observer，支持Activity生命周期绑定，销毁自动取消注册
     * 默认事件等级为{@link Observer#STATE_STARTED}
     * 如果Activity不活跃时发送事件，可以在活跃后收到消息
     *
     * @param activity 观察者所在的Activity
     * @param observer 用于接收事件的观察者
     */
    void observeSticky(@NonNull Activity activity, @NonNull Observer<T> observer);

    /**
     * 注册一个粘性事件Observer，支持Activity生命周期绑定，支持自定义事件等级，销毁自动取消注册
     * 如果Activity不活跃时发送事件，可以在活跃后收到消息
     *
     * @param activity 观察者所在的Activity
     * @param level    观察等级，当观察者所在Activity活跃等级等于或者高于level时，才能接收到事件
     * @param observer 用于接收事件的观察者
     */
    void observeSticky(@NonNull Activity activity, int level, @NonNull Observer<T> observer);

    /**
     * 注册一个事件Observer，与 {@link #observe(Activity, Observer)} 类似，
     * 但是没有生命周期，Observer始终保持活跃，这意味着该观察者始终可以接收到事件，
     * 并永远不会自动注销。所以使用时应该在不需要时通过{@link #removeObserver(Observer)}
     * 手动注销该Observer
     *
     * @param observer 用于接收事件的观察者
     */
    void observeForever(@NonNull Observer<T> observer);

    /**
     * 注册一个事件Observer，与 {@link #observeSticky(Activity, Observer)} 类似，
     * 但是没有生命周期，Observer始终保持活跃，这意味着该观察者始终可以接收到事件，
     * 并永远不会自动注销。所以使用时应该在不需要时通过{@link #removeObserver(Observer)}
     * 手动注销该Observer
     *
     * @param observer 用于接收事件的观察者
     */
    void observeStickyForever(@NonNull Observer<T> observer);

    /**
     * 注销给定的Observer
     *
     * @param observer 用于接收事件的观察者
     */
    void removeObserver(@NonNull Observer<T> observer);

    /**
     * 发送事件，支持任意线程
     *
     * @param value 事件数据
     */
    void post(T value);

    /**
     * 延迟发送事件，支持任意线程
     *
     * @param value 事件数据
     * @param delay 延迟毫秒数
     */
    void postDelay(T value, long delay);

}
