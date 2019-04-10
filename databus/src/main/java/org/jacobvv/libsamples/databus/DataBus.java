package org.jacobvv.libsamples.databus;

import java.util.HashMap;
import java.util.Map;

/**
 * 数据总线入口，用于创建和提供对应tag的数据通道
 *
 * @author jacob
 * @since 19-3-29
 */
public class DataBus {

    private volatile Map<String, Observable<Object>> bus;

    private Lifecycle lifecycle = new LifecycleImpl();

    private DataBus() {
        bus = new HashMap<>();
    }

    private static final class Holder {
        private static final DataBus INSTANCE = new DataBus();
    }

    @SuppressWarnings("unchecked")
    public static <T> Observable<T> with(String tag, @SuppressWarnings("unused") Class<T> clazz) {
        if (!Holder.INSTANCE.bus.containsKey(tag)) {
            synchronized (Holder.INSTANCE) {
                if (!Holder.INSTANCE.bus.containsKey(tag)) {
                    Holder.INSTANCE.bus.put(tag, new DataChannel());
                }
            }
        }
        return (Observable<T>) Holder.INSTANCE.bus.get(tag);
    }

    public static Observable<Object> with(String tag) {
        return with(tag, null);
    }

    static Lifecycle getLifecycle() {
        return Holder.INSTANCE.lifecycle;
    }

    private static class LifecycleImpl implements Lifecycle {

        @Override
        public void onCreate(int hash) {
            for (Observable<Object> channels : Holder.INSTANCE.bus.values()) {
                channels.onStateChange(hash, Lifecycle.STATE_CREATED);
            }
        }

        @Override
        public void onStart(int hash) {
            for (Observable<Object> channels : Holder.INSTANCE.bus.values()) {
                channels.onStateChange(hash, Lifecycle.STATE_STARTED);
            }
        }

        @Override
        public void onResume(int hash) {
            for (Observable<Object> channels : Holder.INSTANCE.bus.values()) {
                channels.onStateChange(hash, Lifecycle.STATE_RESUMED);
            }
        }

        @Override
        public void onPause(int hash) {
            for (Observable<Object> channels : Holder.INSTANCE.bus.values()) {
                channels.onStateChange(hash, Lifecycle.STATE_STARTED);
            }
        }

        @Override
        public void onStop(int hash) {
            for (Observable<Object> channels : Holder.INSTANCE.bus.values()) {
                channels.onStateChange(hash, Lifecycle.STATE_CREATED);
            }
        }

        @Override
        public void onDestroy(int hash) {
            for (Observable<Object> channels : Holder.INSTANCE.bus.values()) {
                channels.onStateChange(hash, Lifecycle.STATE_DESTROYED);
            }
        }
    }

}
