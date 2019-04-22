package org.jacobvv.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static android.Manifest.permission;

/**
 * Permission权限相关工具类，主要包括动态权限申请，判断权限授权，跳转设置等。
 *
 * @author yinhui
 */
@SuppressWarnings({"WeakerAccess", "unused", "SameParameterValue", "ObsoleteSdkInt"})
public final class PermissionUtils {

    public static final String PHONE_STATE = "phone_state";
    public static final String PHONE = "phone";
    public static final String SMS = "sms";
    public static final String STORAGE = "storage";
    public static final String LOCATION = "location";
    public static final String CAMERA = "camera";
    public static final String CALENDAR = "calendar";
    public static final String MICROPHONE = "microphone";
    public static final String CONTACTS = "contacts";

    private static final String TAG = PermissionUtils.class.getSimpleName();

    private static final String[] GROUP_PHONE_STATE = {
            permission.READ_PHONE_STATE
    };
    private static final String[] GROUP_PHONE = {
            permission.READ_PHONE_STATE, permission.CALL_PHONE, permission.READ_CALL_LOG,
            permission.WRITE_CALL_LOG, permission.ADD_VOICEMAIL, permission.USE_SIP,
            permission.PROCESS_OUTGOING_CALLS
    };
    private static final String[] GROUP_SMS = {
            permission.SEND_SMS, permission.RECEIVE_SMS, permission.READ_SMS,
            permission.RECEIVE_WAP_PUSH, permission.RECEIVE_MMS,
    };
    private static final String[] GROUP_STORAGE = {
            permission.READ_EXTERNAL_STORAGE, permission.WRITE_EXTERNAL_STORAGE
    };
    private static final String[] GROUP_LOCATION = {
            permission.ACCESS_FINE_LOCATION, permission.ACCESS_COARSE_LOCATION
    };
    private static final String[] GROUP_CAMERA = {
            permission.CAMERA
    };
    private static final String[] GROUP_CALENDAR = {
            permission.READ_CALENDAR, permission.WRITE_CALENDAR
    };
    private static final String[] GROUP_MICROPHONE = {
            permission.RECORD_AUDIO
    };
    private static final String[] GROUP_CONTACTS = {
            permission.READ_CONTACTS, permission.WRITE_CONTACTS, permission.GET_ACCOUNTS
    };
    private static List<String> DECLARED_PERMISSIONS;

    /**
     * Don't let anyone instantiate this class.
     */
    private PermissionUtils() {
        throw new UnsupportedOperationException("Utils CANNOT be instantiated!");
    }

    private static String[] getPermissions(final String permission) {
        switch (permission) {
            case PHONE_STATE:
                return GROUP_PHONE_STATE;
            case PHONE:
                return GROUP_PHONE;
            case SMS:
                return GROUP_SMS;
            case STORAGE:
                return GROUP_STORAGE;
            case LOCATION:
                return GROUP_LOCATION;
            case CAMERA:
                return GROUP_CAMERA;
            case CALENDAR:
                return GROUP_CALENDAR;
            case MICROPHONE:
                return GROUP_MICROPHONE;
            case CONTACTS:
                return GROUP_CONTACTS;
            default:
        }
        return new String[]{permission};
    }

    /**
     * 获取应用申明的全部权限
     *
     * @return the mPermissions used in application
     */
    public static List<String> getPermissions(Context context) {
        PackageManager pm = context.getPackageManager();
        try {
            return Arrays.asList(
                    pm.getPackageInfo(context.getPackageName(), PackageManager.GET_PERMISSIONS)
                            .requestedPermissions
            );
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, "Package not found: " + e.getMessage());
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    /**
     * 判断权限是否被授权
     *
     * @param context    上下文
     * @param permission 权限
     * @return 是否被授权
     */
    public static boolean isGranted(final Context context, final String permission) {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.M ||
                ContextCompat.checkSelfPermission(context, permission)
                        == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * 判断权限是否被授权
     *
     * @param context     上下文
     * @param permissions 权限
     * @return 是否被授权
     */
    public static boolean isGranted(final Context context, final String... permissions) {
        for (String permission : permissions) {
            if (!isGranted(context, permission)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Launch the application's details settings.
     */
    public static void openAppDetailsSettings(Activity activity) {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.fromParts("package", activity.getPackageName(), null));
        activity.startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    }

    /**
     * Launch the application's details settings.
     */
    public static void openAppDetailsSettings(Fragment fragment) {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.fromParts("package", fragment.getContext().getPackageName(), null));
        fragment.startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    }

    /**
     * 创建申请权限的 PermissionHelper 对象
     *
     * @param activity    申请权限时的上下文
     * @param permissions 需要申请的权限
     * @return PermissionHelper 对象
     */
    public static PermissionHelper of(final Activity activity, final String... permissions) {
        if (DECLARED_PERMISSIONS == null) {
            DECLARED_PERMISSIONS = getPermissions(activity);
        }
        return new PermissionHelper(new ContextWrap(activity), permissions);
    }

    /**
     * 创建申请权限的 PermissionHelper 对象
     *
     * @param fragment    申请权限时的上下文
     * @param permissions 需要申请的权限
     * @return PermissionHelper 对象
     */
    public static PermissionHelper of(final Fragment fragment, final String... permissions) {
        if (DECLARED_PERMISSIONS == null) {
            DECLARED_PERMISSIONS = getPermissions(fragment.getActivity());
        }
        return new PermissionHelper(new ContextWrap(fragment), permissions);
    }

    /**
     * 用于解释接口中，一般情况下，对于第二次申请同一权限，需要对用户进行解释。
     * 该接口可以灵活决定是否继续申请权限，或者直接返回结果。
     *
     * @see RationaleListener
     */
    public interface PermissionRequest {
        /**
         * 继续权限申请
         */
        void requestContinue();

        /**
         * 直接返回结果
         */
        void requestResult();
    }

    /**
     * 解释接口，一般情况下，对于第二次申请同一权限，需要对用户进行解释。
     * 该接口提供了需要向用户解释权限的能力。
     */
    public interface RationaleListener {
        /**
         * 当需要向用户解释权限时被调用
         *
         * @param request     权限申请流程对象，使用该对象可以决定是否继续申请权限，或者直接返回结果。
         * @param permissions 需要解释的权限
         * @see PermissionRequest
         */
        void onRationaleListener(PermissionRequest request, List<String> permissions);
    }

    /**
     * 权限申请结果回调
     */
    public interface PermissionCallback {
        /**
         * 返回结果
         *
         * @param granted       被授权的权限
         * @param denied        被拒绝的权限
         * @param deniedForever 被永久拒绝的权限
         */
        void onResult(List<String> granted, List<String> denied, List<String> deniedForever);
    }

    public static class PermissionHelper {

        private ContextWrap mContext;
        private RationaleListener mRationaleListener;
        private PermissionCallback mPermissionCallback;
        private int mRequestCode = 8000022;
        private Set<String> mPermissions;
        private List<String> mPermissionsRequest;
        private List<String> mPermissionsRationale;
        private List<String> mPermissionsGranted;
        private List<String> mPermissionsDenied;
        private List<String> mPermissionsDeniedForever;

        private PermissionRequest mRequest = new PermissionRequest() {
            @Override
            public void requestContinue() {
                requestPermissions();
            }

            @Override
            public void requestResult() {
                returnResult();
            }
        };

        private PermissionHelper(ContextWrap context, String... permissions) {
            this.mContext = context;
            int count = permissions.length;
            mPermissions = new HashSet<>(count);
            mPermissionsGranted = new ArrayList<>(count);
            mPermissionsRequest = new ArrayList<>(count);
            mPermissionsRationale = new ArrayList<>(count);
            mPermissionsDenied = new ArrayList<>(count);
            mPermissionsDeniedForever = new ArrayList<>(count);
            preparePermissions(permissions);
        }

        private void preparePermissions(String... permissions) {
            List<String> illegalPermissions = new ArrayList<>();
            for (String permission : permissions) {
                for (String p : getPermissions(permission)) {
                    if (DECLARED_PERMISSIONS.contains(p)) {
                        mPermissions.add(p);
                    } else {
                        Log.e(TAG, "Permission: [" + p + "] MUST state in the manifest.");
                    }
                }
            }
            for (String permission : mPermissions) {
                if (isGranted(mContext.getActivity(), permission)) {
                    mPermissionsGranted.add(permission);
                } else {
                    if (shouldShowRationale(permission)) {
                        mPermissionsRationale.add(permission);
                    }
                    mPermissionsRequest.add(permission);
                }
            }
        }

        private boolean shouldShowRationale(String permission) {
            if (mContext.getFragment() == null) {
                return ActivityCompat.shouldShowRequestPermissionRationale(mContext.getActivity(), permission);
            } else {
                return mContext.getFragment().shouldShowRequestPermissionRationale(permission);
            }
        }

        private void requestPermissions() {
            String[] permissions = mPermissionsRequest.toArray(new String[0]);
            if (mContext.getFragment() == null) {
                ActivityCompat.requestPermissions(mContext.getActivity(), permissions, mRequestCode);
            } else {
                mContext.getFragment().requestPermissions(permissions, mRequestCode);
            }
        }

        private void returnResult() {
            mPermissionsDenied.addAll(mPermissionsRequest);
            List<String> granted = new ArrayList<>(mPermissionsGranted);
            List<String> denied = new ArrayList<>(mPermissionsDenied);
            List<String> deniedForever = new ArrayList<>(mPermissionsDeniedForever);
            int size = mPermissions.size();
            clear();
            if (mPermissionCallback != null) {
                mPermissionCallback.onResult(granted, denied, deniedForever);
            }
        }

        private void clear() {
            mPermissions.clear();
            mPermissionsGranted.clear();
            mPermissionsRequest.clear();
            mPermissionsRationale.clear();
            mPermissionsDenied.clear();
            mPermissionsDeniedForever.clear();
        }

        /**
         * 设置申请权限解释接口，Google 建议在同一个权限用户拒绝后，第二次申请时对用户进行解释
         *
         * @param l 解释接口
         * @return PermissionHelper 对象
         */
        public PermissionHelper setRationaleListener(RationaleListener l) {
            this.mRationaleListener = l;
            return this;
        }

        /**
         * 权限申请回调，传入权限申请的结果
         *
         * @param callback 权限申请回调
         * @return PermissionHelper 对象
         */
        public PermissionHelper setCallback(PermissionCallback callback) {
            this.mPermissionCallback = callback;
            return this;
        }

        /**
         * 请求申请权限
         *
         * @param requestCode 请求码
         * @return PermissionHelper 对象
         */
        public PermissionHelper request(int requestCode) {
            this.mRequestCode = requestCode;
            if (mPermissionsRequest.isEmpty()) {
                returnResult();
                return this;
            }
            if (mRationaleListener != null && mPermissionsRationale.size() > 0) {
                mRationaleListener.onRationaleListener(mRequest, mPermissionsRationale);
                return this;
            }
            requestPermissions();
            return this;
        }

        /**
         * 接受 Activity 回调，获取权限申请结果
         *
         * @param requestCode  请求码
         * @param permissions  The requested permissions. Never null.
         * @param grantResults The grant results for the corresponding permissions
         *                     which is either {@link PackageManager#PERMISSION_GRANTED}
         *                     or {@link PackageManager#PERMISSION_DENIED}. Never null.
         */
        public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                               @NonNull int[] grantResults) {
            if (requestCode != mRequestCode) {
                return;
            }
            for (int i = 0; i < permissions.length; i++) {
                String permission = permissions[i];
                mPermissionsRequest.remove(permission);
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    mPermissionsGranted.add(permission);
                } else {
                    if (shouldShowRationale(permission)) {
                        mPermissionsDenied.add(permission);
                    } else {
                        mPermissionsDeniedForever.add(permission);
                    }
                }
            }
            returnResult();
        }
    }

}