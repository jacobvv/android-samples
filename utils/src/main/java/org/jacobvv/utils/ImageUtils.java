/*
 * Copyright 2014 Zhenguo Jin
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jacobvv.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.IntDef;
import android.support.annotation.RawRes;
import android.support.annotation.WorkerThread;
import android.support.media.ExifInterface;
import android.util.Log;
import android.view.View;

import org.jacobvv.utils.luban.Luban;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

/**
 * Bitmap工具类主要包括获取Bitmap和对Bitmap的操作
 *
 * @author jingle1267@163.com
 */
@SuppressWarnings({"WeakerAccess", "unused", "SameParameterValue", "ObsoleteSdkInt"})
public final class ImageUtils {

    public static final int DPI_LDPI = 120;
    public static final int DPI_MDPI = 160;
    public static final int DPI_HDPI = 240;
    public static final int DPI_XHDPI = 320;
    public static final int DPI_XXHDPI = 480;
    public static final int DPI_XXXHDPI = 640;

    private static final String TAG = ImageUtils.class.getSimpleName();

    private static final String JPG = "jpg";
    private static final String JPEG = "jpeg";
    private static final String PNG = "png";
    private static final String WEBP = "webp";
    private static final String GIF = "gif";
    private static final String BMP = "bmp";

    private static final List<String> FORMAT_JPG = new ArrayList<>(2);
    private static final List<String> FORMAT_ALL = new ArrayList<>(6);

    static {
        FORMAT_JPG.add(JPG);
        FORMAT_JPG.add(JPEG);
        FORMAT_ALL.add(JPG);
        FORMAT_ALL.add(JPEG);
        FORMAT_ALL.add(GIF);
        FORMAT_ALL.add(WEBP);
        FORMAT_ALL.add(BMP);
        FORMAT_ALL.add(PNG);
    }

    /**
     * Don't let anyone instantiate this class.
     */
    private ImageUtils() {
        throw new UnsupportedOperationException("Utils CANNOT be instantiated!");
    }

    /**
     * 使用Options进行Bitmap缩放
     * 参见：https://developer.android.com/topic/performance/graphics/load-bitmap
     *
     * @param reqWidth  目标宽度,实际缩放的图片不小于目标宽度
     * @param reqHeight 目标高度,与宽度类似
     */
    public static void calculateInSampleSize(final BitmapFactory.Options options,
                                             final int reqWidth, final int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        boolean isRequestValid = reqWidth > 0 && reqHeight > 0 && (height > reqHeight || width > reqWidth);
        if (isRequestValid) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }
        options.inSampleSize = inSampleSize;
        options.inJustDecodeBounds = false;
    }

    /**
     * 从资源读取图片Bitmap
     *
     * @param res   资源上下文对象
     * @param resId 资源ID
     * @return 图片Bitmap对象
     */
    public static Bitmap getBitmapFromResource(Resources res, @DrawableRes int resId) {
        if (res == null) {
            return null;
        }
        return BitmapFactory.decodeResource(res, resId);
    }

    /**
     * 从资源读取图片Bitmap
     *
     * @param res       资源上下文对象
     * @param resId     资源ID
     * @param reqWidth  目标宽度
     * @param reqHeight 目标高度
     * @return 图片Bitmap对象
     */
    public static Bitmap getBitmapFromResource(
            Resources res, @RawRes int resId, int reqWidth, int reqHeight) {
        if (res == null) {
            return null;
        }
        return getBitmapFromStream(res.openRawResource(resId), null, reqWidth, reqHeight);
    }
//
//    /**
//     * 使用RxJava异步从资源读取图片Bitmap
//     *
//     * @param res       资源上下文对象
//     * @param resId     资源ID
//     * @param reqWidth  目标宽度
//     * @param reqHeight 目标高度
//     * @return 包含图片Bitmap对象的Observable对象
//     */
//    public static Observable<Bitmap> getBitmapFromResourceAsync(
//            final Resources res, @RawRes final int resId, final int reqWidth, final int reqHeight) {
//        if (res == null) {
//            return null;
//        }
//        return getBitmapFromStreamAsync(res.openRawResource(resId), reqWidth, reqHeight);
//    }

    /**
     * 从文件读取图片Bitmap
     * <p>
     * 注意：必须异步调用
     *
     * @param context   Context上下文
     * @param path      图片文件路径
     * @param inDensity 图片文件像素密度
     * @return 图片Bitmap对象
     */
    @WorkerThread
    public static Bitmap getBitmapFromFile(Context context, String path, @Density int inDensity) {
        if (context == null || !isImageFile(path)) {
            return null;
        }
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inDensity = inDensity;
        opts.inTargetDensity = context.getResources().getDisplayMetrics().densityDpi;
        Bitmap bitmap = BitmapFactory.decodeFile(path, opts);
        ExifInterface exif = null;
        if (isJpg(path)) {
            try {
                exif = new ExifInterface(path);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (exif != null) {
            bitmap = rotateImageByExif(bitmap, exif);
        }
        return bitmap;
    }

    /**
     * 从文件读取图片Bitmap
     * <p>
     * 注意：必须异步调用
     *
     * @param context Context上下文
     * @param path    图片文件路径
     * @return 图片Bitmap对象
     */
    @WorkerThread
    public static Bitmap getBitmapFromFile(Context context, String path) {
        if (context == null || !isImageFile(path)) {
            return null;
        }
        Bitmap bitmap = BitmapFactory.decodeFile(path);
        ExifInterface exif = null;
        if (isJpg(path)) {
            try {
                exif = new ExifInterface(path);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (exif != null) {
            bitmap = rotateImageByExif(bitmap, exif);
        }
        return bitmap;
    }

    /**
     * 从文件读取图片Bitmap
     * <p>
     * 注意：必须异步调用
     *
     * @param path      图片文件路径
     * @param reqWidth  目标宽度
     * @param reqHeight 目标高度
     * @return 图片Bitmap对象
     */
    @WorkerThread
    public static Bitmap getBitmapFromFile(String path, int reqWidth, int reqHeight) {
        if (!isImageFile(path)) {
            return null;
        }
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, opts);
        calculateInSampleSize(opts, reqWidth, reqHeight);
        Bitmap bitmap = BitmapFactory.decodeFile(path, opts);
        ExifInterface exif = null;
        if (isJpg(path)) {
            try {
                exif = new ExifInterface(path);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (exif != null) {
            bitmap = rotateImageByExif(bitmap, exif);
        }
        return bitmap;
    }
//
//    /**
//     * 使用RxJava异步从文件读取图片Bitmap
//     *
//     * @param context   上下文Context
//     * @param path      图片文件路径
//     * @param inDensity 图片文件像素密度
//     * @return 包含图片Bitmap对象的Observable对象
//     */
//    public static Observable<Bitmap> getBitmapFromFileAsync(
//            final Context context, final String path, @Density final int inDensity) {
//        return Observable.create(new Observable.OnSubscribe<Bitmap>() {
//            @Override
//            public void call(Subscriber<? super Bitmap> subscriber) {
//                subscriber.onStart();
//                Bitmap bitmap = getBitmapFromFile(context, path, inDensity);
//                if (bitmap == null) {
//                    subscriber.onError(new RuntimeException("Context is NULL or src is not a file."));
//                    return;
//                }
//                subscriber.onNext(bitmap);
//                subscriber.onCompleted();
//            }
//        }).subscribeOn(Schedulers.io());
//    }
//
//    /**
//     * 使用RxJava异步从文件读取图片Bitmap
//     *
//     * @param context 上下文Context
//     * @param path    图片文件路径
//     * @return 包含图片Bitmap对象的Observable对象
//     */
//    public static Observable<Bitmap> getBitmapFromFileAsync(final Context context,
//                                                            final String path) {
//        return Observable.create(new Observable.OnSubscribe<Bitmap>() {
//            @Override
//            public void call(Subscriber<? super Bitmap> subscriber) {
//                subscriber.onStart();
//                Bitmap bitmap = getBitmapFromFile(context, path);
//                if (bitmap == null) {
//                    subscriber.onError(new RuntimeException("Context is NULL or src is not a file."));
//                    return;
//                }
//                subscriber.onNext(bitmap);
//                subscriber.onCompleted();
//            }
//        }).subscribeOn(Schedulers.io());
//    }
//
//    /**
//     * 使用RxJava异步从文件读取图片Bitmap
//     *
//     * @param path      图片文件路径
//     * @param reqWidth  目标宽度
//     * @param reqHeight 目标高度
//     * @return 包含图片Bitmap对象的Observable对象
//     */
//    public static Observable<Bitmap> getBitmapFromFileAsync(
//            final String path, final int reqWidth, final int reqHeight) {
//        return Observable.create(new Observable.OnSubscribe<Bitmap>() {
//            @Override
//            public void call(Subscriber<? super Bitmap> subscriber) {
//                subscriber.onStart();
//                Bitmap bitmap = getBitmapFromFile(path, reqWidth, reqHeight);
//                if (bitmap == null) {
//                    subscriber.onError(new RuntimeException("Src is not a file."));
//                }
//                subscriber.onNext(bitmap);
//                subscriber.onCompleted();
//            }
//        }).subscribeOn(Schedulers.io());
//    }

    /**
     * 从字节数组读取图片Bitmap
     *
     * @param data 字节数组
     * @return 图片Bitmap对象
     */
    public static Bitmap getBitmapFromByteArray(byte[] data) {
        return getBitmapFromByteArray(data, 0, data.length);
    }

    /**
     * 从字节数组读取图片Bitmap
     *
     * @param data   字节数组
     * @param offset 开始读取的偏移量
     * @param length 读取数据的长度
     * @return 图片Bitmap对象
     */
    public static Bitmap getBitmapFromByteArray(byte[] data, int offset, int length) {
        if (data == null || data.length == 0 || length == 0) {
            return null;
        }
        return BitmapFactory.decodeByteArray(data, offset, length);
    }

    /**
     * 从字节数组读取图片Bitmap
     *
     * @param data      字节数组
     * @param offset    开始读取的偏移量
     * @param length    读取数据的长度
     * @param reqWidth  目标宽度
     * @param reqHeight 目标高度
     * @return 图片Bitmap对象
     */
    public static Bitmap getBitmapFromByteArray(
            byte[] data, int offset, int length, int reqWidth, int reqHeight) {
        if (data == null || data.length == 0 || length == 0) {
            return null;
        }
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(data, offset, length, options);
        calculateInSampleSize(options, reqWidth, reqHeight);
        return BitmapFactory.decodeByteArray(data, offset, length, options);
    }

    /**
     * 从输入流读取图片Bitmap
     * <p>
     * 注意：必须异步调用
     *
     * @param is        输入流
     * @param reqWidth  目标宽度
     * @param reqHeight 目标高度
     * @return 图片Bitmap对象
     */
    @WorkerThread
    public static Bitmap getBitmapFromStream(InputStream is, int reqWidth, int reqHeight) {
        return getBitmapFromStream(is, null, reqWidth, reqHeight);
    }

    /**
     * 从输入流读取图片Bitmap
     * <p>
     * 注意：必须异步调用
     *
     * @param is         输入流
     * @param outPadding 读取图片的Padding值
     * @param reqWidth   目标宽度
     * @param reqHeight  目标高度
     * @return 图片Bitmap对象
     */
    @WorkerThread
    public static Bitmap getBitmapFromStream(
            InputStream is, Rect outPadding, int reqWidth, int reqHeight) {
        if (is == null) {
            return null;
        }
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(is, outPadding, options);
        calculateInSampleSize(options, reqWidth, reqHeight);
        return BitmapFactory.decodeStream(is, outPadding, options);
    }
//
//    /**
//     * 使用RxJava异步从输入流读取图片Bitmap
//     *
//     * @param is        输入流
//     * @param reqWidth  目标宽度
//     * @param reqHeight 目标高度
//     * @return 包含图片Bitmap对象的Observable对象
//     */
//    public static Observable<Bitmap> getBitmapFromStreamAsync(
//            InputStream is, int reqWidth, int reqHeight) {
//        return getBitmapFromStreamAsync(is, null, reqWidth, reqHeight);
//    }
//
//    /**
//     * 使用RxJava异步从输入流读取图片Bitmap
//     *
//     * @param is         输入流
//     * @param outPadding 读取图片的Padding值
//     * @param reqWidth   目标宽度
//     * @param reqHeight  目标高度
//     * @return 包含图片Bitmap对象的Observable对象
//     */
//    public static Observable<Bitmap> getBitmapFromStreamAsync(
//            final InputStream is, final Rect outPadding, final int reqWidth, final int reqHeight) {
//        return Observable.create(new Observable.OnSubscribe<Bitmap>() {
//            @Override
//            public void call(Subscriber<? super Bitmap> subscriber) {
//                subscriber.onStart();
//                Bitmap bitmap = getBitmapFromStream(is, outPadding, reqWidth, reqHeight);
//                if (bitmap == null) {
//                    subscriber.onError(new RuntimeException("Src is NULL."));
//                }
//                subscriber.onNext(bitmap);
//                subscriber.onCompleted();
//            }
//        }).subscribeOn(Schedulers.io());
//    }

    /**
     * 从Drawable对象中读取Bitmap
     *
     * @param drawable Drawable对象
     * @return Bitmap对象
     */
    public static Bitmap getBitmapFromDrawable(Drawable drawable) {
        if (drawable == null) {
            return null;
        }
        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if (bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }
        Bitmap bitmap;
        if (drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1,
                    drawable.getOpacity() != PixelFormat.OPAQUE
                            ? Config.ARGB_8888
                            : Config.RGB_565);
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                    drawable.getIntrinsicHeight(),
                    drawable.getOpacity() != PixelFormat.OPAQUE
                            ? Config.ARGB_8888
                            : Config.RGB_565);
        }
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    /**
     * 从View生成Bitmap
     *
     * @param view View对象
     * @return Bitmap对象
     */
    public static Bitmap getBitmapFromView(View view) {
        return getBitmapFromView(view, -1, -1);
    }

    /**
     * 从View生成Bitmap
     *
     * @param view      View对象
     * @param reqWidth  目标宽度
     * @param reqHeight 目标高度
     * @return Bitmap对象
     */
    public static Bitmap getBitmapFromView(View view, int reqWidth, int reqHeight) {
        if (view == null) {
            return null;
        }
        int width = view.getWidth();
        int height = view.getHeight();
        view.clearFocus();
        view.setPressed(false);

        // 保存View缓存状态
        boolean willNotCache = view.willNotCacheDrawing();
        int color = view.getDrawingCacheBackgroundColor();

        view.setWillNotCacheDrawing(false);
        view.setDrawingCacheBackgroundColor(0);
        if (color != 0) {
            view.destroyDrawingCache();
        }
        view.buildDrawingCache();
        Bitmap cacheBitmap = view.getDrawingCache();
        Bitmap bitmap;
        boolean needScale = reqWidth > 0 && reqHeight > 0
                && reqWidth != width && reqHeight != height;
        if (cacheBitmap == null) {
            bitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            view.layout(view.getLeft(), view.getTop(), view.getRight(),
                    view.getBottom());
            view.draw(canvas);
            if (needScale) {
                bitmap = scale(bitmap, reqWidth, reqHeight, true);
            }
        } else {
            if (needScale) {
                bitmap = scale(cacheBitmap, reqWidth, reqHeight, true);
            } else {
                bitmap = Bitmap.createBitmap(cacheBitmap);
            }
        }
        // Restore the view
        view.destroyDrawingCache();
        view.setWillNotCacheDrawing(willNotCache);
        view.setDrawingCacheBackgroundColor(color);
        return bitmap;
    }

    /**
     * 从Activity生成Bitmap
     *
     * @param activity Activity对象
     * @return Bitmap对象
     */
    public static Bitmap getBitmapFromActivity(Activity activity) {
        return getBitmapFromActivity(activity, -1, -1);
    }

    /**
     * 从Activity生成Bitmap
     *
     * @param activity  Activity对象
     * @param reqWidth  目标宽度
     * @param reqHeight 目标高度
     * @return Bitmap对象
     */
    public static Bitmap getBitmapFromActivity(Activity activity, int reqWidth, int reqHeight) {
        if (activity == null) {
            return null;
        }
        View view = activity.getWindow().getDecorView();
        Bitmap viewBitmap = getBitmapFromView(view);

        Rect frame = new Rect();
        view.getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;
        Point point = new Point();
        activity.getWindowManager().getDefaultDisplay().getSize(point);
        int width = point.x;
        int height = point.y - statusBarHeight;

        if (reqWidth > 0 && reqHeight > 0 && reqWidth != width && reqHeight != height) {
            Matrix matrix = new Matrix();
            float scaleWidth = (float) reqWidth / width;
            float scaleHeight = (float) reqHeight / height;
            float scale = scaleWidth > scaleHeight ? scaleWidth : scaleHeight;
            matrix.postScale(scale, scale);
            return Bitmap.createBitmap(viewBitmap, 0, statusBarHeight, width, height, matrix, true);
        } else {
            return Bitmap.createBitmap(viewBitmap, 0, statusBarHeight, width, height);
        }
    }

    /**
     * 将Bitmap对象转为字节数组
     *
     * @param bitmap Bitmap对象
     * @return 字节数组
     */
    public static byte[] getBytesFromBitmap(Bitmap bitmap) {
        if (isEmptyBitmap(bitmap)) {
            return null;
        }
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
        return os.toByteArray();
    }

    /**
     * 将Bitmap对象转为像素数组
     *
     * @param bitmap Bitmap对象
     * @return 像素数组
     */
    public static int[] getPixelFromBitmap(Bitmap bitmap) {
        if (isEmptyBitmap(bitmap)) {
            return null;
        }
        int[] pixels = new int[bitmap.getWidth() * bitmap.getHeight()];
        bitmap.getPixels(pixels, 0, bitmap.getWidth(), 0, 0,
                bitmap.getWidth(), bitmap.getHeight());
        return pixels;
    }

    /**
     * 将Bitmap进行缩放
     *
     * @param bitmap     要缩放的Bitmap
     * @param reqWidth   目标宽度
     * @param reqHeight  目标高度
     * @param equalRatio 是否宽度高度等比例缩放
     * @return 缩放后的Bitmap
     */
    public static Bitmap scale(Bitmap bitmap, float reqWidth, float reqHeight,
                               boolean equalRatio) {
        if (isEmptyBitmap(bitmap)) {
            return null;
        }
        float width = bitmap.getWidth();
        float height = bitmap.getHeight();
        Matrix matrix = new Matrix();
        float scaleWidth = reqWidth / width;
        float scaleHeight = reqHeight / height;
        if (equalRatio) {
            // 选择宽和高中最大的比率缩放，这样可以保证最终图片的宽和高一定都会大于等于目标的宽和高。
            float scale = scaleWidth > scaleHeight ? scaleWidth : scaleHeight;
            scaleWidth = scale;
            scaleHeight = scale;
        }
        matrix.postScale(scaleWidth, scaleHeight);
        return Bitmap.createBitmap(bitmap, 0, 0, (int) width, (int) height, matrix, true);
    }

    /**
     * 使用图片的EXIF信息，将图片旋转为正确的方向
     *
     * @param bitmap Bitmap对象
     * @param exif   EXIF信息对象
     * @return 旋转后的Bitmap对象
     */
    public static Bitmap rotateImageByExif(Bitmap bitmap, ExifInterface exif) {
        if (isEmptyBitmap(bitmap) || exif == null) {
            return bitmap;
        }
        Matrix matrix = new Matrix();
        int angle = 0;
        int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                angle = 90;
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                angle = 180;
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                angle = 270;
                break;
            default:
        }
        matrix.postRotate(angle);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    /**
     * 使用鲁班对图片进行压缩，并保存在给定的路径
     * <p>
     * 注意：必须异步调用
     *
     * @param src     源图片
     * @param dest    目标图片
     * @param quality 压缩质量
     * @return 是否成功压缩
     */
    @WorkerThread
    public static boolean compressImageByLuban(String src, String dest, int quality) {
        return Luban.compress(src, dest, quality);
    }
//
//    /**
//     * 通过RxJava异步使用鲁班对图片进行压缩，并保存在给定的路径
//     *
//     * @param src     源图片
//     * @param dest    目标图片
//     * @param quality 压缩质量
//     * @return 包含压缩后的图片文件对象的Observable对象
//     */
//    public static Observable<File> compressImageAsyncByLuban(String src, String dest, int quality) {
//        return Luban.compressAsync(src, dest, quality);
//    }

    /**
     * 对图片Bitmap进行压缩
     *
     * @param src       要压缩的Bitmap对象
     * @param reqWidth  目标宽度
     * @param reqHeight 目标高度
     * @param quality   压缩质量
     * @return 压缩后的Bitmap对象
     */
    public static Bitmap compressImage(Bitmap src, int reqWidth, int reqHeight,
                                       Bitmap.CompressFormat format, int quality) {
        if (isEmptyBitmap(src)) {
            return null;
        }
        // 缩放
        Bitmap bitmap;
        if (reqWidth > 0 && reqHeight > 0 && src.getWidth() != reqWidth && src.getHeight() != reqHeight) {
            bitmap = scale(src, reqWidth, reqHeight, true);
            bitmap = bitmap == null ? src : bitmap;
        } else {
            bitmap = src;
        }
        // 使用质量压缩写入流
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        bitmap.compress(format, quality, os);
        return getBitmapFromByteArray(os.toByteArray());
    }

    /**
     * 对图片文件进行压缩，并保存在给定的路径
     * <p>
     * 注意：必须异步调用
     *
     * @param src       源图片
     * @param reqWidth  目标宽度
     * @param reqHeight 目标高度
     * @param quality   压缩质量
     * @return 是否成功压缩
     */
    @WorkerThread
    public static boolean compressImage(String src, String dest, int reqWidth, int reqHeight,
                                        Bitmap.CompressFormat format, int quality) {
        if (!isImageFile(src) || FileUtils.delete(new File(dest))) {
            return false;
        }
        // 使用Options读取源文件大小信息
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(src, opts);
        // 计算合适的缩放比例
        calculateInSampleSize(opts, reqWidth, reqHeight);
        // 使用缩放将图片读取到内存
        Bitmap bitmap = BitmapFactory.decodeFile(src, opts);
        // 读取源文件的EXIF信息，必要时进行旋转
        ExifInterface exif = null;
        String suffix = src.substring(src.lastIndexOf(".") + 1).toLowerCase();
        if (FORMAT_JPG.contains(suffix)) {
            try {
                exif = new ExifInterface(src);
            } catch (IOException e) {
                Log.w(TAG, "Load EXIF info failed.");
            }
        }
        if (exif != null) {
            bitmap = rotateImageByExif(bitmap, exif);
        }
        // 使用质量压缩写入文件
        BufferedOutputStream bos = null;
        try {
            bos = new BufferedOutputStream(new FileOutputStream(dest));
            bitmap.compress(format, quality, bos);
        } catch (IOException e) {
            Log.e(TAG, "File open error: " + e.getMessage());
            return false;
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e) {
                    Log.e(TAG, "File open error: " + e.getMessage());
                }
            }
        }
        return true;
    }
//
//    /**
//     * 通过RxJava异步对图片文件进行压缩，并保存在给定的路径
//     *
//     * @param src       源图片
//     * @param dest      目标图片
//     * @param reqWidth  目标宽度
//     * @param reqHeight 目标高度
//     * @param quality   压缩质量
//     * @return 包含压缩后的图片文件对象的Observable对象
//     */
//    public static Observable<File> compressImageAsync(
//            final String src, final String dest, final int reqWidth, final int reqHeight,
//            final Bitmap.CompressFormat format, final int quality) {
//        return Observable.create(new Observable.OnSubscribe<File>() {
//            @Override
//            public void call(Subscriber<? super File> subscriber) {
//                subscriber.onStart();
//                boolean result = compressImage(src, dest, reqWidth, reqHeight, format, quality);
//                if (!result) {
//                    subscriber.onError(new RuntimeException("Compress failed."));
//                }
//                subscriber.onNext(new File(dest));
//                subscriber.onCompleted();
//            }
//        }).subscribeOn(Schedulers.io());
//    }

    /**
     * Save the bitmap.
     *
     * @param src     The source of bitmap.
     * @param file    The file.
     * @param recycle True to recycle the source of bitmap, false otherwise.
     * @return {@code true}: success<br>{@code false}: fail
     */
    @WorkerThread
    public static boolean save(final Bitmap src, final String file,
                               Bitmap.CompressFormat format, int quality, boolean recycle) {
        if (isEmptyBitmap(src) || !FileUtils.delete(new File(file))) {
            return false;
        }
        boolean ret = false;
        OutputStream os = null;
        try {
            os = new BufferedOutputStream(new FileOutputStream(file));
            ret = src.compress(format, quality, os);
            if (recycle && !src.isRecycled()) {
                src.recycle();
            }
        } catch (IOException e) {
            Log.e(TAG, "Save bitmap to file FAILED. " + e.getMessage());
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    Log.e(TAG, "Save bitmap to file FAILED. " + e.getMessage());
                }
            }
        }
        return ret;
    }
//
//    /**
//     * 通过RxJava异步将bitmap保存至文件。
//     *
//     * @param src     The source of bitmap.
//     * @param file    The file.
//     * @param recycle True to recycle the source of bitmap, false otherwise.
//     * @return 包含已保存文件的Observable对象
//     */
//    public static Observable<File> saveAsync(
//            final Bitmap src, final String file,
//            final Bitmap.CompressFormat format, final int quality, final boolean recycle) {
//        return Observable.create(new Observable.OnSubscribe<File>() {
//            @Override
//            public void call(Subscriber<? super File> subscriber) {
//                subscriber.onStart();
//                boolean save = save(src, file, format, quality, recycle);
//                if (save) {
//                    subscriber.onNext(new File(file));
//                } else {
//                    subscriber.onError(new RuntimeException("Save bitmap to file FAILED."));
//                }
//                subscriber.onCompleted();
//            }
//        });
//    }

    /**
     * 对给定的Bitmap对象设置亮度
     *
     * @param src        Bitmap对象
     * @param brightness 亮度
     * @return 调整亮度后的Bitmap
     */
    public static Bitmap setBitmapBright(Bitmap src, int brightness) {
        if (isEmptyBitmap(src)) {
            return null;
        }
        Bitmap bmp = Bitmap.createBitmap(src.getWidth(), src.getHeight(), Config.ARGB_8888);
        ColorMatrix matrix = new ColorMatrix();
        matrix.set(new float[]{
                1, 0, 0, 0, brightness,
                0, 1, 0, 0, brightness,
                0, 0, 1, 0, brightness,
                0, 0, 0, 1, 0
        });
        Paint paint = new Paint();
        paint.setColorFilter(new ColorMatrixColorFilter(matrix));
        Canvas canvas = new Canvas(bmp);
        canvas.drawBitmap(src, 0, 0, paint);
        return bmp;
    }

    /**
     * 判断文件是否是图片文件
     *
     * @param file 文件路径
     * @return 是否是图片文件
     */
    public static boolean isImageFile(String file) {
        if (!FileUtils.isFile(file)) {
            return false;
        }
        String suffix = file.substring(file.lastIndexOf(".") + 1).toLowerCase();
        return FORMAT_ALL.contains(suffix);
    }

    /**
     * 判断文件是否是图片文件
     *
     * @param file 文件对象
     * @return 是否是图片文件
     */
    public static boolean isImageFile(File file) {
        if (!file.isFile()) {
            return false;
        }
        String path = file.getPath();
        String suffix = path.substring(path.lastIndexOf(".") + 1).toLowerCase();
        return FORMAT_ALL.contains(suffix);
    }

    /**
     * 判断文件是否是JPG图片文件
     *
     * @param file 文件路径
     * @return 是否是JPG图片文件
     */
    public static boolean isJpg(String file) {
        if (!FileUtils.isFile(file)) {
            return false;
        }
        String suffix = file.substring(file.lastIndexOf(".") + 1).toLowerCase();
        return FORMAT_JPG.contains(suffix);
    }

    /**
     * 判断文件是否是JPG图片文件
     *
     * @param file 文件对象
     * @return 是否是JPG图片文件
     */
    public static boolean isJpg(File file) {
        if (!file.isFile()) {
            return false;
        }
        String path = file.getPath();
        String suffix = path.substring(path.lastIndexOf(".") + 1).toLowerCase();
        return FORMAT_JPG.contains(suffix);
    }

    private static boolean isEmptyBitmap(final Bitmap src) {
        return src == null || src.getWidth() == 0 || src.getHeight() == 0;
    }

    /**
     * DPI限定
     */
    @IntDef({DPI_LDPI, DPI_MDPI, DPI_HDPI, DPI_XHDPI, DPI_XXHDPI, DPI_XXXHDPI})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Density {
    }
}
