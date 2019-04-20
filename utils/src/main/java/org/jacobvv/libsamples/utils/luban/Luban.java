package org.jacobvv.libsamples.utils.luban;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;
import android.text.TextUtils;
import android.util.Log;

import org.jacobvv.libsamples.utils.FileUtils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * @author yinhui
 */
@SuppressWarnings({"WeakerAccess", "unused", "SameParameterValue", "ObsoleteSdkInt"})
public class Luban {

    private static final String TAG = "Luban";
    private static final String DEFAULT_CACHE_DIR = "luban_cache";

    private File mTargetDir;
    private int mLimitSize = 100;
    private List<String> mImages;
    private int mQuality = 60;

    private Luban(Builder builder) {
        this.mTargetDir = builder.mTargetDir;
        this.mImages = builder.mImages;
        this.mLimitSize = builder.mLimitSize;
        this.mQuality = builder.mQuality;
    }

    private Luban() {
    }

    public static Builder with(Context context) {
        return new Builder(context);
    }

    @WorkerThread
    public static boolean compress(String src, String dest, int quality) {
        return !(TextUtils.isEmpty(src) || TextUtils.isEmpty(dest))
                && compress(new File(src), new File(dest), quality);
    }

    @WorkerThread
    public static boolean compress(File src, File dest, int quality) {
        if (!src.isFile() && !FileUtils.delete(dest)) {
            Log.e(TAG, "Source is not a file OR destination access FAILED!");
            return false;
        }
        try {
            new Engine(src, dest, quality).compress();
            return true;
        } catch (IOException e) {
            Log.e(TAG, "Compress failed: " + e.getMessage());
            return false;
        }
    }
//
//    @WorkerThread
//    public static Observable<File> compressAsync(String src, String dest, int quality) {
//        if (TextUtils.isEmpty(src) || TextUtils.isEmpty(dest)) {
//            return Observable.error(new RuntimeException("Source or destination is empty!"));
//        } else {
//            return compressAsync(new File(src), new File(dest), quality);
//        }
//    }
//
//    @WorkerThread
//    public static Observable<File> compressAsync(final File src, final File dest, final int quality) {
//        return Observable.create(new Observable.OnSubscribe<File>() {
//            @Override
//            public void call(Subscriber<? super File> subscriber) {
//                subscriber.onStart();
//                if (!src.isFile() && !FileUtils.delete(dest)) {
//                    subscriber.onError(new RuntimeException(
//                            "Source is not a file OR destination access FAILED!"));
//                }
//                try {
//                    subscriber.onNext(new Engine(src, dest, quality).compress());
//                } catch (IOException e) {
//                    subscriber.onError(e);
//                }
//                subscriber.onCompleted();
//            }
//        }).subscribeOn(Schedulers.io());
//    }

    /**
     * Returns a file with a cache image name in the private cache directory.
     */
    private File getImageCacheFile(File targetDir) throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmssSSS", Locale.getDefault())
                .format(new Date());
        String imageFileName = "phi_img_" + timeStamp + "_compress";
        return File.createTempFile(imageFileName, ".jpg", targetDir);
    }

    @Nullable
    private static File getImageCacheDir(Context context) {
        File result = new File(context.getCacheDir(), DEFAULT_CACHE_DIR);
        if (!FileUtils.createOrExistDir(result, false, true)) {
            Log.e(TAG, "Internal cache directory prepare FAILED!");
            return null;
        }
        return result;
    }

    @WorkerThread
    public List<File> internalCompress(Context context) throws IOException {
        if (mTargetDir == null) {
            mTargetDir = getImageCacheDir(context);
        } else if (!FileUtils.createOrExistDir(mTargetDir, false, true)) {
            mTargetDir = null;
        }
        if (mTargetDir == null) {
            Log.e(TAG, "Target path prepare FAILED!");
            throw new IOException("Target path prepare FAILED!");
        }
        List<File> result = new ArrayList<>(mImages.size());
        for (String image : mImages) {
            result.add(internalCompress(new File(image), context));
        }
        return result;
    }

    @WorkerThread
    public File internalCompress(File image, Context context) throws IOException {
        if (mTargetDir == null) {
            mTargetDir = getImageCacheDir(context);
        } else if (!FileUtils.createOrExistDir(mTargetDir, false, true)) {
            mTargetDir = null;
        }
        if (mTargetDir == null) {
            Log.e(TAG, "Target path prepare FAILED!");
            throw new IOException("Target path prepare FAILED!");
        }
        File result;
        File outFile = getImageCacheFile(mTargetDir);
        if (Checker.needCompress(mLimitSize, image)) {
            result = new Engine(image, outFile, mQuality).compress();
        } else {
            result = image;
        }
        return result;
    }

    public static class Builder {
        private Context context;
        private File mTargetDir;
        private int mLimitSize = 100;
        private List<String> mImages;
        public int mQuality = 60;

        Builder(Context context) {
            this.context = context;
            this.mImages = new ArrayList<>();
        }

        private Luban build() {
            return new Luban(this);
        }

        public Builder load(String... images) {
            mImages.addAll(Arrays.asList(images));
            return this;
        }

        public Builder load(List<String> images) {
            mImages.addAll(images);
            return this;
        }

        public Builder load(File file) {
            mImages.add(file.getAbsolutePath());
            return this;
        }

        public Builder load(Uri uri) {
            mImages.add(uri.getPath());
            return this;
        }

        public Builder setTargetDir(String targetDir) {
            this.mTargetDir = new File(targetDir);
            return this;
        }

        public Builder setTargetDir(File targetDir) {
            this.mTargetDir = targetDir;
            return this;
        }

        /**
         * do not internalCompress when the origin image file size less than one value
         *
         * @param size the value of file size, unit KB, default 100K
         */
        public Builder ignoreBy(int size) {
            this.mLimitSize = size;
            return this;
        }

        public Builder setQuality(int quality) {
            this.mQuality = quality;
            return this;
        }

        @WorkerThread
        public List<File> compress() throws IOException {
            return build().internalCompress(context);
        }
//
//        public Observable<List<File>> compressAsync() {
//            return Observable.create(new Observable.OnSubscribe<List<File>>() {
//                @Override
//                public void call(Subscriber<? super List<File>> subscriber) {
//                    subscriber.onStart();
//                    try {
//                        subscriber.onNext(build().internalCompress(context));
//                    } catch (IOException e) {
//                        subscriber.onError(e);
//                    }
//                    subscriber.onCompleted();
//                }
//            }).subscribeOn(Schedulers.io());
//        }
    }
}