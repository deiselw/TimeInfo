package com.deiselw.timeinfo.preference;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.deiselw.timeinfo.R;

/**
 * Created by Deise on 06/11/2017.
 */

public class PreferencesFragment extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }
}