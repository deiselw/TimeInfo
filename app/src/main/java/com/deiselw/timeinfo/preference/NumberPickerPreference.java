package com.deiselw.timeinfo.preference;

import android.content.Context;
import android.content.res.TypedArray;
import android.preference.DialogPreference;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.NumberPicker;

import com.deiselw.timeinfo.R;

/**
 * Created by Deise on 02/01/2018.
 */

public class NumberPickerPreference extends DialogPreference {
    static final int DEFAULT_VALUE = 100;

    private NumberPicker mNumberPicker;
    private int mMinValue;
    private int mMaxValue;
    private int mValue;

    public NumberPickerPreference(Context context) {
        this(context, null);
    }

    public NumberPickerPreference(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NumberPickerPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, 0, 0);
    }

    public NumberPickerPreference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs);
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs, R.styleable.NumberPickerPreference, 0, 0);
        try {
            mMinValue = a.getInteger(R.styleable.NumberPickerPreference_minValue, 0);
            mMaxValue = a.getInteger(R.styleable.NumberPickerPreference_maxValue, mMinValue);
        } finally {
            a.recycle();
        }

        setPositiveButtonText(android.R.string.ok);
        setNegativeButtonText(android.R.string.cancel);
    }

    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) {
        return a.getInteger(index, DEFAULT_VALUE);
    }

    @Override
    protected void onSetInitialValue(boolean restorePersistedValue, Object defaultValue) {
        if (restorePersistedValue) {
            mValue = getPersistedInt(DEFAULT_VALUE);
        }else {
            mValue = (Integer) defaultValue;
            persistInt(mValue);
        }
    }

    @Override
    protected void onBindDialogView(View view) {
        super.onBindDialogView(view);
        mNumberPicker = (NumberPicker) view;
        mNumberPicker.setMinValue(mMinValue);
        mNumberPicker.setMaxValue(mMaxValue);
        mNumberPicker.setValue(mValue);
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        if (positiveResult) {
            int value = mNumberPicker.getValue();

            boolean changed = value != mValue;
            if (changed && callChangeListener(value)) {
                mValue = value;
                persistInt(value);
                setSummary(Integer.toString(value));

                notifyDependencyChange(shouldDisableDependents());
                notifyChanged();
            }
        }
    }

    @Override
    protected void onAttachedToHierarchy (PreferenceManager preferenceManager) {
        super.onAttachedToHierarchy(preferenceManager);
        setSummary(Integer.toString(getPersistedInt(DEFAULT_VALUE)));
    }
}
