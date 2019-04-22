package org.jacobvv.utils;

import android.content.res.Resources;
import android.os.Build;
import android.text.TextUtils;

import java.util.Locale;
import java.util.TimeZone;

/**
 * @author yinhui
 * @date 18-6-4
 */
@SuppressWarnings({"WeakerAccess", "unused", "SameParameterValue", "ObsoleteSdkInt"})
public final class DeviceUtils {

    private static final String TAG = DeviceUtils.class.getSimpleName();

    private static String sDeviceInfo;

    private DeviceUtils() {
        throw new UnsupportedOperationException("Utils CANNOT be instantiated!");
    }

    /**
     * 获取当前时区
     *
     * @return 当前时区
     */
    public static String getTimeZone() {
        return TimeZone.getDefault().getDisplayName(false, TimeZone.SHORT);
    }

    /**
     * 获取当前语言
     *
     * @return 当前语言
     */
    public static String getLanguage() {
        return Locale.getDefault().getLanguage();
    }

    /**
     * 获取Android设置中设定的首要语言
     *
     * @return 系统首要语言
     */
    public static String getSystemLanguage() {
        Locale locale;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            locale = Resources.getSystem().getConfiguration().getLocales().get(0);
        } else {
            locale = Resources.getSystem().getConfiguration().locale;
        }
        return locale.getLanguage();
    }

    public static String getIncremental() {
        return Build.VERSION.INCREMENTAL;
    }

    /**
     * Return the version name of sRomType's system.
     *
     * @return the version name of sRomType's system
     */
    public static String getSdkVersionName() {
        return Build.VERSION.RELEASE;
    }

    /**
     * Return version code of sRomType's system.
     *
     * @return version code of sRomType's system
     */
    public static int getSdkVersionCode() {
        return Build.VERSION.SDK_INT;
    }

    /**
     * Return the manufacturer of the product/hardware.
     * <p>e.g. Xiaomi</p>
     *
     * @return the manufacturer of the product/hardware
     */
    public static String getManufacturer() {
        return Build.MANUFACTURER;
    }

    public static String getBrand() {
        return Build.BRAND;
    }

    /**
     * Return the model of sRomType.
     * <p>e.g. MI2SC</p>
     *
     * @return the model of sRomType
     */
    public static String getModel() {
        String model = Build.MODEL;
        if (model != null) {
            model = model.trim().replaceAll("\\s*", "");
        } else {
            model = "";
        }
        return model;
    }

    /**
     * 获取系统信息
     *
     * @return 系统信息
     */
    public static String getDeviceInfo() {
        if (TextUtils.isEmpty(sDeviceInfo)) {
            StringBuilder sb = new StringBuilder();
            sb.append(getModel())
                    .append("_android")
                    .append(getSdkVersionName())
                    .append(getManufacturer())
                    .append("_")
                    .append(getBrand());
            String incremental = Build.VERSION.INCREMENTAL;
            if (!TextUtils.isEmpty(incremental)) {
                sb.append("_").append(incremental);
            }
            sDeviceInfo = sb.toString();
        }
        return sDeviceInfo;
    }

}
