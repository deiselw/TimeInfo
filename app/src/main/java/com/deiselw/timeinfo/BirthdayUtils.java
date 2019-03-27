package com.deiselw.timeinfo;

import java.util.Calendar;
import java.util.Locale;

/**
 * Created by Deise on 18/01/2018.
 */

public class BirthdayUtils {
    public static final int LEAP_DAY = 31 + 28;

    private int mLifeClockYear;
    private int mLifeClockMonth;
    private int mLifeClockDay;
    private int mCurrentDay;
    private String mDate;

    public BirthdayUtils(long birthday) {
        update(birthday);
    }

    public void update(long birthday) {
        Calendar calendar = Calendar.getInstance();
        int currentMonth = calendar.get(Calendar.MONTH);
        int currentDayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        int currentDayOfYear = calendar.get(Calendar.DAY_OF_YEAR);
        int currentYear = calendar.get(Calendar.YEAR);

        calendar.set(Calendar.MONTH, currentMonth - 1);
        int pastMonthDays = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        calendar.setTimeInMillis(birthday);
        int birthdayMonth = calendar.get(Calendar.MONTH);
        int birthdayDayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        int birthdayDayOfYear = calendar.get(Calendar.DAY_OF_YEAR);
        int birthdayYear = calendar.get(Calendar.YEAR);
        mDate = calendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault()).toUpperCase() +
                    " " + birthdayDayOfMonth + " " + birthdayYear;

        boolean pastBirthday = false;
        if (currentMonth > birthdayMonth) {
            pastBirthday = true;
        } else if (currentMonth == birthdayMonth) {
            pastBirthday = currentDayOfMonth >= birthdayDayOfMonth;
        }

        mLifeClockYear = currentYear - birthdayYear;
        mLifeClockMonth = currentMonth - birthdayMonth;
        mLifeClockDay = currentDayOfMonth - birthdayDayOfMonth;
        if (!pastBirthday) {
            mLifeClockYear -= 1;
            mLifeClockMonth += TimeUtils.YEAR_MONTHS;
        }
        if (currentDayOfMonth < birthdayDayOfMonth) {
            mLifeClockMonth -= 1;
            mLifeClockDay += pastMonthDays;
        }

        mCurrentDay = 1 + mLifeClockYear*TimeUtils.YEAR_DAYS;
        mCurrentDay += currentDayOfYear - birthdayDayOfYear;
        if (!pastBirthday) {
            mCurrentDay += TimeUtils.YEAR_DAYS;
        }

        boolean birthdayBeforeLeap = birthdayDayOfYear < LEAP_DAY;
        boolean currentBeforeLeap = currentDayOfYear < LEAP_DAY;

        currentYear += currentBeforeLeap ? -1 : 0;

        int leapCount = 0;
        leapCount += (birthdayBeforeLeap && isLeapYear(birthdayYear)) ? 1 : 0;
        for (int year = birthdayYear + 1; year <= currentYear; year++) {
            leapCount += isLeapYear(year) ? 1 : 0;
        }

        mCurrentDay += leapCount;
    }

    public int getLifeAccumYear() {
        return mLifeClockYear;
    }

    public int getLifeAccumMonth() { return mLifeClockYear*TimeUtils.YEAR_MONTHS + mLifeClockMonth; }

    public int getLifeAccumDay() {
        return mCurrentDay - 1;
    }

    public int getLifeAccumHour() { return (mCurrentDay - 1)*TimeUtils.DAY_HOURS + TimeUtils.getInstance().getHourOfDay(); }

    public int getLifeClockYears() {
        return mLifeClockYear;
    }

    public int getLifeClockMonths() {
        return mLifeClockMonth;
    }

    public int getLifeClockDays() {
        return mLifeClockDay;
    }

    public int getLifeClockHours() { return TimeUtils.getInstance().getHourOfDay(); }

    public String getDate() {
        return mDate;
    }

    private boolean isLeapYear(int year) {
        if (year%4 != 0) {
            return false;
        }
        if (year%100 != 0) {
            return true;
        }
        if (year%400 != 0) {
            return false;
        }
        return true;
    }
}
