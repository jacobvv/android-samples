package org.jacobvv.permission;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jacob
 * @date 19-4-23
 */
public final class PermissionUtils {

    private static final String TAG = "Permission";

    /**
     * 判断权限是否被授权
     *
     * @param context    上下文
     * @param permission 权限
     * @return 是否被授权
     */
    public static boolean isGranted(final Context context, final String permission) {
        return ContextCompat.checkSelfPermission(context, permission)
                == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * 判断权限是否被授权
     *
     * @param context     上下文
     * @param permissions 权限
     * @return 是否被授权
     */
    public static List<String> shouldPermissionRequest(final Context context, final String... permissions) {
        List<String> denied = new ArrayList<>(permissions.length);
        for (String permission : permissions) {
            if (!isGranted(context, permission)) {
                denied.add(permission);
            }
        }
        return denied;
    }

    public static List<String> shouldShowRationale(Activity activity, String... permissions) {
        List<String> permissionsShouldShow = new ArrayList<>(permissions.length);
        for (String permission : permissions) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
                permissionsShouldShow.add(permission);
            }
        }
        return permissionsShouldShow;
    }

    public static List<String> shouldShowRationale(Fragment fragment, String... permissions) {
        List<String> permissionsShouldShow = new ArrayList<>(permissions.length);
        for (String permission : permissions) {
            if (fragment.shouldShowRequestPermissionRationale(permission)) {
                permissionsShouldShow.add(permission);
            }
        }
        return permissionsShouldShow;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public List<String> shouldShowRationale(android.app.Fragment fragment, String... permissions) {
        List<String> permissionsShouldShow = new ArrayList<>(permissions.length);
        for (String permission : permissions) {
            if (fragment.shouldShowRequestPermissionRationale(permission)) {
                permissionsShouldShow.add(permission);
            }
        }
        return permissionsShouldShow;
    }

    public static List<String> checkPermissionResult(String[] permissions, int[] grantResults) {
        List<String> denied = new ArrayList<>(permissions.length);
        if (grantResults.length == 0) {
            return denied;
        }
        for (int i = 0; i < grantResults.length; i++) {
            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                denied.add(permissions[i]);
            }
        }
        return denied;
    }

}
