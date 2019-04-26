package org.jacobvv.permission.annotation;

/**
 * @author jacob
 * @date 19-4-23
 */
public interface PermissionRequest<T> {
    void proceed(T target);

    void cancel(T target);
}
