package org.jacobvv.androidsamples.permission.log;

import org.jacobvv.baserecycler.BaseArrayAdapter;

/**
 * @author jacob
 * @date 19-4-28
 */
public final class LogAdapterFactory {

    public static BaseArrayAdapter<Object> create() {
        BaseArrayAdapter<Object> adapter = new BaseArrayAdapter<>();
        adapter.register(GrantedLog.class, new GrantedLog.GrantedType());
        adapter.register(DeniedLog.class, new DeniedLog.DeniedType());
        return adapter;
    }
}
