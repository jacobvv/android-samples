package org.jacobvv.permission.annotaion;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author jacob
 * @date 19-4-22
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.METHOD)
public @interface NeedPermission {
    String[] value();
}
