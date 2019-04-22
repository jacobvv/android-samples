package org.jacobvv.databus;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.util.SparseArray;

import java.util.LinkedList;

/**
 * 数据通道，对于每个Activity，保存了用于通讯的事件和消息
 * 能够根据Activity的生命周期，动态释放数据通道，并能够实现粘性消息
 *
 * @author jacob
 * @date 19-4-5
 */
public class DataChannel<T> implements Observable<T> {

    private SparseArray<LinkedList<Observer<T>>> mChannels;
    private SparseArray<LinkedList<T>> mPendingMessages;

    private Handler mHandler = new Handler(Looper.getMainLooper());

    DataChannel() {
        this.mChannels = new SparseArray<>();
        this.mPendingMessages = new SparseArray<>();
    }

    @Override
    public void observe(@NonNull final Activity activity, @NonNull final Observer<T> observer) {
        observe(activity, Lifecycle.STATE_STARTED, observer);
    }

    @Override
    public void observe(@NonNull final Activity activity, int level,
                        @NonNull final Observer<T> observer) {
        observer.setLevel(level);
        if (Looper.myLooper() == Looper.getMainLooper()) {
            observeInternal(activity, observer, false);
        } else {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    observeInternal(activity, observer, false);
                }
            });
        }
    }

    @Override
    public void observeSticky(@NonNull Activity activity, @NonNull Observer<T> observer) {
        observeSticky(activity, Lifecycle.STATE_STARTED, observer);
    }

    @Override
    public void observeSticky(@NonNull final Activity activity, int level,
                              @NonNull final Observer<T> observer) {
        observer.setLevel(level);
        if (Looper.myLooper() == Looper.getMainLooper()) {
            observeInternal(activity, observer, true);
        } else {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    observeInternal(activity, observer, true);
                }
            });
        }
    }

    @Override
    public void observeForever(@NonNull final Observer<T> observer) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            observeForeverInternal(observer, false);
        } else {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    observeForeverInternal(observer, false);
                }
            });
        }
    }

    @Override
    public void observeStickyForever(@NonNull final Observer<T> observer) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            observeForeverInternal(observer, true);
        } else {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    observeForeverInternal(observer, true);
                }
            });
        }
    }

    @Override
    public void removeObserver(@NonNull final Observer<T> observer) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            removeObserverInternal(observer);
        } else {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    removeObserverInternal(observer);
                }
            });
        }
    }

    @MainThread
    private void observeInternal(@NonNull Activity activity, @NonNull Observer<T> observer,
                                 boolean sticky) {
        int hash = activity.hashCode();
        if (mChannels.get(hash) == null) {
            mChannels.put(hash, new LinkedList<Observer<T>>());
            LifeFragment.inject(activity, DataBus.getLifecycle());
        }
        LinkedList<Observer<T>> observers = mChannels.get(hash);
        observers.add(observer);
        // 如果是粘性事件，则触发所有待触发事件，并清空列表
        if (sticky) {
            dispatchAllPendingEvents(observer);
        }
    }

    @MainThread
    private void observeForeverInternal(@NonNull Observer<T> observer, boolean sticky) {
        // 对于无生命周期观察者，Key为0，并需要手动移除
        if (mChannels.get(0) == null) {
            mChannels.put(0, new LinkedList<Observer<T>>());
        }
        LinkedList<Observer<T>> observers = mChannels.get(0);
        observers.add(observer);
        // 如果是粘性事件，则触发所有待触发事件，并清空列表
        if (sticky) {
            dispatchAllPendingEvents(observer);
        }
    }

    @MainThread
    private void removeObserverInternal(@NonNull Observer<T> observer) {
        for (int i = mChannels.size() - 1; i >= 0; i--) {
            LinkedList<Observer<T>> observers = mChannels.valueAt(i);
            observers.remove(observer);
        }
    }

    @Override
    public void post(T value) {
        boolean noObserver = true;
        for (int i = mChannels.size() - 1; i >= 0; i--) {
            int hash = mChannels.keyAt(i);
            boolean observerUnactivated = false;
            LinkedList<Observer<T>> observers = mChannels.valueAt(i);
            for (Observer<T> observer : observers) {
                noObserver = false;
                if (hash == 0 || observer.isActive()) {
                    setValue(observer, value);
                } else {
                    observerUnactivated = true;
                }
            }
            if (observerUnactivated) {
                // 此时有相关Observer不活跃，缓存该事件，以供后续Observer活跃时触发
                synchronized (this) {
                    LinkedList<T> eventList = mPendingMessages.get(hash);
                    if (eventList == null) {
                        eventList = new LinkedList<>();
                        mPendingMessages.put(hash, eventList);
                    }
                    eventList.add(value);
                }
            }
        }
        if (noObserver) {
            // 此时还未有相关Observer注册，缓存该事件，以供后续粘性Observer触发
            synchronized (this) {
                LinkedList<T> eventList = mPendingMessages.get(0);
                if (eventList == null) {
                    eventList = new LinkedList<>();
                    mPendingMessages.put(0, eventList);
                }
                eventList.add(value);
            }
        }
    }

    @Override
    public void postDelay(final T value, long delay) {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                post(value);
            }
        }, delay);
    }

    private void setValue(final Observer<T> observer, final T value) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            try {
                observer.onChanged(value);
            } catch (ClassCastException ignored) {
            }
        } else {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    try {
                        observer.onChanged(value);
                    } catch (ClassCastException ignored) {
                    }
                }
            });
        }
    }

    @MainThread
    private void dispatchPendingEvents(int hash, @NonNull Observer<T> observer) {
        LinkedList<T> events = mPendingMessages.get(0);
        if (events != null && !events.isEmpty()) {
            for (T value : events) {
                observer.onChanged(value);
            }
        }
        events = mPendingMessages.get(hash);
        if (events != null && !events.isEmpty()) {
            for (T value : events) {
                observer.onChanged(value);
            }
        }
        mPendingMessages.remove(0);
        mPendingMessages.remove(hash);
    }

    @MainThread
    private void dispatchAllPendingEvents(@NonNull Observer<T> observer) {
        for (int i = mPendingMessages.size() - 1; i >= 0; i--) {
            LinkedList<T> events = mPendingMessages.valueAt(i);
            for (T value : events) {
                observer.onChanged(value);
            }
        }
        mPendingMessages.clear();
    }

    @Override
    public void onStateChange(int hash, int state) {
        LinkedList<Observer<T>> observers = mChannels.get(hash);
        if (state == Lifecycle.STATE_DESTROYED) {
            // 如果Activity被销毁，则移除所有相关的Observer
            mChannels.remove(hash);
            if (observers != null) {
                observers.clear();
            }
        } else if (observers != null && !observers.isEmpty()) {
            // 如果Activity状态发生变化，则将状态设置到所有相关的Observer中，
            // 并根据活跃状况，触发缓存的事件
            for (Observer<T> observer : observers) {
                boolean activeChanged = observer.setState(state);
                if (activeChanged) {
                    dispatchPendingEvents(hash, observer);
                }
            }
        }
    }
}
