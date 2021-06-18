package org.jacobvv.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


/**
 * @author yinhui
 */
@SuppressWarnings({"WeakerAccess", "unused", "SameParameterValue", "ObsoleteSdkInt"})
public final class AppUtils {

    public static final String PACKAGE_WECHAT = "com.tencent.mm";
    public static final String PACKAGE_QQ = "com.tencent.mobileqq";
    public static final String PACKAGE_LINCOMB = "com.lincomb.licai";
    public static final String PACKAGE_OFO = "so.ofo.labofo";

    private static final String TAG = AppUtils.class.getSimpleName();

    private static final String APK_SUFFIX = ".apk";
    private static final String APK_MIME_TYPE = "application/vnd.android.package-archive";
    private static AppInfo app;

    private AppUtils() {
        throw new UnsupportedOperationException("Utils CANNOT be instantiated!");
    }

    /**
     * Install the app.
     * <p>Target APIs greater than 25 must hold
     * {@code <uses-permission android:name="android.permission.REQUEST_INSTALL_PACKAGES" />}</p>
     *
     * @param filePath The path of file.
     */
    public static void installApp(Context context, final String filePath, String authority) {
        if (!FileUtils.isFile(filePath) || filePath.toLowerCase().endsWith(APK_SUFFIX)) {
            Log.e(TAG, "Target path is not a APK file.");
            return;
        }
        installApp(context, new File(filePath), authority);
    }

    /**
     * Install the app.
     * <p>Target APIs greater than 25 must hold
     * {@code <uses-permission android:name="android.permission.REQUEST_INSTALL_PACKAGES" />}</p>
     *
     * @param file The file.
     */
    public static void installApp(Context context, final File file, String authority) {
        if (!file.isFile() || !file.getName().toLowerCase().endsWith(APK_SUFFIX)) {
            Log.e(TAG, "Install: target file is not a APK file.");
            return;
        }
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri data;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            //如果没有设置 SDCard 写权限，或者没有 SDCard,apk 文件保存在内存中，需要授予权限才能安装
            try {
                String[] command = {"chmod", "777", file.getPath()};
                ProcessBuilder builder = new ProcessBuilder(command);
                builder.start();
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
            data = Uri.fromFile(file);
        } else {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            data = FileProvider.getUriForFile(context, authority, file);
        }
        intent.setDataAndType(data, APK_MIME_TYPE);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * Uninstall the app.
     *
     * @param packageName The name of the package.
     */
    public static void uninstallApp(Context context, final String packageName) {
        if (TextUtils.isEmpty(packageName)) {
            Log.e(TAG, "Uninstall: Package name is empty.");
            return;
        }
        Intent intent = new Intent(Intent.ACTION_DELETE);
        intent.setData(Uri.parse("package:" + packageName));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * Return whether the app is installed.
     *
     * @param action   The Intent action, such as ACTION_VIEW.
     * @param category The desired category.
     * @return {@code true}: yes<br>{@code false}: no
     */
    public static boolean isAppInstalled(Context context, @NonNull final String action,
                                         @NonNull final String category) {
        Intent intent = new Intent(action);
        intent.addCategory(category);
        PackageManager pm = context.getPackageManager();
        ResolveInfo info = pm.resolveActivity(intent, 0);
        return info != null;
    }

    /**
     * Return whether the app is installed.
     *
     * @param packageName The name of the package.
     * @return {@code true}: yes<br>{@code false}: no
     */
    public static boolean isAppInstalled(Context context, @NonNull final String packageName) {
        return !TextUtils.isEmpty(packageName) &&
                context.getPackageManager().getLaunchIntentForPackage(packageName) != null;
    }

    /**
     * Return whether application is foreground.
     *
     * @return {@code true}: yes<br>{@code false}: no
     */
    public static boolean isAppForeground(Context context) {
        return isAppForeground(context, context.getPackageName());
    }

    /**
     * Return whether application is foreground.
     *
     * @return {@code true}: yes<br>{@code false}: no
     */
    public static boolean isAppForeground(Context context, String packageName) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (am == null) {
            return false;
        }
        List<ActivityManager.RunningAppProcessInfo> appInfoList = am.getRunningAppProcesses();
        if (appInfoList == null || appInfoList.size() == 0) {
            return false;
        }
        for (ActivityManager.RunningAppProcessInfo info : appInfoList) {
            if (info.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return info.processName.equals(packageName);
            }
        }
        return false;
    }

    /**
     * 获取本应用信息
     *
     * @param context 上下文
     * @return 应用信息
     */
    public static AppInfo getAppInfo(Context context) {
        if (app == null) {
            app = getAppInfo(context, context.getPackageName());
        }
        return app;
    }

    /**
     * 获取特定应用信息
     *
     * @param context     上下文
     * @param packageName 包名
     * @return 应用信息
     */
    public static AppInfo getAppInfo(Context context, final String packageName) {
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(packageName, 0);
            return getAppInfo(pm, pi);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取指定元数据的值
     *
     * @param context  上下文
     * @param metaName 指定元数据的名称
     * @return 元数据的值。获取失败则返回 null
     */
    public static String getAppMetaValueByName(Context context, String metaName) {
        String value = null;
        ApplicationInfo appInfo;
        try {
            appInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        if (appInfo != null && appInfo.metaData != null) {
            value = appInfo.metaData.getString(metaName);
        }
        return value;
    }


    /**
     * 获取所有已安装的应用信息
     *
     * @param context 上下文
     * @return 应用信息列表
     */
    public static List<AppInfo> getAppsInfo(Context context) {
        List<AppInfo> list = new ArrayList<>();
        PackageManager pm = context.getPackageManager();
        List<PackageInfo> installedPackages = pm.getInstalledPackages(0);
        for (PackageInfo pi : installedPackages) {
            AppInfo ai = getAppInfo(pm, pi);
            if (ai == null) {
                continue;
            }
            list.add(ai);
        }
        return list;
    }

    private static AppInfo getAppInfo(final PackageManager pm, final PackageInfo pi) {
        if (pm == null || pi == null) {
            return null;
        }
        ApplicationInfo ai = pi.applicationInfo;
        String packageName = pi.packageName;
        String name = ai.loadLabel(pm).toString();
        Drawable icon = ai.loadIcon(pm);
        String packagePath = ai.sourceDir;
        String versionName = pi.versionName;
        int versionCode = pi.versionCode;
        boolean isSystem = (ApplicationInfo.FLAG_SYSTEM & ai.flags) != 0;
        return new AppInfo(packageName, name, icon, packagePath, versionName, versionCode, isSystem);
    }

    /**
     * The application's information.
     */
    public static class AppInfo {

        private String packageName;
        private String name;
        private Drawable icon;
        private String packagePath;
        private String versionName;
        private int versionCode;
        private boolean isSystem;

        private AppInfo(String packageName, String name, Drawable icon, String packagePath,
                        String versionName, int versionCode, boolean isSystem) {
            this.name = name;
            this.icon = icon;
            this.packageName = packageName;
            this.packagePath = packagePath;
            this.versionName = versionName;
            this.versionCode = versionCode;
            this.isSystem = isSystem;
        }

        public String getPackageName() {
            return packageName;
        }

        public String getName() {
            return name;
        }

        public Drawable getIcon() {
            return icon;
        }

        public String getPackagePath() {
            return packagePath;
        }

        public String getVersionName() {
            return versionName;
        }

        public int getVersionCode() {
            return versionCode;
        }

        public boolean isSystem() {
            return isSystem;
        }

        @Override
        public String toString() {
            return "AppInfo{" +
                    "packageName='" + packageName + '\'' +
                    ", name='" + name + '\'' +
                    ", icon=" + icon +
                    ", packagePath='" + packagePath + '\'' +
                    ", versionName='" + versionName + '\'' +
                    ", versionCode=" + versionCode +
                    ", isSystem=" + isSystem +
                    '}';
        }
    }
}
