package org.jacobvv.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.WorkerThread;

import java.util.Map;

/**
 * SharedPreference工具方法类
 *
 * @author yinhui
 * @date 18-6-12
 */
@SuppressWarnings({"unused", "SameParameterValue", "ObsoleteSdkInt"})
public final class PreferenceUtils {

    private static final String TAG = PreferenceUtils.class.getSimpleName();

    private PreferenceUtils() {
        throw new UnsupportedOperationException("Utils CANNOT be instantiated!");
    }

    /**
     * 从SharedPreference中读取数据
     *
     * @param context  上下文
     * @param name     SharedPreference名称
     * @param key      键
     * @param defValue 默认值
     * @param <T>      获取的数据类型
     * @return 返回相应的值，如果类型不匹配，或者不支持的数据类型，则返回默认值
     */
    @SuppressWarnings("unchecked")
    public static <T> T get(Context context, String name, String key, T defValue) {
        if (defValue == null) {
            LogUtils.e(TAG, "Default value is null.");
            return null;
        }
        SharedPreferences sp = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        try {
            if (defValue instanceof String) {
                return (T) sp.getString(key, (String) defValue);
            } else if (defValue instanceof Boolean) {
                return (T) (Boolean) sp.getBoolean(key, (Boolean) defValue);
            } else if (defValue instanceof Integer) {
                return (T) (Integer) sp.getInt(key, (Integer) defValue);
            } else if (defValue instanceof Long) {
                return (T) (Long) sp.getLong(key, (Long) defValue);
            } else if (defValue instanceof Float) {
                return (T) (Float) sp.getFloat(key, (Float) defValue);
            } else {
                LogUtils.e(TAG, "Type of " + defValue.getClass().getSimpleName()
                        + " is not supported.");
                return defValue;
            }
        } catch (ClassCastException e) {
            LogUtils.e(TAG, "Type of default value is not match with value stored. "
                    + e.getMessage());
            return defValue;
        }
    }

    /**
     * 异步将数据存入SharedPreference
     *
     * @param context 上下文
     * @param name    SharedPreference名称
     * @param key     键
     * @param value   值
     */
    public static void put(Context context, String name, String key, Object value) {
        SharedPreferences sp = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        if (value instanceof String) {
            edit.putString(key, (String) value);
        } else if (value instanceof Boolean) {
            edit.putBoolean(key, (Boolean) value);
        } else if (value instanceof Integer) {
            edit.putInt(key, (Integer) value);
        } else if (value instanceof Long) {
            edit.putLong(key, (Long) value);
        } else if (value instanceof Float) {
            edit.putFloat(key, (Float) value);
        } else {
            LogUtils.e(TAG, "Value: " + value + " is not supported.");
        }
        edit.apply();
    }

    /**
     * 异步将数据存入SharedPreference
     *
     * @param context 上下文
     * @param name    SharedPreference名称
     * @param values  键值对集合
     */
    public static void put(Context context, String name, Map<String, Object> values) {
        SharedPreferences sp = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        for (Map.Entry<String, Object> entry : values.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            if (value instanceof String) {
                edit.putString(key, (String) value);
            } else if (value instanceof Boolean) {
                edit.putBoolean(key, (Boolean) value);
            } else if (value instanceof Integer) {
                edit.putInt(key, (Integer) value);
            } else if (value instanceof Long) {
                edit.putLong(key, (Long) value);
            } else if (value instanceof Float) {
                edit.putFloat(key, (Float) value);
            } else {
                LogUtils.e(TAG, "Value: " + value + " is not supported.");
            }
        }
        edit.apply();
    }

    /**
     * 立即将数据存入SharedPreference
     * <p>
     * 注意：建议使用异步调用
     *
     * @param context 上下文
     * @param name    SharedPreference名称
     * @param key     键
     * @param value   值
     * @return 保存是否成功
     */
    @WorkerThread
    public static boolean putImmediately(Context context, String name, String key, Object value) {
        SharedPreferences sp = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        if (value instanceof String) {
            edit.putString(key, (String) value);
        } else if (value instanceof Boolean) {
            edit.putBoolean(key, (Boolean) value);
        } else if (value instanceof Integer) {
            edit.putInt(key, (Integer) value);
        } else if (value instanceof Long) {
            edit.putLong(key, (Long) value);
        } else if (value instanceof Float) {
            edit.putFloat(key, (Float) value);
        } else {
            LogUtils.e(TAG, "Value: " + value + " is not supported.");
        }
        return edit.commit();
    }

    /**
     * 立即将数据存入SharedPreference
     * <p>
     * 注意：建议使用异步调用
     *
     * @param context 上下文
     * @param name    SharedPreference名称
     * @param values  键值对集合
     * @return 保存是否成功
     */
    @WorkerThread
    public static boolean putImmediately(Context context, String name, Map<String, Object> values) {
        SharedPreferences sp = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        for (Map.Entry<String, Object> entry : values.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            if (value instanceof String) {
                edit.putString(key, (String) value);
            } else if (value instanceof Boolean) {
                edit.putBoolean(key, (Boolean) value);
            } else if (value instanceof Integer) {
                edit.putInt(key, (Integer) value);
            } else if (value instanceof Long) {
                edit.putLong(key, (Long) value);
            } else if (value instanceof Float) {
                edit.putFloat(key, (Float) value);
            } else {
                LogUtils.e(TAG, "Value: " + value + " is not supported.");
            }
        }
        return edit.commit();
    }

    /**
     * 判断是否已经存储给定键的数据
     *
     * @param context 上下文
     * @param name    SharedPreference名称
     * @param key     键
     * @return 是否已经存储给定键的数据
     */
    public static boolean contains(Context context, String name, String key) {
        return context.getSharedPreferences(name, Context.MODE_PRIVATE).contains(key);
    }

    /**
     * 异步清空SharedPreference中的数据
     *
     * @param context 上下文
     * @param name    SharedPreference名称
     */
    public static void clear(Context context, String name) {
        context.getSharedPreferences(name, Context.MODE_PRIVATE).edit().clear().apply();
    }

    /**
     * 异步清空SharedPreference中的某些键的数据
     *
     * @param context 上下文
     * @param name    SharedPreference名称
     * @param keys    需要清空的键
     */
    public static void clear(Context context, String name, String... keys) {
        if (keys == null || keys.length == 0) {
            return;
        }
        SharedPreferences sp = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        for (String key : keys) {
            edit.remove(key);
        }
        edit.apply();
    }

    /**
     * 立即清空SharedPreference中的数据
     * <p>
     * 注意：建议使用异步调用
     *
     * @param context 上下文
     * @param name    SharedPreference名称
     * @return 是否清空成功
     */
    @WorkerThread
    public static boolean clearImmediately(Context context, String name) {
        return context.getSharedPreferences(name, Context.MODE_PRIVATE).edit().clear().commit();
    }

    /**
     * 立即清空SharedPreference中的数据
     * <p>
     * 注意：建议使用异步调用
     *
     * @param context 上下文
     * @param name    SharedPreference名称
     * @param keys    需要清空的键
     * @return 是否清空成功
     */
    @WorkerThread
    public static boolean clearImmediately(Context context, String name, String... keys) {
        if (keys == null || keys.length == 0) {
            return false;
        }
        SharedPreferences sp = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        for (String key : keys) {
            edit.remove(key);
        }
        return edit.commit();
    }
}
