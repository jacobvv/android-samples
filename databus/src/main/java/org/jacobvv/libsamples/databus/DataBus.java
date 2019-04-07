package org.jacobvv.libsamples.databus;

import java.util.HashMap;
import java.util.Map;

/**
 * 数据总线入口，用于创建和提供对应tag的数据通道
 *
 * @author jacob
 * @date 19-3-29
 */
public class DataBus {

    private volatile Map<String, Observable<Object>> bus;

    private DataBus() {
        bus = new HashMap<>();
    }

    private static final class Holder {
        private static final DataBus INSTANCE = new DataBus();
    }

    @SuppressWarnings("unchecked")
    public static <T> Observable<T> with(String key, @SuppressWarnings("unused") Class<T> clazz) {
        if (!Holder.INSTANCE.bus.containsKey(key)) {
            synchronized (Holder.INSTANCE) {
                if (!Holder.INSTANCE.bus.containsKey(key)) {
                    Holder.INSTANCE.bus.put(key, new DataChannel());
                }
            }
        }
        return (Observable<T>) Holder.INSTANCE.bus.get(key);
    }

    public static Observable<Object> with(String key) {
        return with(key, null);
    }

}
