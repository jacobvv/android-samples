package org.jacobvv.utils;

import android.util.Base64;
import android.util.Log;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author yinhui
 * @date 18-6-21
 */
@SuppressWarnings({"WeakerAccess", "unused", "SameParameterValue", "ObsoleteSdkInt"})
public final class StringUtils {

    private static final String TAG = StringUtils.class.getSimpleName();

    public static final String HASH_MD5 = "MD5";
    public static final String HASH_SHA1 = "SHA-1";
    public static final String HASH_SHA256 = "SHA-256";

    private static final char[] HEX_DIGITS =
            {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    private static final int DOUBLE = 2;
    private static final int QUADRUPLE = 4;
    private static final char HEX_FIRST_NUM = '0';
    private static final char HEX_LAST_NUM = '9';
    private static final char HEX_FIRST_LETTER = 'a';
    private static final char HEX_LAST_LETTER = 'f';

    private static final String REG_NICKNAME = "^[\\u4e00-\\u9fa5_a-zA-Z0-9]+$";

    private static final String REG_PHONE = "^1[3456789]\\d{9}$";

    private static final String REG_PASSWORD =
            "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d\\W_]+$" +
                    "|^(?=.*[A-Za-z])(?=.*\\W_)[A-Za-z\\d\\W_]+$" +
                    "|^(?=.*[\\W_])(?=.*\\d)[A-Za-z\\d\\W_]+$";

    private StringUtils() {
        throw new UnsupportedOperationException("Utils CANNOT be instantiated!");
    }

    /**
     * 判断昵称是否合格，只能是中文，字母，数字，下划线
     *
     * @param name 昵称
     * @param min  最小昵称长度
     * @param max  最大昵称长度
     * @return 是否合格
     */
    public static boolean isNicknameValid(String name, int min, int max) {
        if (CheckUtils.isEmpty(name)) {
            LogUtils.e(TAG, "Name is empty.");
            return false;
        }
        if (min <= 0 || min > max) {
            LogUtils.e(TAG, "Range of name is invalid: min=" + min + "; max=" + max);
            return false;
        }
        return name.length() >= min && name.length() <= max && name.matches(REG_NICKNAME);
    }

    /**
     * 判断国内手机号码格式是否合格
     *
     * @param phone 手机号码
     * @return 是否合格
     */
    public static boolean isPhoneValid(String phone) {
        if (CheckUtils.isEmpty(phone)) {
            LogUtils.e(TAG, "Phone number is empty.");
            return false;
        }
        return phone.matches(REG_PHONE);
    }

    /**
     * 判断密码格式是否合格，必须包括字母，数字，特殊符号其中两种
     *
     * @param password 密码
     * @param min      最小密码长度
     * @param max      最大密码长度
     * @return 是否合格
     */
    public static boolean isPasswordValid(String password, int min, int max) {
        if (CheckUtils.isEmpty(password)) {
            LogUtils.e(TAG, "Password is empty.");
            return false;
        }
        if (min <= 0 || min > max) {
            LogUtils.e(TAG, "Range of password is invalid: min=" + min + "; max=" + max);
            return false;
        }
        return password.length() >= min && password.length() <= max && password.matches(REG_PASSWORD);
    }

    /**
     * 将字符串按十进制解析为byte型数
     *
     * @param src 源字符串
     * @return 解析结果，如果格式错误，默认返回0
     */
    public static byte toByte(String src) {
        return toByte(src, 10, (byte) 0);
    }

    /**
     * 将字符串按十进制解析为byte型数
     *
     * @param src      源字符串
     * @param defValue 默认值
     * @return 解析结果，如果格式错误，默认返回给定的默认值
     */
    public static byte toByte(String src, byte defValue) {
        return toByte(src, 10, defValue);
    }

    /**
     * 将字符串按给定进制解析为byte型数
     *
     * @param src      源字符串
     * @param radix    进制
     * @param defValue 默认值
     * @return 解析结果，如果格式错误，默认返回给定的默认值
     */
    public static byte toByte(String src, int radix, byte defValue) {
        if (CheckUtils.isEmpty(src)) {
            LogUtils.e(TAG, "Number string is empty.");
            return defValue;
        }
        try {
            return Byte.parseByte(src, radix);
        } catch (NumberFormatException e) {
            LogUtils.e(TAG, "Number string to byte format error: " + src);
            return defValue;
        }
    }

    /**
     * 将字符串按十进制解析为int型数
     *
     * @param src 源字符串
     * @return 解析结果，如果格式错误，默认返回0
     */
    public static int toInt(String src) {
        return toInt(src, 10, 0);
    }

    /**
     * 将字符串按十进制解析为int型数
     *
     * @param src      源字符串
     * @param defValue 默认值
     * @return 解析结果，如果格式错误，默认返回给定的默认值
     */
    public static int toInt(String src, int defValue) {
        return toInt(src, 10, defValue);
    }

    /**
     * 将字符串按给定进制解析为int型数
     *
     * @param src      源字符串
     * @param radix    进制
     * @param defValue 默认值
     * @return 解析结果，如果格式错误，默认返回给定的默认值
     */
    public static int toInt(String src, int radix, int defValue) {
        if (CheckUtils.isEmpty(src)) {
            LogUtils.e(TAG, "Number string is empty.");
            return defValue;
        }
        try {
            return Integer.parseInt(src, radix);
        } catch (NumberFormatException e) {
            LogUtils.e(TAG, "Number string to int format error: " + src);
            return defValue;
        }
    }

    /**
     * 将字符串按十进制解析为long型数
     *
     * @param src 源字符串
     * @return 解析结果，如果格式错误，默认返回0
     */
    public static long toLong(String src) {
        return toLong(src, 10, 0);
    }

    /**
     * 将字符串按十进制解析为long型数
     *
     * @param src      源字符串
     * @param defValue 默认值
     * @return 解析结果，如果格式错误，默认返回给定的默认值
     */
    public static long toLong(String src, long defValue) {
        return toLong(src, 10, defValue);
    }

    /**
     * 将字符串按给定进制解析为long型数
     *
     * @param src      源字符串
     * @param radix    进制
     * @param defValue 默认值
     * @return 解析结果，如果格式错误，默认返回给定的默认值
     */
    public static long toLong(String src, int radix, long defValue) {
        if (CheckUtils.isEmpty(src)) {
            LogUtils.e(TAG, "Number string is empty.");
            return defValue;
        }
        try {
            return Long.parseLong(src, radix);
        } catch (NumberFormatException e) {
            LogUtils.e(TAG, "Number string to long format error: " + src);
            return defValue;
        }
    }

    /**
     * 将字符串解析为float型浮点数
     *
     * @param src 源字符串
     * @return 解析结果，如果格式错误，默认返回0
     */
    public static float toFloat(String src) {
        return toFloat(src, 0);
    }

    /**
     * 将字符串解析为float型浮点数
     *
     * @param src      源字符串
     * @param defValue 默认值
     * @return 解析结果，如果格式错误，默认返回给定的默认值
     */
    public static float toFloat(String src, float defValue) {
        if (CheckUtils.isEmpty(src)) {
            LogUtils.e(TAG, "Number string is empty.");
            return defValue;
        }
        try {
            return Float.valueOf(src);
        } catch (NumberFormatException e) {
            LogUtils.e(TAG, "Number string to float format error: " + src);
            return defValue;
        }
    }

    /**
     * 将字符串解析为double型浮点数
     *
     * @param src 源字符串
     * @return 解析结果，如果格式错误，默认返回0
     */
    public static double toDouble(String src) {
        return toDouble(src, 0);
    }

    /**
     * 将字符串解析为double型浮点数
     *
     * @param src      源字符串
     * @param defValue 默认值
     * @return 解析结果，如果格式错误，默认返回给定的默认值
     */
    public static double toDouble(String src, double defValue) {
        if (CheckUtils.isEmpty(src)) {
            LogUtils.e(TAG, "Number string is empty.");
            return defValue;
        }
        try {
            return Double.valueOf(src);
        } catch (NumberFormatException e) {
            LogUtils.e(TAG, "Number string to double format error: " + src);
            return defValue;
        }
    }

    /**
     * 获取数据的Base64编码
     *
     * @param src 输入数据
     * @return Base64编码结果数据
     */
    public static byte[] base64Encode(final byte[] src) {
        return Base64.encode(src, Base64.NO_WRAP);
    }

    /**
     * 解码给定的Base64数据
     *
     * @param src Base64数据
     * @return 解码结果数据
     */
    public static byte[] base64Decode(final byte[] src) {
        return Base64.decode(src, Base64.NO_WRAP);
    }

    /**
     * 获取十六进制数据的Base64编码，结果以十六进制字符串形式返回
     *
     * @param hex 输入的十六进制字符串
     * @return Base64编码结果的十六进制字符串
     */
    public static String base64EncodeHex(final String hex) {
        return byte2Hex(Base64.encode(hex2Byte(hex), Base64.NO_WRAP));
    }

    /**
     * 解码给定的Base64数据的十六进制字符串，结果以十六进制字符串的形式返回
     *
     * @param hex Base64数据的十六进制字符串
     * @return 解码结果的十六进制字符串
     */
    public static String base64DecodeHex(final String hex) {
        return byte2Hex(Base64.decode(hex2Byte(hex), Base64.NO_WRAP));
    }

    /**
     * 获取数据的MD5编码
     *
     * @param src 输入数据
     * @return MD5编码数据
     */
    public static byte[] getMd5(byte[] src) {
        return getHash(src, HASH_MD5);
    }

    /**
     * 获取数据的SHA-1编码
     *
     * @param src 输入数据
     * @return SHA-1编码数据
     */
    public static byte[] getSha1(byte[] src) {
        return getHash(src, HASH_SHA1);
    }

    /**
     * 获取数据的SHA-256编码
     *
     * @param src 输入数据
     * @return SHA-256编码数据
     */
    public static byte[] getSha256(byte[] src) {
        return getHash(src, HASH_SHA256);
    }

    /**
     * 获取给定的十六进制数据的MD5编码，结果以十六进制字符串的形式返回
     *
     * @param hex 给定的十六进制数据
     * @return MD5编码的十六进制字符串
     */
    public static String getMd5(String hex) {
        return getHash(hex, HASH_MD5);
    }

    /**
     * 获取给定的十六进制数据的SHA-1编码，结果以十六进制字符串的形式返回
     *
     * @param hex 给定的十六进制数据
     * @return SHA-1编码的十六进制字符串
     */
    public static String getSha1(String hex) {
        return getHash(hex, HASH_SHA1);
    }

    /**
     * 获取给定的十六进制数据的SHA-256编码，结果以十六进制字符串的形式返回
     *
     * @param hex 给定的十六进制数据
     * @return SHA-256编码的十六进制字符串
     */
    public static String getSha256(String hex) {
        return getHash(hex, HASH_SHA256);
    }

    /**
     * 获取十六进制数据的HASH值
     *
     * @param hex 十六进制数据
     * @return HASH值的十六进制字符串
     */
    public static String getHash(final String hex, String algorithm) {
        if (CheckUtils.isEmpty(hex)) {
            return "";
        }
        return byte2Hex(getHash(hex2Byte(hex), algorithm));
    }

    /**
     * 获取数据的HASH值
     *
     * @param src 输入数据
     * @return MD5值的字节数组
     */
    public static byte[] getHash(final byte[] src, String algorithm) {
        if (src == null) {
            return null;
        }
        try {
            MessageDigest md = MessageDigest.getInstance(algorithm);
            md.update(src);
            return md.digest();
        } catch (NoSuchAlgorithmException e) {
            LogUtils.e(TAG, "(getHash) No such algorithm: " + algorithm);
        }
        return null;
    }

    /**
     * 字节数组转十六进制字符串
     *
     * @param bytes 源数据
     * @return 十六进制字符串
     */
    public static String byte2Hex(byte[] bytes) {
        if (bytes == null) {
            Log.e(TAG, "Bytes is null.");
            return "";
        }
        int len = bytes.length;
        if (len <= 0) {
            return "";
        }
        char[] ret = new char[len << 1];
        for (int i = 0, j = 0; i < len; i++) {
            ret[j++] = HEX_DIGITS[bytes[i] >>> 4 & 0x0f];
            ret[j++] = HEX_DIGITS[bytes[i] & 0x0f];
        }
        return new String(ret);
    }

    /**
     * 十六进制字符串转字节数组
     *
     * @param hex 源数据
     * @return 字节数组
     */
    public static byte[] hex2Byte(String hex) {
        if (CheckUtils.isEmpty(hex)) {
            return null;
        }
        int len = hex.length();
        if (len % DOUBLE != 0) {
            hex = "0" + hex;
            len = len + 1;
        }
        char[] hexBytes = hex.toLowerCase().toCharArray();
        byte[] ret = new byte[len >> 1];
        for (int i = 0; i < len; i += DOUBLE) {
            ret[i >> 1] = (byte) (hex2Dec(hexBytes[i]) << 4 | hex2Dec(hexBytes[i + 1]));
        }
        return ret;
    }

    /**
     * 字符串转十六进制字符串
     *
     * @param src 源数据
     * @return 十六进制字符串
     */
    public static String string2Hex(String src) {
        if (CheckUtils.isEmpty(src)) {
            return "";
        }
        int len = src.length();
        char[] ret = new char[len << 2];
        char[] chars = src.toCharArray();
        for (int i = 0, j = 0; i < len; i++) {
            ret[j++] = HEX_DIGITS[chars[i] >>> 12 & 0x0f];
            ret[j++] = HEX_DIGITS[chars[i] >>> 8 & 0x0f];
            ret[j++] = HEX_DIGITS[chars[i] >>> 4 & 0x0f];
            ret[j++] = HEX_DIGITS[chars[i] & 0x0f];
        }
        return new String(ret);
    }

    /**
     * 十六进制字符串转字符串
     *
     * @param hex 源数据
     * @return 字符串
     */
    public static String hex2String(String hex) {
        if (CheckUtils.isEmpty(hex)) {
            return "";
        }
        int len = hex.length();
        int zeroCountToAdd = len % QUADRUPLE;
        if (zeroCountToAdd != 0) {
            StringBuilder sb = new StringBuilder(hex);
            for (int i = 0; i < zeroCountToAdd; i++) {
                sb.insert(0, '0');
            }
            hex = sb.toString();
            len = len + zeroCountToAdd;
        }
        char[] hexChars = hex.toLowerCase().toCharArray();
        char[] ret = new char[len >> 2];
        for (int i = 0; i < len; i += QUADRUPLE) {
            ret[i >> 2] = (char) (int) (hex2Dec(hexChars[i]) << 12
                    | hex2Dec(hexChars[i + 1]) << 8
                    | hex2Dec(hexChars[i + 2]) << 4
                    | hex2Dec(hexChars[i + 3]));
        }
        return new String(ret);
    }

    private static int hex2Dec(final char hexChar) {
        if (hexChar >= HEX_FIRST_NUM && hexChar <= HEX_LAST_NUM) {
            return hexChar - HEX_FIRST_NUM;
        } else if (hexChar >= HEX_FIRST_LETTER && hexChar <= HEX_LAST_LETTER) {
            return hexChar - HEX_FIRST_LETTER + 10;
        } else {
            throw new IllegalArgumentException();
        }
    }

}
