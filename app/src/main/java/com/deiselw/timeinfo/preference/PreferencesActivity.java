package com.deiselw.timeinfo.preference;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.deiselw.timeinfo.R;

/**
 * Created by Deise on 06/11/2017.
 */

public class PreferencesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        PreferencesFragment preferencesFragment = new PreferencesFragment();
        getFragmentManager().beginTransaction()
                .replace(R.id.frame_content, preferencesFragment)
                .commit();
    }
}
