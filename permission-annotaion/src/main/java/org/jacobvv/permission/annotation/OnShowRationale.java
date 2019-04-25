package org.jacobvv.permission.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author jacob
 * @date 19-4-25
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.SOURCE)
public @interface OnShowRationale {
    /**
     * Request code for permission request.
     *
     * @return request code
     */
    int value() default 0;
}
