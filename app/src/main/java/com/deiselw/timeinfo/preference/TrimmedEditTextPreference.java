package com.deiselw.timeinfo.preference;

import android.content.Context;
import android.preference.EditTextPreference;
import android.preference.PreferenceManager;
import android.util.AttributeSet;

/**
 * Created by Deise on 08/11/2017.
 */

public class TrimmedEditTextPreference extends EditTextPreference {
    private CharSequence mSummaryAttr;

    public TrimmedEditTextPreference(Context context) {
        this(context, null);
    }

    public TrimmedEditTextPreference(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TrimmedEditTextPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, 0, 0);
    }

    public TrimmedEditTextPreference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs);
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        if (positiveResult) {
            String value = getEditText().getText().toString().trim();
            if (callChangeListener(value)) {
                if (value.isEmpty()) {
                    setSummary(mSummaryAttr);
                }else {
                    setSummary(value);
                }
                setText(value);
            }
        }
    }

    @Override
    protected void onAttachedToHierarchy (PreferenceManager preferenceManager) {
        super.onAttachedToHierarchy(preferenceManager);

        mSummaryAttr = getSummary();

        String persistedValue = getPersistedString("");
        if (persistedValue != "") {
            setSummary(persistedValue);
        }
    }
}
