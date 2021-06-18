package org.jacobvv.utils;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresPermission;
import androidx.annotation.WorkerThread;

import java.io.File;
import java.util.Map;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;

/**
 * Uri/Url工具类
 *
 * @author yinhui
 * @date 18-6-26
 */
@SuppressWarnings({"WeakerAccess", "unused", "SameParameterValue", "ObsoleteSdkInt"})
public class UriUtils {

    private static final String TAG = UriUtils.class.getSimpleName();

    private static final char CHAR_Q = '?';
    private static final char CHAR_AND = '&';
    private static final char CHAR_EQUALS = '=';

    private static final String AUTHORITY_STORAGE_DOC = "com.android.externalstorage.documents";
    private static final String AUTHORITY_DOWNLOADS_DOC = "com.android.providers.downloads.documents";
    private static final String AUTHORITY_MEDIA_DOC = "com.android.providers.media.documents";

    private static final String URI_DOWNLOAD_CONTENT = "content://downloads/public_downloads";

    private static final String MEDIA_TYPE_PRIMARY = "primary";
    private static final String MEDIA_TYPE_AUDIO = "audio";
    private static final String MEDIA_TYPE_IMAGE = "image";
    private static final String MEDIA_TYPE_VIDEO = "video";

    private UriUtils() {
        throw new UnsupportedOperationException("Utils CANNOT be instantiated!");
    }

    /**
     * 通过baseUrl和参数构建url
     *
     * @param baseUrl 基础url地址
     * @param params  参数
     * @return 带参数的url地址
     */
    public static String buildUrl(@NonNull String baseUrl, Map<String, String> params) {
        if (CheckUtils.isEmpty(baseUrl)) {
            LogUtils.e("(buildUrl) Base url is empty.");
            return null;
        }
        if (params == null) {
            return baseUrl;
        }
        StringBuilder url = new StringBuilder(baseUrl);
        boolean isFirstParam = true;
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (isFirstParam && !baseUrl.contains(String.valueOf(CHAR_Q))) {
                url.append(CHAR_Q);
            } else {
                url.append(CHAR_AND);
            }
            url.append(entry.getKey()).append(CHAR_EQUALS).append(entry.getValue());
            isFirstParam = false;
        }
        return url.toString();
    }

    /**
     * 将file转换为Uri对象
     *
     * @param file 给定的file
     * @return 对应的Uri对象
     */
    public static Uri file2Uri(File file) {
        return Uri.fromFile(file);
    }

    /**
     * 将路径转换为Uri对象
     *
     * @param path 给定的路径
     * @return 对应的Uri对象
     */
    public static Uri path2Uri(String path) {
        return Uri.parse(path);
    }

    /**
     * Uri对象转换为File对象，如果无法解析则返null
     * 注意：建议使用异步调用，需要文件读取权限
     *
     * @param context 上下文
     * @param uri     给定的Uri对象
     * @return 对应的File对象
     */
    @WorkerThread
    @RequiresPermission(READ_EXTERNAL_STORAGE)
    public static File uri2File(Context context, Uri uri) {
        String path = uri2Path(context, uri);
        return CheckUtils.isEmpty(path) ? null : new File(path);
    }

    /**
     * Uri对象转换为路径，如果无法解析则返null
     * 注意：建议使用异步调用，需要文件读取权限
     *
     * @param context 上下文
     * @param uri     给定的Uri对象
     * @return 对应的路径
     */
    @WorkerThread
    @RequiresPermission(READ_EXTERNAL_STORAGE)
    public static String uri2Path(Context context, Uri uri) {
        String path;
        // 以 file:// 开头的
        if (ContentResolver.SCHEME_FILE.equals(uri.getScheme())) {
            return uri.getPath();
        }
        if (ContentResolver.SCHEME_CONTENT.equals(uri.getScheme())) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT
                    && DocumentsContract.isDocumentUri(context, uri)) {
                // SDK19及以上，属于Document类型，分情况获取Path
                if (AUTHORITY_STORAGE_DOC.equals(uri.getAuthority())) {
                    // ExternalStorageProvider
                    final String docId = DocumentsContract.getDocumentId(uri);
                    final String[] split = docId.split(":");
                    final String type = split[0];
                    if (MEDIA_TYPE_PRIMARY.equalsIgnoreCase(type)) {
                        path = Environment.getExternalStorageDirectory() + "/" + split[1];
                        return path;
                    }
                } else if (AUTHORITY_DOWNLOADS_DOC.equals(uri.getAuthority())) {
                    // DownloadsProvider
                    final String id = DocumentsContract.getDocumentId(uri);
                    final Uri contentUri = ContentUris.withAppendedId(
                            Uri.parse(URI_DOWNLOAD_CONTENT),
                            Long.valueOf(id));
                    path = getDataColumn(context, contentUri, null, null);
                    return path;
                } else if (AUTHORITY_MEDIA_DOC.equals(uri.getAuthority())) {
                    // MediaProvider
                    final String docId = DocumentsContract.getDocumentId(uri);
                    final String[] split = docId.split(":");
                    final String type = split[0];
                    Uri contentUri = null;
                    if (MEDIA_TYPE_IMAGE.equals(type)) {
                        contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                    } else if (MEDIA_TYPE_VIDEO.equals(type)) {
                        contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                    } else if (MEDIA_TYPE_AUDIO.equals(type)) {
                        contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                    }
                    final String selection = "_id=?";
                    final String[] selectionArgs = new String[]{split[1]};
                    path = getDataColumn(context, contentUri, selection, selectionArgs);
                    return path;
                }
            } else {
                // SDK19以下，直接查询Uri得到Path
                path = getDataColumn(context, uri, null, null);
                return path;
            }
        }
        return null;
    }

    private static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        final String[] projection = {MediaStore.MediaColumns.DATA};
        Cursor cursor = null;
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                return cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA));
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return null;
    }

}
