package org.jacobvv.utils.luban;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.support.media.ExifInterface;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Responsible for starting internalCompress and managing active and cached resources.
 */
class Engine {
    private ExifInterface srcExif;
    private File src;
    private File dest;
    private int mQuality;
    private int srcWidth;
    private int srcHeight;

    Engine(File src, File dest, int quality) throws IOException {
        if (Checker.isJpg(src.getPath())) {
            this.srcExif = new ExifInterface(src.getPath());
        }
        this.mQuality = quality;
        this.dest = dest;
        this.src = src;

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        options.inSampleSize = 1;

        BitmapFactory.decodeFile(src.getPath(), options);
        this.srcWidth = options.outWidth;
        this.srcHeight = options.outHeight;
    }

    private int computeSize() {
        srcWidth = srcWidth % 2 == 1 ? srcWidth + 1 : srcWidth;
        srcHeight = srcHeight % 2 == 1 ? srcHeight + 1 : srcHeight;

        int longSide = Math.max(srcWidth, srcHeight);
        int shortSide = Math.min(srcWidth, srcHeight);

        float scale = ((float) shortSide / longSide);
        if (scale <= 1 && scale > 0.5625) {
            if (longSide < 1664) {
                return 1;
            } else if (longSide < 4990) {
                return 2;
            } else if (longSide > 4990 && longSide < 10240) {
                return 4;
            } else {
                return longSide < 1280 ? 1 : longSide / 1280;
            }
        } else if (scale <= 0.5625 && scale > 0.5) {
            return longSide / 1280 == 0 ? 1 : longSide / 1280;
        } else {
            return (int) Math.ceil(longSide / (1280.0 / scale));
        }
    }

    private Bitmap rotatingImage(Bitmap bitmap) {
        if (srcExif == null) {
            return bitmap;
        }

        Matrix matrix = new Matrix();
        int angle = 0;
        int orientation = srcExif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
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

    File compress() throws IOException {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = computeSize();

        Bitmap tagBitmap = BitmapFactory.decodeFile(src.getPath(), options);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();

        tagBitmap = rotatingImage(tagBitmap);
        tagBitmap.compress(Bitmap.CompressFormat.JPEG, mQuality, stream);
        tagBitmap.recycle();

        FileOutputStream fos = new FileOutputStream(dest);
        fos.write(stream.toByteArray());
        fos.flush();
        fos.close();
        stream.close();

        return dest;
    }
}