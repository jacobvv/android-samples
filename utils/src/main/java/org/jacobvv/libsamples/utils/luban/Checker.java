package org.jacobvv.libsamples.utils.luban;

import android.text.TextUtils;

import java.io.File;

final class Checker {

    private static final String JPG = "jpg";
    private static final String JPEG = "jpeg";

    private Checker() {
        throw new RuntimeException("Utils CANNOT be instantiated!");
    }

    static boolean isJpg(String path) {
        if (TextUtils.isEmpty(path)) {
            return false;
        }
        String suffix = path.substring(path.lastIndexOf(".")).toLowerCase();
        return suffix.contains(JPG) || suffix.contains(JPEG);
    }

    static boolean needCompress(int minSize, File file) {
        return minSize <= 0 || file.exists() && file.length() > (minSize << 10);
    }
}
