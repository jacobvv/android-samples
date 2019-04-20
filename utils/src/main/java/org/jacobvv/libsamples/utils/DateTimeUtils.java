package org.jacobvv.libsamples.utils;

import android.support.annotation.NonNull;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * 日期时间工具方法类
 *
 * @author yinhui
 * @date 18-6-13
 */
@SuppressWarnings({"WeakerAccess", "unused", "SameParameterValue", "ObsoleteSdkInt",
        "SimpleDateFormat"})
public final class DateTimeUtils {

    private static final String TAG = DateTimeUtils.class.getSimpleName();

    private static final long TIME_MILLIS_DAY = 24 * 3600 * 1000;

    public static final DateFormat DEFAULT_FORMAT =
            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static final SimpleDateFormat SERVER_FORMAT =
            new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
    public static final SimpleDateFormat DATE_FORMAT =
            new SimpleDateFormat("yyyy-MM-dd");
    public static final SimpleDateFormat DATE_NO_YEAR_FORMAT =
            new SimpleDateFormat("MM-dd");
    public static final SimpleDateFormat YEAR_FORMAT =
            new SimpleDateFormat("yyyy");
    public static final SimpleDateFormat TIME_FORMAT =
            new SimpleDateFormat("HH:mm");

    private DateTimeUtils() {
        throw new UnsupportedOperationException("Utils CANNOT be instantiated!");
    }

    /**
     * Return the current time in milliseconds.
     *
     * @return the current time in milliseconds
     */
    public static long nowMillis() {
        return System.currentTimeMillis();
    }

    /**
     * Return the current formatted time string.
     * <p>The pattern is {@code yyyy-MM-dd HH:mm:ss}.</p>
     *
     * @return the current formatted time string
     */
    public static String nowString() {
        return millis2String(System.currentTimeMillis(), DEFAULT_FORMAT);
    }

    /**
     * Return the current formatted time string.
     *
     * @param format The format.
     * @return the current formatted time string
     */
    public static String nowString(@NonNull final DateFormat format) {
        return millis2String(System.currentTimeMillis(), format);
    }

    /**
     * Return the current date.
     *
     * @return the current date
     */
    public static Date nowDate() {
        return new Date();
    }

    /**
     * Milliseconds to the formatted time string.
     * <p>The pattern is {@code yyyy-MM-dd HH:mm:ss}.</p>
     *
     * @param millis The milliseconds.
     * @return the formatted time string
     */
    public static String millis2String(final long millis) {
        return millis2String(millis, DEFAULT_FORMAT);
    }

    /**
     * Milliseconds to the formatted time string.
     *
     * @param millis The milliseconds.
     * @param format The format.
     * @return the formatted time string
     */
    public static String millis2String(final long millis, @NonNull final DateFormat format) {
        return format.format(new Date(millis));
    }

    /**
     * Formatted time string to the milliseconds.
     * <p>The pattern is {@code yyyy-MM-dd HH:mm:ss}.</p>
     *
     * @param time The formatted time string.
     * @return the milliseconds
     */
    public static long string2Millis(final String time) {
        return string2Millis(time, DEFAULT_FORMAT);
    }

    /**
     * Formatted time string to the milliseconds.
     *
     * @param time   The formatted time string.
     * @param format The format.
     * @return the milliseconds
     */
    public static long string2Millis(final String time, @NonNull final DateFormat format) {
        if (CheckUtils.isEmpty(time)) {
            LogUtils.e(TAG, "Time string is empty.");
            return -1;
        }
        try {
            return format.parse(time).getTime();
        } catch (ParseException e) {
            LogUtils.e(TAG, "Time string format error! " + e.getMessage());
        }
        return -1;
    }

    /**
     * Formatted time string to the date.
     * <p>The pattern is {@code yyyy-MM-dd HH:mm:ss}.</p>
     *
     * @param time The formatted time string.
     * @return the date
     */
    public static Date string2Date(final String time) {
        return string2Date(time, DEFAULT_FORMAT);
    }

    /**
     * Formatted time string to the date.
     *
     * @param time   The formatted time string.
     * @param format The format.
     * @return the date
     */
    public static Date string2Date(final String time, @NonNull final DateFormat format) {
        if (CheckUtils.isEmpty(time)) {
            LogUtils.e(TAG, "Time string is empty.");
            return null;
        }
        try {
            return format.parse(time);
        } catch (ParseException e) {
            LogUtils.e(TAG, "Time string format error! " + e.getMessage());
        }
        return null;
    }

    /**
     * Date to the formatted time string.
     * <p>The pattern is {@code yyyy-MM-dd HH:mm:ss}.</p>
     *
     * @param date The date.
     * @return the formatted time string
     */
    public static String date2String(final Date date) {
        return date2String(date, DEFAULT_FORMAT);
    }

    /**
     * Date to the formatted time string.
     *
     * @param date   The date.
     * @param format The format.
     * @return the formatted time string
     */
    public static String date2String(final Date date, @NonNull final DateFormat format) {
        return format.format(date);
    }

    /**
     * Date to the milliseconds.
     *
     * @param date The date.
     * @return the milliseconds
     */
    public static long date2Millis(final Date date) {
        return date.getTime();
    }

    /**
     * Milliseconds to the date.
     *
     * @param millis The milliseconds.
     * @return the date
     */
    public static Date millis2Date(final long millis) {
        return new Date(millis);
    }

    /**
     * Return the time span, in unit.
     * <p>The pattern is {@code yyyy-MM-dd HH:mm:ss}.</p>
     *
     * @param timeBefore The before formatted time string.
     * @param timeAfter  The after formatted time string.
     * @param unit       The unit of time span.
     * @return the time span, in unit
     */
    public static long getTimeSpan(final String timeBefore,
                                   final String timeAfter,
                                   final TimeUnit unit) {
        return getTimeSpan(timeBefore, timeAfter, DEFAULT_FORMAT, unit);
    }

    /**
     * Return the time span, in unit.
     *
     * @param timeBefore The before formatted time string.
     * @param timeAfter  The after formatted time string.
     * @param format     The format.
     * @param unit       The unit of time span.
     * @return the time span, in unit
     */
    public static long getTimeSpan(final String timeBefore,
                                   final String timeAfter,
                                   @NonNull final DateFormat format,
                                   final TimeUnit unit) {
        return millis2Time(string2Millis(timeAfter, format)
                - string2Millis(timeBefore, format), unit);
    }

    /**
     * Return the time span, in unit.
     *
     * @param dateBefore The before date.
     * @param dateAfter  The after date.
     * @param unit       The unit of time span.
     * @return the time span, in unit
     */
    public static long getTimeSpan(final Date dateBefore,
                                   final Date dateAfter,
                                   final TimeUnit unit) {
        return millis2Time(date2Millis(dateAfter) - date2Millis(dateBefore), unit);
    }

    /**
     * Return the time span, in unit.
     *
     * @param millisBefore The before milliseconds.
     * @param millisAfter  The after milliseconds.
     * @param unit         The unit of time span.
     * @return the time span, in unit
     */
    public static long getTimeSpan(final long millisBefore,
                                   final long millisAfter,
                                   final TimeUnit unit) {
        return millis2Time(millisBefore - millisAfter, unit);
    }

    /**
     * Return the time span by now, in unit.
     * <p>The pattern is {@code yyyy-MM-dd HH:mm:ss}.</p>
     *
     * @param time The formatted time string.
     * @param unit The unit of time span.
     * @return the time span by now, in unit
     */
    public static long getTimeSpanByNow(final String time, final TimeUnit unit) {
        return getTimeSpan(string2Millis(time), nowMillis(), unit);
    }

    /**
     * Return the time span by now, in unit.
     *
     * @param time   The formatted time string.
     * @param format The format.
     * @param unit   The unit of time span.
     * @return the time span by now, in unit
     */
    public static long getTimeSpanByNow(final String time,
                                        @NonNull final DateFormat format,
                                        final TimeUnit unit) {
        return getTimeSpan(string2Millis(time, format), nowMillis(), unit);
    }

    /**
     * Return the time span by now, in unit.
     *
     * @param date The date.
     * @param unit The unit of time span.
     * @return the time span by now, in unit
     */
    public static long getTimeSpanByNow(final Date date, final TimeUnit unit) {
        return getTimeSpan(date, nowDate(), unit);
    }

    /**
     * Return the time span by now, in unit.
     *
     * @param millis The milliseconds.
     * @param unit   The unit of time span.
     * @return the time span by now, in unit
     */
    public static long getTimeSpanByNow(final long millis, final TimeUnit unit) {
        return getTimeSpan(millis, nowMillis(), unit);
    }

    /**
     * Return whether it is today.
     * <p>The pattern is {@code yyyy-MM-dd HH:mm:ss}.</p>
     *
     * @param time The formatted time string.
     * @return {@code true}: yes<br>{@code false}: no
     */
    public static boolean isToday(final String time) {
        return isToday(string2Millis(time, DEFAULT_FORMAT));
    }

    /**
     * Return whether it is today.
     *
     * @param time   The formatted time string.
     * @param format The format.
     * @return {@code true}: yes<br>{@code false}: no
     */
    public static boolean isToday(final String time, @NonNull final DateFormat format) {
        return isToday(string2Millis(time, format));
    }

    /**
     * Return whether it is today.
     *
     * @param date The date.
     * @return {@code true}: yes<br>{@code false}: no
     */
    public static boolean isToday(final Date date) {
        return isToday(date.getTime());
    }

    /**
     * Return whether it is today.
     *
     * @param millis The milliseconds.
     * @return {@code true}: yes<br>{@code false}: no
     */
    public static boolean isToday(final long millis) {
        Calendar today = Calendar.getInstance();
        Calendar time = Calendar.getInstance();
        time.setTimeInMillis(millis);
        return today.get(Calendar.YEAR) == time.get(Calendar.YEAR)
                && today.get(Calendar.DAY_OF_YEAR) == time.get(Calendar.DAY_OF_YEAR);
    }

    /**
     * Return whether it is yesterday.
     * <p>The pattern is {@code yyyy-MM-dd HH:mm:ss}.</p>
     *
     * @param time The formatted time string.
     * @return {@code true}: yes<br>{@code false}: no
     */
    public static boolean isYesterday(final String time) {
        return isYesterday(string2Millis(time, DEFAULT_FORMAT));
    }

    /**
     * Return whether it is yesterday.
     *
     * @param time   The formatted time string.
     * @param format The format.
     * @return {@code true}: yes<br>{@code false}: no
     */
    public static boolean isYesterday(final String time, @NonNull final DateFormat format) {
        return isYesterday(string2Millis(time, format));
    }

    /**
     * Return whether it is yesterday.
     *
     * @param date The date.
     * @return {@code true}: yes<br>{@code false}: no
     */
    public static boolean isYesterday(final Date date) {
        return isYesterday(date.getTime());
    }

    /**
     * Return whether it is yesterday.
     *
     * @param millis The milliseconds.
     * @return {@code true}: yes<br>{@code false}: no
     */
    public static boolean isYesterday(final long millis) {
        Calendar today = Calendar.getInstance();
        Calendar time = Calendar.getInstance();
        time.setTimeInMillis(millis - TIME_MILLIS_DAY);
        return today.get(Calendar.YEAR) == time.get(Calendar.YEAR)
                && today.get(Calendar.DAY_OF_YEAR) == time.get(Calendar.DAY_OF_YEAR);
    }

    /**
     * Return whether it is this year.
     * <p>The pattern is {@code yyyy-MM-dd HH:mm:ss}.</p>
     *
     * @param time The formatted time string.
     * @return {@code true}: yes<br>{@code false}: no
     */
    public static boolean isThisYear(final String time) {
        return isThisYear(string2Millis(time, DEFAULT_FORMAT));
    }

    /**
     * Return whether it is this year.
     *
     * @param time   The formatted time string.
     * @param format The format.
     * @return {@code true}: yes<br>{@code false}: no
     */
    public static boolean isThisYear(final String time, @NonNull final DateFormat format) {
        return isThisYear(string2Millis(time, format));
    }

    /**
     * Return whether it is this year.
     *
     * @param date The date.
     * @return {@code true}: yes<br>{@code false}: no
     */
    public static boolean isThisYear(final Date date) {
        return isThisYear(date.getTime());
    }

    /**
     * Return whether it is this year.
     *
     * @param millis The milliseconds.
     * @return {@code true}: yes<br>{@code false}: no
     */
    public static boolean isThisYear(final long millis) {
        Calendar today = Calendar.getInstance();
        Calendar time = Calendar.getInstance();
        time.setTimeInMillis(millis);
        return today.get(Calendar.YEAR) == time.get(Calendar.YEAR);
    }

    /**
     * 获取给定时间未来或者过去某段时间后的时间点
     *
     * @param time     给定的时间字符串
     * @param timeSpan 时间间隔
     * @param unit     时间间隔单位
     * @return 结果Unix时间戳
     */
    public static long getFutureOrPastTime(final String time,
                                           final long timeSpan, final TimeUnit unit) {
        return getFutureOrPastTime(string2Millis(time, DEFAULT_FORMAT), timeSpan, unit);
    }

    /**
     * 获取给定时间未来或者过去某段时间后的时间点
     *
     * @param time     给定的时间字符串
     * @param format   给定的时间字符串格式
     * @param timeSpan 时间间隔
     * @param unit     时间间隔单位
     * @return 结果Unix时间戳
     */
    public static long getFutureOrPastTime(final String time, @NonNull final DateFormat format,
                                           final long timeSpan, final TimeUnit unit) {
        return getFutureOrPastTime(string2Millis(time, format), timeSpan, unit);
    }

    /**
     * 获取给定时间未来或者过去某段时间后的时间点
     *
     * @param date     给定的时间对象
     * @param timeSpan 时间间隔
     * @param unit     时间间隔单位
     * @return 结果Unix时间戳
     */
    public static long getFutureOrPastTime(final Date date,
                                           final long timeSpan, final TimeUnit unit) {
        return getFutureOrPastTime(date.getTime(), timeSpan, unit);
    }

    /**
     * 获取给定时间未来或者过去某段时间后的时间点
     *
     * @param millis   给定的Unix时间戳
     * @param timeSpan 时间间隔
     * @param unit     时间间隔单位
     * @return 结果Unix时间戳
     */
    public static long getFutureOrPastTime(final long millis,
                                           final long timeSpan, final TimeUnit unit) {
        return millis + time2Millis(timeSpan, unit);
    }

    /**
     * 获取当前未来或者过去某段时间后的时间点
     *
     * @param timeSpan 时间间隔
     * @param unit     时间间隔单位
     * @param format   结果时间格式
     * @return 结果时间字符串
     */
    public static String getFutureOrPastTimeByNow(final long timeSpan, final TimeUnit unit,
                                                  @NonNull final DateFormat format) {
        return millis2String(getFutureOrPastTimeByNow(timeSpan, unit), format);
    }

    /**
     * 获取当前未来或者过去某段时间后的时间点
     *
     * @param timeSpan 时间间隔
     * @param unit     时间间隔单位
     * @return 结果Unix时间戳
     */
    public static long getFutureOrPastTimeByNow(final long timeSpan, final TimeUnit unit) {
        return System.currentTimeMillis() + time2Millis(timeSpan, unit);
    }

    /**
     * Returns the value of the given calendar field.
     * <p>The pattern is {@code yyyy-MM-dd HH:mm:ss}.</p>
     *
     * @param time  The formatted time string.
     * @param field The given calendar field.
     *              <ul>
     *              <li>{@link Calendar#ERA}</li>
     *              <li>{@link Calendar#YEAR}</li>
     *              <li>{@link Calendar#MONTH}</li>
     *              <li>...</li>
     *              <li>{@link Calendar#DST_OFFSET}</li>
     *              </ul>
     * @return the value of the given calendar field
     */
    public static int getValueByCalendarField(final String time, final int field) {
        return getValueByCalendarField(string2Date(time, DEFAULT_FORMAT), field);
    }

    /**
     * Returns the value of the given calendar field.
     *
     * @param time   The formatted time string.
     * @param format The format.
     * @param field  The given calendar field.
     *               <ul>
     *               <li>{@link Calendar#ERA}</li>
     *               <li>{@link Calendar#YEAR}</li>
     *               <li>{@link Calendar#MONTH}</li>
     *               <li>...</li>
     *               <li>{@link Calendar#DST_OFFSET}</li>
     *               </ul>
     * @return the value of the given calendar field
     */
    public static int getValueByCalendarField(final String time,
                                              @NonNull final DateFormat format,
                                              final int field) {
        return getValueByCalendarField(string2Date(time, format), field);
    }

    /**
     * Returns the value of the given calendar field.
     *
     * @param date  The date.
     * @param field The given calendar field.
     *              <ul>
     *              <li>{@link Calendar#ERA}</li>
     *              <li>{@link Calendar#YEAR}</li>
     *              <li>{@link Calendar#MONTH}</li>
     *              <li>...</li>
     *              <li>{@link Calendar#DST_OFFSET}</li>
     *              </ul>
     * @return the value of the given calendar field
     */
    public static int getValueByCalendarField(final Date date, final int field) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(field);
    }

    /**
     * Returns the value of the given calendar field.
     *
     * @param millis The milliseconds.
     * @param field  The given calendar field.
     *               <ul>
     *               <li>{@link Calendar#ERA}</li>
     *               <li>{@link Calendar#YEAR}</li>
     *               <li>{@link Calendar#MONTH}</li>
     *               <li>...</li>
     *               <li>{@link Calendar#DST_OFFSET}</li>
     *               </ul>
     * @return the value of the given calendar field
     */
    public static int getValueByCalendarField(final long millis, final int field) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(millis);
        return cal.get(field);
    }

    private static long time2Millis(final long timeSpan, final TimeUnit unit) {
        return unit.toMillis(timeSpan);
    }

    private static long millis2Time(final long millis, final TimeUnit unit) {
        return unit.convert(millis, TimeUnit.MILLISECONDS);
    }

}
