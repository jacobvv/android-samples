package org.jacobvv.utils;

import android.annotation.SuppressLint;
import android.support.annotation.WorkerThread;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author yinhui
 * @date 18-5-25
 */
@SuppressWarnings({"WeakerAccess", "unused", "SameParameterValue", "ObsoleteSdkInt"})
public final class FileUtils {

    private static final String TAG = FileUtils.class.getSimpleName();

    public static final String HASH_MD5 = "MD5";
    public static final String HASH_SHA1 = "SHA-1";
    public static final String HASH_SHA256 = "SHA-256";

    private static final int BUFFER_SIZE = 256 * 1024;

    /**
     * 0.9KB
     */
    private static final long READABLE_K = 921;
    /**
     * 0.9MB
     */
    private static final long READABLE_M = 943718;
    /**
     * 0.9GB
     */
    private static final long READABLE_G = 966367642;

    /**
     * Don't let anyone instantiate this class.
     */
    private FileUtils() {
        throw new UnsupportedOperationException("Utils CANNOT be instantiated!");
    }

    /**
     * 判断是否存在
     *
     * @param path 目标路径
     * @return 如果存在返回true，否则返回false
     */
    public static boolean exists(String path) {
        return !TextUtils.isEmpty(path) && new File(path).exists();
    }

    /**
     * 判断是否存在
     *
     * @param path 目标路径
     * @return 如果存在返回true，否则返回false
     */
    public static boolean exists(File path) {
        return path != null && path.exists();
    }

    /**
     * 判断是否是文件
     *
     * @param path 目标路径
     * @return 是文件返回true，否则返回false
     */
    public static boolean isFile(String path) {
        return !TextUtils.isEmpty(path) && new File(path).isFile();
    }

    /**
     * 判断是否是文件
     *
     * @param file 目标路径
     * @return 是文件返回true，否则返回false
     */
    public static boolean isFile(File file) {
        return file != null && file.isFile();
    }

    /**
     * 判断是否是目录
     *
     * @param path 目标路径
     * @return 是目录返回true， 否则返回false
     */
    public static boolean isDir(String path) {
        return !TextUtils.isEmpty(path) && new File(path).isDirectory();
    }

    /**
     * 判断是否是目录
     *
     * @param path 目标路径
     * @return 是目录返回true， 否则返回false
     */
    public static boolean isDir(File path) {
        return path != null && path.isDirectory();
    }

    /**
     * 创建目录
     *
     * @param path         目标路径
     * @param clearIfDir   如果目标存在目录，是否清空目录中的所有子项
     * @param deleteIfFile 如果目标是文件，是否删除
     * @return 目录已存在或者创建成功
     */
    public static boolean createOrExistDir(String path, boolean clearIfDir, boolean deleteIfFile) {
        return !TextUtils.isEmpty(path) && createOrExistDir(new File(path), clearIfDir, deleteIfFile);
    }

    /**
     * 创建目录
     *
     * @param path         目标路径
     * @param clearIfDir   如果目标存在目录，是否清空目录中的所有子项
     * @param deleteIfFile 如果目标是文件，是否删除
     * @return 目录已存在或者创建成功
     */
    public static boolean createOrExistDir(File path, boolean clearIfDir, boolean deleteIfFile) {
        if (path.isFile()) {
            return deleteIfFile && path.delete() && path.mkdirs();
        } else if (path.isDirectory()) {
            return !clearIfDir || clear(path);
        } else {
            return path.mkdirs();
        }
    }

    /**
     * 创建文件
     *
     * @param path         文件路径
     * @param deleteIfDir  如果目标是目录，是否删除
     * @param deleteIfFile 如果目标已存在文件，是否删除
     * @return 文件已存在或创建成功
     */
    public static boolean createOrExistFile(String path, boolean deleteIfDir, boolean deleteIfFile) {
        return !TextUtils.isEmpty(path) && createOrExistFile(new File(path), deleteIfDir, deleteIfFile);
    }

    /**
     * 创建文件
     *
     * @param path         文件路径
     * @param deleteIfDir  如果目标是目录，是否删除
     * @param deleteIfFile 如果目标已存在文件，是否删除
     * @return 文件已存在或创建成功
     */
    public static boolean createOrExistFile(File path, boolean deleteIfDir, boolean deleteIfFile) {
        boolean result;
        if (path.isFile()) {
            if (!deleteIfFile) {
                return true;
            }
            result = delete(path);
        } else if (path.isDirectory()) {
            result = deleteIfDir && delete(path);
        } else {
            result = createOrExistDir(path.getParentFile(), false, false);
        }
        if (!result) {
            return false;
        }
        try {
            return path.createNewFile();
        } catch (IOException e) {
            Log.e(TAG, "Create file failed." + e.getMessage());
            return false;
        }
    }

    /**
     * 重命名文件或路径
     *
     * @param src       The path of file.
     * @param newName   The new name of file.
     * @param overwrite 是否覆盖目标文件或路径
     * @return {@code true}: success<br>{@code false}: fail
     */
    public static boolean rename(final String src, final String newName, boolean overwrite) {
        return !TextUtils.isEmpty(src) && rename(new File(src), newName, overwrite);
    }

    /**
     * 重命名文件或目录
     *
     * @param src       The file.
     * @param newName   The new name of file.
     * @param overwrite 是否覆盖目标文件或目录
     * @return {@code true}: success<br>{@code false}: fail
     */
    public static boolean rename(final File src, final String newName, boolean overwrite) {
        // file is null then return false
        if (src == null || !src.exists() || TextUtils.isEmpty(newName)) {
            return false;
        }
        // the new name equals old name then return true
        if (newName.equals(src.getName())) {
            return true;
        }
        File newFile = new File(src.getParent() + File.separator + newName);
        // the new name of file
        if (overwrite && newFile.exists()) {
            delete(newFile);
        }
        // the new name of file exists then return false
        return !newFile.exists() && src.renameTo(newFile);
    }

    /**
     * 移动文件或目录
     *
     * @param src       源文件或目录
     * @param dest      目标文件或目录
     * @param overwrite 是否覆盖，包括子项
     * @param merge     如果是目录，那么是否合并
     * @return 完全移动成功则为true，否则为false
     * <p>
     * 例：
     * 1. 移动一个文件或目录，如果目标存在（不管是文件还是目录）则覆盖
     * move(src, dest, true, false)
     * 2. 移动一个目录，如果目标是文件则覆盖，如果目标是目录则合并，子项使用相同逻辑
     * move(src, dest, true, true)
     * 3. 移动一个目录，如果目标是文件则放弃，如果目标是目录则合并，遇到冲突则放弃
     * move(src, dest, false, true)
     * 4. 移动一个文件或目录，如果目标存在则放弃
     * move(src, dest, false, false)
     */
    public static boolean move(String src, String dest, boolean overwrite, boolean merge) {
        return !TextUtils.isEmpty(src) && !TextUtils.isEmpty(dest) &&
                move(new File(src), new File(dest), overwrite, merge);
    }

    /**
     * 移动文件或目录
     *
     * @param src       源文件或目录
     * @param dest      目标文件或目录
     * @param overwrite 是否覆盖，包括子项
     * @param merge     如果是目录，那么是否合并
     * @return 完全移动成功则为true，否则为false
     * <p>
     * 例：
     * 1. 移动一个文件或目录，如果目标存在（不管是文件还是目录）则覆盖
     * move(src, dest, true, false)
     * 2. 移动一个目录，如果目标是文件则覆盖，如果目标是目录则合并，子项使用相同逻辑
     * move(src, dest, true, true)
     * 3. 移动一个目录，如果目标是文件则放弃，如果目标是目录则合并，遇到冲突则放弃
     * move(src, dest, false, true)
     * 4. 移动一个文件或目录，如果目标存在则放弃
     * move(src, dest, false, false)
     */
    public static boolean move(File src, File dest, boolean overwrite, boolean merge) {
        // file is null then return false
        if (src == null || !src.exists() || dest == null) {
            return false;
        }
        // 如果目标路径不存在，直接移动
        if (!dest.exists()) {
            return src.renameTo(dest);
        }
        // 如果源路径或目标路径是文件，或者目标文件夹不允许合并，那么只取决于overwrite是否删除后移动
        boolean needOverwrite = src.isFile() || dest.isFile() || (!merge && dest.isDirectory());
        if (needOverwrite) {
            return overwrite && delete(dest) && src.renameTo(dest);
        }
        // 如果目标路径是文件夹，并且允许合并，那么合并文件夹，子路径冲突逻辑继承
        String[] children = src.list();
        boolean success = true;
        for (String child : children) {
            File srcChild = new File(src, child);
            File destChild = new File(dest, child);
            success &= move(srcChild, destChild, overwrite, merge);
        }
        if (src.list().length == 0) {
            success &= src.delete();
        }
        return success;
    }

    /**
     * 复制文件或目录
     * 注意：必须异步执行
     *
     * @param src       源文件或目录
     * @param dest      目标文件或目录
     * @param overwrite 是否覆盖，包括子项
     * @param merge     如果是目录，那么是否合并
     * @return 完全复制成功则为true，否则为false
     * <p>
     * 例：
     * 1. 复制一个文件或目录，如果目标存在（不管是文件还是目录）则覆盖
     * move(src, dest, true, false)
     * 2. 复制一个目录，如果目标是文件则覆盖，如果目标是目录则合并，子项使用相同逻辑
     * move(src, dest, true, true)
     * 3. 复制一个目录，如果目标是文件则放弃，如果目标是目录则合并，遇到冲突则放弃
     * move(src, dest, false, true)
     * 4. 复制一个文件或目录，如果目标存在则放弃
     * move(src, dest, false, false)
     */
    @WorkerThread
    public static boolean copy(String src, String dest, boolean overwrite, boolean merge) {
        return !TextUtils.isEmpty(src) && !TextUtils.isEmpty(dest) &&
                copy(new File(src), new File(dest), overwrite, merge);
    }

    /**
     * 复制文件或目录
     * 注意：必须异步执行
     *
     * @param src       源文件或目录
     * @param dest      目标文件或目录
     * @param overwrite 是否覆盖，包括子项
     * @param merge     如果是目录，那么是否合并
     * @return 完全复制成功则为true，否则为false
     * <p>
     * 例：
     * 1. 复制一个文件或目录，如果目标存在（不管是文件还是目录）则覆盖
     * move(src, dest, true, false)
     * 2. 复制一个目录，如果目标是文件则覆盖，如果目标是目录则合并，子项使用相同逻辑
     * move(src, dest, true, true)
     * 3. 复制一个目录，如果目标是文件则放弃，如果目标是目录则合并，遇到冲突则放弃
     * move(src, dest, false, true)
     * 4. 复制一个文件或目录，如果目标存在则放弃
     * move(src, dest, false, false)
     */
    @WorkerThread
    public static boolean copy(File src, File dest, boolean overwrite, boolean merge) {
        // file is null then return false
        if (src == null || !src.exists() || dest == null) {
            return false;
        }
        // 如果目标路径不存在，直接复制
        if (!dest.exists()) {
            return copy(src, dest);
        }
        // 如果源路径或目标路径是文件，或者目标文件夹不允许合并，那么只取决于overwrite是否删除后复制
        boolean needOverwrite = src.isFile() || dest.isFile() || (!merge && dest.isDirectory());
        if (needOverwrite) {
            return overwrite && delete(dest) && copy(src, dest);
        }
        // 如果目标路径是文件夹，并且允许合并，那么合并文件夹，子路径冲突逻辑继承
        String[] children = src.list();
        boolean success = true;
        for (String child : children) {
            File srcChild = new File(src, child);
            File destChild = new File(dest, child);
            success &= copy(srcChild, destChild, overwrite, merge);
        }
        return success;
    }

    /**
     * 复制文件或目录
     * 注意：必须异步执行
     *
     * @param src  源文件或目录
     * @param dest 目标路径
     * @return 源文件或目录存在，目标路径不存在，并且复制成功则返回true，否则返回false
     */
    @WorkerThread
    public static boolean copy(String src, String dest) {
        return !TextUtils.isEmpty(src) && !TextUtils.isEmpty(dest) &&
                copy(new File(src), new File(dest));
    }

    /**
     * 复制文件或目录
     * 注意：必须异步执行
     *
     * @param src  源文件或目录
     * @param dest 目标路径
     * @return 源文件或目录存在，目标路径不存在，并且复制成功则返回true，否则返回false
     */
    @WorkerThread
    public static boolean copy(File src, File dest) {
        if (src == null || !src.exists() || dest.exists()) {
            return false;
        }
        if (src.isFile()) {
            BufferedInputStream is = null;
            BufferedOutputStream os = null;
            try {
                is = new BufferedInputStream(new FileInputStream(src),
                        BUFFER_SIZE);
                os = new BufferedOutputStream(new FileOutputStream(dest),
                        BUFFER_SIZE);
                byte[] buffer = new byte[BUFFER_SIZE];
                int len;
                while ((len = is.read(buffer, 0, BUFFER_SIZE)) != -1) {
                    os.write(buffer, 0, len);
                }
            } catch (IOException e) {
                Log.e(TAG, "Copy file failed. " + e.getMessage());
                return false;
            } finally {
                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException e) {
                        Log.e(TAG, "Copy file failed. " + e.getMessage());
                    }
                }
                if (os != null) {
                    try {
                        os.close();
                    } catch (IOException e) {
                        Log.e(TAG, "Copy file failed. " + e.getMessage());
                    }
                }
            }
            return true;
        } else {
            if (!dest.mkdirs()) {
                return false;
            }
            String[] list = src.list();
            for (String child : list) {
                File srcChild = new File(src, child);
                File destChild = new File(dest, child);
                if (!copy(srcChild, destChild)) {
                    return false;
                }
            }
            return true;
        }
    }

    /**
     * 删除文件或目录
     *
     * @param path 要删除的文件或者目录
     * @return 是否删除成功
     */
    public static boolean delete(String path) {
        return !TextUtils.isEmpty(path) && delete(new File(path));
    }

    /**
     * 删除文件或目录
     *
     * @param path 要删除的文件或者目录
     * @return 是否删除成功
     */
    public static boolean delete(File path) {
        if (path.isFile()) {
            return path.delete();
        } else if (path.isDirectory()) {
            File[] children = path.listFiles();
            for (File child : children) {
                if (!delete(child)) {
                    return false;
                }
            }
            return path.delete();
        } else {
            return true;
        }
    }

    /**
     * 清空目录
     *
     * @param path 要清空的目录
     * @return 是否删除成功
     */
    public static boolean clear(String path) {
        return !TextUtils.isEmpty(path) && clear(new File(path));
    }

    /**
     * 清空目录
     *
     * @param path 要清空的目录
     * @return 是否删除成功
     */
    public static boolean clear(File path) {
        if (path.isFile()) {
            return false;
        } else if (path.isDirectory()) {
            File[] children = path.listFiles();
            for (File child : children) {
                boolean success = delete(child);
                if (!success) {
                    return false;
                }
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * 获取一个文件的MD5字符串值
     *
     * @param file 指定文件
     * @return MD5值的字节数组
     */
    public static String getMd5String(final String file) {
        return getHashString(file, HASH_MD5);
    }

    /**
     * 获取一个文件的MD5字符串值
     *
     * @param file 指定文件
     * @return MD5值的字节数组
     */
    public static String getMd5String(final File file) {
        return getHashString(file, HASH_MD5);
    }

    /**
     * 获取一个文件的HASH字符串值
     *
     * @param file 指定文件
     * @return MD5值的字节数组
     */
    public static String getHashString(final String file, String algorithm) {
        if (TextUtils.isEmpty(file)) {
            LogUtils.e(TAG, "(getHashString) File is null.");
            return null;
        }
        return getHashString(new File(file), algorithm);
    }

    /**
     * 获取一个文件的HASH字符串值
     *
     * @param file 指定文件
     * @return MD5值的字节数组
     */
    public static String getHashString(final File file, String algorithm) {
        byte[] hash = getHash(file, algorithm);
        return StringUtils.byte2Hex(hash);
    }

    /**
     * 获取一个文件的MD5值
     *
     * @param file 指定文件
     * @return MD5值的字节数组
     */
    public static byte[] getMd5(final String file) {
        return getHash(file, HASH_MD5);
    }

    /**
     * 获取一个文件的MD5值
     *
     * @param file 指定文件
     * @return MD5值的字节数组
     */
    public static byte[] getMd5(final File file) {
        return getHash(file, HASH_MD5);
    }

    /**
     * 获取一个文件的HASH值
     *
     * @param file 指定文件
     * @return MD5值的字节数组
     */
    public static byte[] getHash(final String file, String algorithm) {
        if (TextUtils.isEmpty(file)) {
            LogUtils.e(TAG, "(getHash) File is null.");
            return null;
        }
        return getHash(new File(file), algorithm);
    }

    /**
     * 获取一个文件的HASH值
     *
     * @param file 指定文件
     * @return MD5值的字节数组
     */
    public static byte[] getHash(final File file, String algorithm) {
        if (file == null || !file.isFile()) {
            LogUtils.e(TAG, "(getHash) File is null.");
            return null;
        }
        DigestInputStream dis = null;
        try {
            FileInputStream fis = new FileInputStream(file);
            MessageDigest md = MessageDigest.getInstance(algorithm);
            dis = new DigestInputStream(fis, md);
            byte[] buffer = new byte[BUFFER_SIZE];
            while (true) {
                if (dis.read(buffer) < 0) {
                    break;
                }
            }
            md = dis.getMessageDigest();
            return md.digest();
        } catch (NoSuchAlgorithmException e) {
            LogUtils.e(TAG, "(getHash) No such algorithm: " + algorithm);
        } catch (IOException e) {
            LogUtils.e(TAG, "(getHash) File not found or open file failed. "
                    + e.getMessage());
        } finally {
            try {
                if (dis != null) {
                    dis.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 获取可读的目录大小
     *
     * @param path The file or directory.
     * @return the length of path, if not exist, return 0.
     */
    public static String getSizeReadable(final String path) {
        if (path == null) {
            return "";
        }
        return getSizeReadable(new File(path));
    }

    /**
     * 获取可读的目录大小
     *
     * @param path The file or directory.
     * @return the length of path, if not exist, return 0.
     */
    public static String getSizeReadable(final File path) {
        return long2ReadableSize(getSize(path));
    }

    /**
     * Return the length of file or directory.
     *
     * @param path The file or directory.
     * @return the length of path, if not exist, return 0.
     */
    public static long getSize(final String path) {
        if (path == null) {
            return 0;
        }
        return getSize(new File(path));
    }

    /**
     * Return the length of file or directory.
     *
     * @param path The file or directory.
     * @return the length of path, if not exist, return 0.
     */
    public static long getSize(final File path) {
        if (!exists(path)) {
            return 0;
        }
        if (path.isFile()) {
            return path.length();
        } else {
            long size = 0;
            File[] files = path.listFiles();
            for (File file : files) {
                size += getSize(file);
            }
            return size;
        }
    }

    @SuppressLint("DefaultLocale")
    private static String long2ReadableSize(final long byteNum) {
        if (byteNum < 0) {
            LogUtils.e(TAG, "(long2ReadableSize) File size must above zero.");
            return "";
        } else if (byteNum < READABLE_K) {
            return String.format("%dB", byteNum);
        } else if (byteNum < READABLE_M) {
            return String.format("%.3fKB", (double) byteNum / 1024);
        } else if (byteNum < READABLE_G) {
            return String.format("%.3fMB", (double) byteNum / 1048576);
        } else {
            return String.format("%.3fGB", (double) byteNum / 1073741824);
        }
    }
}
