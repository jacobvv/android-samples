package org.jacobvv.libsamples.databus;

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

    private Lifecycle mLifecycle = new LifecycleImpl();

    private Handler mHandler = new Handler(Looper.getMainLooper());

    DataChannel() {
        this.mChannels = new SparseArray<>();
    }

    @Override
    public void observe(@NonNull final Activity activity, @NonNull final Observer<T> observer) {
        observe(activity, Observer.STATE_STARTED, observer);
    }

    @Override
    public void observe(@NonNull final Activity activity, int level,
                        @NonNull final Observer<T> observer) {
        observer.setLevel(level);
        if (Looper.myLooper() == Looper.getMainLooper()) {
            observeInternal(activity, observer);
        } else {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    observeInternal(activity, observer);
                }
            });
        }
    }

    @Override
    public void observeSticky(@NonNull Activity activity, @NonNull Observer<T> observer) {
        observer.setSticky();
        observe(activity, Observer.STATE_STARTED, observer);
    }

    @Override
    public void observeSticky(@NonNull Activity activity, int level, @NonNull Observer<T> observer) {
        observer.setSticky();
        observe(activity, level, observer);
    }

    @Override
    public void observeForever(@NonNull final Observer<T> observer) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            observeForeverInternal(observer);
        } else {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    observeForeverInternal(observer);
                }
            });
        }
    }

    @Override
    public void observeStickyForever(@NonNull Observer<T> observer) {
        observer.setSticky();
        observeForever(observer);
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
    private void observeInternal(@NonNull Activity activity,@NonNull Observer<T> observer) {
        int hash = activity.hashCode();
        if (mChannels.get(hash) == null) {
            mChannels.put(hash, new LinkedList<Observer<T>>());
            LifeFragment.inject(activity, mLifecycle);
        }
        LinkedList<Observer<T>> observers = mChannels.get(hash);
        observers.add(observer);
    }

    @MainThread
    private void observeForeverInternal(@NonNull Observer<T> observer) {
        // 对于无生命周期观察者，Key为0，并需要手动移除
        if (mChannels.get(0) == null) {
            mChannels.put(0, new LinkedList<Observer<T>>());
        }
        LinkedList<Observer<T>> observers = mChannels.get(0);
        observers.add(observer);
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
        for (int i = mChannels.size() - 1; i >= 0; i--) {
            int hash = mChannels.keyAt(i);
            if (hash == 0) {
                // TODO: Forever observer message management.
            }
            LinkedList<Observer<T>> observers = mChannels.valueAt(i);
            for (Observer<T> observer : observers) {
                if (observer.isActive()) {
                    try {
                        observer.onChanged(value);
                    } catch (ClassCastException ignored) {
                    }
                } else if (observer.isSticky()) {
                    // TODO: Sticky message management.
                }
            }
        }
    }

    @Override
    public void postDelay(T value, long delay) {

    }

    private class LifecycleImpl implements Lifecycle {

        @Override
        public void onCreate(int hash) {
            LinkedList<Observer<T>> observers = mChannels.get(hash);
            if (observers != null && !observers.isEmpty()) {
                for (Observer<T> observer : observers) {
                    observer.setState(Observer.STATE_CREATED);
                }
            }
        }

        @Override
        public void onStart(int hash) {
            LinkedList<Observer<T>> observers = mChannels.get(hash);
            if (observers != null && !observers.isEmpty()) {
                for (Observer<T> observer : observers) {
                    observer.setState(Observer.STATE_STARTED);
                }
            }
        }

        @Override
        public void onResume(int hash) {
            LinkedList<Observer<T>> observers = mChannels.get(hash);
            if (observers != null && !observers.isEmpty()) {
                for (Observer<T> observer : observers) {
                    observer.setState(Observer.STATE_RESUMED);
                }
            }
        }

        @Override
        public void onPause(int hash) {
            LinkedList<Observer<T>> observers = mChannels.get(hash);
            if (observers != null && !observers.isEmpty()) {
                for (Observer<T> observer : observers) {
                    observer.setState(Observer.STATE_STARTED);
                }
            }
        }

        @Override
        public void onStop(int hash) {
            LinkedList<Observer<T>> observers = mChannels.get(hash);
            if (observers != null && !observers.isEmpty()) {
                for (Observer<T> observer : observers) {
                    observer.setState(Observer.STATE_CREATED);
                }
            }
        }

        @Override
        public void onDestroy(int hash) {
            LinkedList<Observer<T>> observers = mChannels.get(hash);
            mChannels.remove(hash);
            if (observers != null) {
                observers.clear();
            }
        }
    }
}
