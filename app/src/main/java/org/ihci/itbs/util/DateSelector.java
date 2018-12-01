package org.ihci.itbs.util;

import org.jetbrains.annotations.Contract;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * This {@code DateSelector} utility is used for select a {@code Date}
 * quickly and conveniently.
 * <p>
 * Try not to handle {@code Date} directly in other classes.
 *
 * @author Yukino Yukinoshita
 * @version 1.0
 */

public class DateSelector {

    private Date currentDate;

    public DateSelector() {
        currentDate = new Date();
    }

    public DateSelector(Date currentDate) {
        setCurrentDate(currentDate);
    }

    public Date getCurrentDate() {
        return (Date) currentDate.clone();
    }

    public void setCurrentDate(Date currentDate) {
        this.currentDate = currentDate;
    }

    /**
     * Get several days after
     *
     * @param daysAfter number of day after
     * @return the day after {@code daysAfter} days
     */
    public Date getDaysAfter(int daysAfter) {
        if (currentDate == null) {
            currentDate = new Date();
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);
        calendar.add(Calendar.DATE, daysAfter);
        return calendar.getTime();
    }

    public static Date getDaysAfter(Date now, int daysAfter) {
        if (now == null) {
            now = new Date();
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);
        calendar.add(Calendar.DATE, daysAfter);
        return calendar.getTime();
    }

    public static int getDayOfMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    public static Date getStartTimeThisDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    public static Date getEndTimeThisDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTime();
    }

    /**
     * Get the first day in this week
     *
     * @return first day with time 0:00:00
     */
    public static Date getFirstDayCurrentWeek() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.DAY_OF_WEEK, 2);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTime();
    }

    /**
     * Get the first day in this week
     *
     * @param date specified date
     * @return first day with time 0:00:00
     */
    public static Date getFirstDayCurrentWeek(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_WEEK, 2);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTime();
    }

    /**
     * Get the last day in this week
     *
     * @return last day with time 0:00:00
     */
    public static Date getLastDayCurrentWeek() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(getFirstDayCurrentWeek());
        calendar.add(Calendar.DATE, 6);
        return calendar.getTime();
    }

    /**
     * Get the last day and time in this week
     *
     * @return last day with time 23:59:59
     */
    public static Date getLastDayAndTimeCurrentWeek() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(getLastDayCurrentWeek());
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        return calendar.getTime();
    }

    /**
     * Get the first day in specified week
     *
     * @param specifiedWeek specified week number
     * @param currentWeek current week number
     * @return first day with time 0:00:00
     */
    public static Date getFirstDaySpecifiedWeek(int specifiedWeek, int currentWeek) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.WEEK_OF_YEAR, specifiedWeek - currentWeek);
        calendar.set(Calendar.DAY_OF_WEEK, 2);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTime();
    }

    /**
     * Get the last day in specified week
     *
     * @param specifiedWeek specified week number
     * @param currentWeek current week number
     * @return last day with time 0:00:00
     */
    public static Date getLastDaySpecifiedWeek(int specifiedWeek, int currentWeek) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(getFirstDaySpecifiedWeek(specifiedWeek, currentWeek));
        calendar.add(Calendar.DATE, 6);
        return calendar.getTime();
    }

    /**
     * Get the last day and time in specified week
     *
     * @param specifiedWeek specified week number
     * @param currentWeek current week number
     * @return last day with time 23:59:59
     */
    public static Date getLastDayAndTimeSpecifiedWeek(int specifiedWeek, int currentWeek) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(getLastDaySpecifiedWeek(specifiedWeek, currentWeek));
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        return calendar.getTime();
    }

    @Contract("null -> null")
    public static String dateToString(Date date) {
        if (date == null) {
            return null;
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
        return simpleDateFormat.format(date);
    }

    @Contract("null -> null")
    public static Date stringToDate(String date) throws ParseException {
        if (date == null) {
            return null;
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
        return simpleDateFormat.parse(date);
    }

    @Contract("null -> null")
    public static String dateToStringWithoutTime(Date date) {
        if (date == null) {
            return null;
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日", Locale.CHINA);
        return simpleDateFormat.format(date);
    }

    @Override
    public String toString() {
        return dateToString(getCurrentDate());
    }

}
