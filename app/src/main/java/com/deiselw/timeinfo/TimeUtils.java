package com.deiselw.timeinfo;

import java.util.Calendar;
import java.util.Locale;

/**
 * Created by Deise on 27/11/2017.
 */

public class TimeUtils {
    public static final int YEAR_MONTHS = 12;
    public static final int YEAR_DAYS = 365;
    public static final int WEEK_DAYS = 7;
    public static final int WEEK_HOURS = 168; // 24h*7d;
    public static final int DAY_HOURS = 24;
    public static final int HOUR_MINUTES = 60;

    private static TimeUtils mInstance;

    private Locale mLocale;
    private int mMinuteOfHour;
    private int mHourOfDay;
    private int mHourOfWeek;
    private int mHourOfMonth;
    private int mHourOfYear;
    private int mDayOfWeek;
    private int mDayOfMonth;
    private int mDayOfYear;
    private int mWeekOfMonth;
    private int mWeekOfYear;
    private int mMonthOfYear;
    private int mYear;
    private int mFirstDayOfWeek;
    private int mMonthDays;
    private String mMonthName;
    private double mTotalHourOfDay;

    public static TimeUtils getInstance() {
        if (mInstance == null) {
            mInstance = new TimeUtils();
        }
        return mInstance;
    }

    public void update() {
        Calendar now = Calendar.getInstance();
        mLocale = Locale.getDefault();

        mMinuteOfHour = now.get(Calendar.MINUTE);
        mHourOfDay = now.get(Calendar.HOUR_OF_DAY);
        mTotalHourOfDay = (double)mHourOfDay + ((double)mMinuteOfHour/(double)HOUR_MINUTES);

        mFirstDayOfWeek = now.getFirstDayOfWeek();

        mDayOfWeek = (7 + now.get(Calendar.DAY_OF_WEEK) - mFirstDayOfWeek)%7 + 1; // [1-6]
        mHourOfWeek = (mDayOfWeek - 1)*DAY_HOURS + mHourOfDay;

        mDayOfMonth = now.get(Calendar.DAY_OF_MONTH);
        mHourOfMonth = (mDayOfMonth - 1)*DAY_HOURS + mHourOfDay;

        mDayOfYear = now.get(Calendar.DAY_OF_YEAR);
        mHourOfYear = (mDayOfYear - 1)*DAY_HOURS + mHourOfDay;

        mWeekOfMonth = now.get(Calendar.WEEK_OF_MONTH);
        mWeekOfYear = now.get(Calendar.WEEK_OF_YEAR);

        mMonthName = now.getDisplayName(Calendar.MONTH, Calendar.SHORT, mLocale);
        mMonthDays = now.getActualMaximum(Calendar.DAY_OF_MONTH);
        mMonthOfYear = now.get(Calendar.MONTH) + 1; // [1-12]

        mYear = now.get(Calendar.YEAR);
    }

    public int getHourOfDay() { return mHourOfDay; }

    public int getMinuteOfHour() { return mMinuteOfHour; };

    public int getCompletedWeekDays() {
        return mDayOfWeek - 1;
    } // [0-6]

    public int getCompletedMonthDays() {
        return mDayOfMonth - 1;
    } // [0-x]

    public int getCompletedYearMonths() {
        return mMonthOfYear - 1;
    } // [0-11]

    public double getRelativeDayAmount() {
        return mTotalHourOfDay/(double)DAY_HOURS;
    }

    public double getRelativeWeekAmount() {
        return (mTotalHourOfDay + (double)(mDayOfWeek - 1)*(double)DAY_HOURS)/(double)WEEK_HOURS;
    }

    public double getRelativeMonthAmount() {
        return (mTotalHourOfDay + (double)(mDayOfMonth - 1)*(double)DAY_HOURS)/((double)mMonthDays*(double)DAY_HOURS);
    }

    public double getRelativeYearAmount() {
        return (double)mDayOfYear/(double)YEAR_DAYS;
    }

    public int getMonthDays() { return mMonthDays; }

    public int getMonthDaysUntilFirstDayOfWeek() {
        Calendar fstDayOfMonth = Calendar.getInstance();
        fstDayOfMonth.set(Calendar.DAY_OF_MONTH, 1);
        return (mFirstDayOfWeek + WEEK_DAYS - fstDayOfMonth.get(Calendar.DAY_OF_WEEK))%WEEK_DAYS + 1; // transform to [1-7]
    }

    public String[] getDaysOfWeek() {
        String[] daysOfWeek = new String[WEEK_DAYS];
        Calendar calendar = Calendar.getInstance();
        for (int i = 0; i < WEEK_DAYS; i++) {
            calendar.set(Calendar.DAY_OF_WEEK, mFirstDayOfWeek + i);
            daysOfWeek[i] = calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, mLocale);
        }
        return daysOfWeek;
    }

    public int[] getMonthsDays() {
        int[] monthsDays = new int[YEAR_MONTHS];
        Calendar calendar = Calendar.getInstance();
        for (int i = 0; i < YEAR_MONTHS; i++) {
            calendar.set(Calendar.MONTH, i);
            monthsDays[i] = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        }
        return monthsDays;
    }

    public String[] getMonths() {
        String[] months = new String[YEAR_MONTHS];
        Calendar calendar = Calendar.getInstance();
        for (int i = 0; i < YEAR_MONTHS; i++) {
            calendar.set(Calendar.MONTH, i);
            months[i] = calendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, mLocale);
        }
        return months;
    }

    public int getHourMinutesLeft() { return HOUR_MINUTES - mMinuteOfHour; }

    public int getDayHoursLeft() {
        return DAY_HOURS - mHourOfDay - 1;
    }

    public int getWeekDaysLeft() {
        return WEEK_DAYS - mDayOfWeek;
    }

    public int getMonthDaysLeft() { return mMonthDays - mDayOfMonth; }

    public int getYearMonthsLeft() {
        return YEAR_MONTHS - mMonthOfYear;
    }

    public String getDate() {
        return mMonthName.toUpperCase() + " " + mDayOfMonth + " " + mYear;
    }

    public String getFirstDayOfWeek() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_WEEK, mFirstDayOfWeek);
        return calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, mLocale);
    }

    public String getMonthFirstDayOfWeek() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        return calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, mLocale);
    }

    private TimeUtils() {
        update();
    }
}
