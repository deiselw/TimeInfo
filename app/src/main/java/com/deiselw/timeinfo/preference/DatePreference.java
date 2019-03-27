package com.deiselw.timeinfo.preference;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.DialogPreference;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;

import com.deiselw.timeinfo.R;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Deise on 07/11/2017.
 */

public class DatePreference extends DialogPreference {
    private long mTimeInMillis;
    private DatePicker mDatePicker;
    private int mMaxYears;

    public DatePreference(Context context, AttributeSet attrs) {
        super(context, attrs);

        mMaxYears = context.getResources().getInteger(R.integer.calendar_years_max);
        setPositiveButtonText(android.R.string.ok);
        setNegativeButtonText(android.R.string.cancel);
    }

    @Override
    protected void onBindDialogView(View view) {
        super.onBindDialogView(view);

        mDatePicker = (DatePicker) view;

        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);
        mDatePicker.setMaxDate(calendar.getTimeInMillis());

        if (mTimeInMillis != 0) {
            calendar.setTimeInMillis(mTimeInMillis);
        }

        mDatePicker.updateDate(
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));

        calendar.set(Calendar.YEAR, currentYear - mMaxYears);
        mDatePicker.setMinDate(calendar.getTimeInMillis());
    }

    @Override
    protected void onSetInitialValue(boolean restorePersistedValue, Object defaultValue) {
        if (restorePersistedValue) {
            mTimeInMillis = getPersistedLong(0);
        }else {
            mTimeInMillis = 0;
        }
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        if (positiveResult) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(mDatePicker.getYear(), mDatePicker.getMonth(), mDatePicker.getDayOfMonth());
            long timeInMillis = calendar.getTimeInMillis();

            boolean changed = timeInMillis != mTimeInMillis;
            if (changed && callChangeListener(timeInMillis)) {
                mTimeInMillis = timeInMillis;
                persistLong(timeInMillis);
                setSummary(toString(calendar.getTime()));

                notifyDependencyChange(shouldDisableDependents());
                notifyChanged();
            }
        }
    }

    public String toString(long timeInMillis) {
        return toString(new Date(timeInMillis));
    }

    public String toString(Date date) {
        return DateFormat.getDateInstance(DateFormat.MEDIUM).format(date);
    }

    @Override
    protected void onAttachedToHierarchy (PreferenceManager preferenceManager) {
        super.onAttachedToHierarchy(preferenceManager);

        long persistedValue = getPersistedLong(0);
        if (persistedValue != 0) {
            setSummary(toString(persistedValue));
        }
    }
}
