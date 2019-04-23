package org.jacobvv.androidsamples.permission;

import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;


import org.jacobvv.permission.PermissionUtils;
import org.jacobvv.permission.annotaion.PermissionRequest;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * @author jacob
 * @date 19-4-23
 */
final class PermissionActivity_PermissionHelper {
    private static final int REQUEST_TAKEPHOTO = 0;
    private static final String[] PERMISSION_TAKEPHOTO = new String[] {"android.permission.CAMERA"};
    private PermissionActivity_PermissionHelper() {
    }

    static void takePhoto_WithPermissionCheck(@NonNull PermissionActivity target) {
        List<String> permissionsDenied = PermissionUtils.shouldPermissionRequest(target, PERMISSION_TAKEPHOTO);
        if (permissionsDenied.isEmpty()) {
            target.takePhoto();
        } else {
            List<String> permissionsRationale = PermissionUtils.shouldShowRationale(target,
                    PERMISSION_TAKEPHOTO);
            if (!permissionsRationale.isEmpty()) {
                permissionsDenied.removeAll(permissionsRationale);
                // TODO: Show rationale if necessary.
            } else {
                ActivityCompat.requestPermissions(target, PERMISSION_TAKEPHOTO, REQUEST_TAKEPHOTO);
            }
        }
    }

    static void onRequestPermissionsResult(@NonNull PermissionActivity target, int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {
        List<String> deniedForever = PermissionUtils.checkPermissionResult(permissions, grantResults);
        switch (requestCode) {
            case REQUEST_TAKEPHOTO:
                if (deniedForever.isEmpty()) {
                    target.takePhoto();
                } else {
                    List<String> denied = PermissionUtils.shouldShowRationale(target,
                            PERMISSION_TAKEPHOTO);
                    deniedForever.removeAll(denied);
                    // TODO: Callback permissions denied if necessary.
                }
                break;
            default:
                break;
        }
    }

    private static final class TakePhoto_PermissionRequest implements PermissionRequest {
        private final WeakReference<PermissionActivity> weakTarget;
        private final List<String> permissionsDenied;
        private final List<String> permissionsRationale;
        private TakePhoto_PermissionRequest(@NonNull PermissionActivity target,
                                            @NonNull List<String> permissionsDenied,
                                            @NonNull List<String> permissionsRationale) {
            this.weakTarget = new WeakReference<>(target);
            this.permissionsDenied = permissionsDenied;
            this.permissionsRationale = permissionsRationale;
        }

        @Override
        public void proceed() {
            PermissionActivity target = weakTarget.get();
            if (target != null) {
                ActivityCompat.requestPermissions(target, PERMISSION_TAKEPHOTO, REQUEST_TAKEPHOTO);
            }
        }

        @Override
        public void cancel() {
            PermissionActivity target = weakTarget.get();
            if (target != null) {
                // TODO: Callback permissions denied if necessary.
            }
        }
    }

}
