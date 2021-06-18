package org.jacobvv.databus;

import android.app.Activity;
import androidx.annotation.NonNull;

/**
 * @author jacob
 * @date 19-3-29
 */
public interface Observable<T> {

    /**
     * 注册一个事件Observer，绑定Activity生命周期，即只在Activity活跃时接收到事件，
     * 当Activity处于非活跃状态时，发送的事件将会保存，直到Activity重新活跃时触发。
     * 当Activity销毁时，注册的Observer将会自动注销
     * 默认事件等级为{@link Lifecycle#STATE_STARTED}
     * <p>
     * 注意：当有其他的粘性Observer注册到同一个通道tag时，将会消费掉所有该tag保存的事件，
     * 此时再有Activity恢复活跃状态，对应tag的Observer将不会有任何事件触发。
     *
     * @param activity 观察者所在的Activity
     * @param observer 用于接收事件的观察者
     */
    void observe(@NonNull Activity activity, @NonNull Observer<T> observer);

    /**
     * 注册一个事件Observer，与{@link #observe(Activity, Observer)}类似，可以自定义活跃等级
     *
     * @param activity 观察者所在的Activity
     * @param level    观察等级，当观察者所在Activity活跃等级等于或者高于level时，才能接收到事件
     * @param observer 用于接收事件的观察者
     */
    void observe(@NonNull Activity activity, int level, @NonNull Observer<T> observer);

    /**
     * 注册一个事件Observer，与{@link #observe(Activity, Observer)}类似
     * 支持粘性事件，当Observer还未注册时发送的事件，在Observer使用该方法注册后将触发事件
     * 默认事件等级为{@link Lifecycle#STATE_STARTED}
     *
     * 注意：该方法注册Observer时会消费掉所有已保存的待触发事件，
     * 其中也包括因为Activity不在活跃状态从而保存的，用以在Activity活跃后触发的事件。
     *
     * @param activity 观察者所在的Activity
     * @param observer 用于接收事件的观察者
     */
    void observeSticky(@NonNull Activity activity, @NonNull Observer<T> observer);

    /**
     * 注册一个事件Observer，与{@link #observeSticky(Activity, Observer)}类似，可以自定义活跃等级
     *
     * @param activity 观察者所在的Activity
     * @param level    观察等级，当观察者所在Activity活跃等级等于或者高于level时，才能接收到事件
     * @param observer 用于接收事件的观察者
     */
    void observeSticky(@NonNull Activity activity, int level, @NonNull Observer<T> observer);

    /**
     * 注册一个事件Observer，与 {@link #observe(Activity, Observer)} 类似，
     * 但是没有生命周期，Observer始终保持活跃，这意味着该观察者始终可以接收到事件，
     * 并永远不会自动注销。所以应该在不需要时通过{@link #removeObserver(Observer)}
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
     * 发送事件，支持任意线程，最终Observer将会在主线程触发事件
     *
     * @param value 事件数据
     */
    void post(T value);

    /**
     * 延迟发送事件，支持任意线程，最终Observer将会在主线程触发事件
     *
     * @param value 事件数据
     * @param delay 延迟毫秒数
     */
    void postDelay(T value, long delay);

    /**
     * 生命周期回调，更改状态
     *
     * @param hash  Activity的hashcode
     * @param state Observer状态，参考Lifecycle接口的状态定义
     */
    void onStateChange(int hash, int state);

}
