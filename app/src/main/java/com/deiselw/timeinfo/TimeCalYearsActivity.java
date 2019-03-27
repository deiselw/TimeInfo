package com.deiselw.timeinfo;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.deiselw.timeinfo.view.TimeCalView;

/**
 * Created by Deise on 08/01/2018.
 */

public class TimeCalYearsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cal_years);

//        CollapsingToolbarLayout collapsingToolbar = findViewById(R.id.collapsing_toolbar);
//        collapsingToolbar.setExpandedTitleTextAppearance(R.style.TextAppearanceTitleLight);
//        collapsingToolbar.setCollapsedTitleTextAppearance(R.style.TextAppearanceTitleLight);
//        collapsingToolbar.setCollapsedTitleTypeface(ResourcesCompat.getFont(getApplicationContext(), R.font.roboto_light));
//        collapsingToolbar.setExpandedTitleTypeface(ResourcesCompat.getFont(getApplicationContext(), R.font.roboto_light));

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_close_white_24dp);
        actionBar.setDisplayHomeAsUpEnabled(true);

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        long birthday = sharedPref.getLong(MainActivity.KEY_PREF_BIRTHDAY, 0);
        if (birthday == 0) {
            return;
        }

        int colCount = 10;
        int calendarYears = sharedPref.getInt(MainActivity.KEY_PREF_CALENDAR_YEARS, getResources().getInteger(R.integer.calendar_years_default));
        int currentYear = new BirthdayUtils(birthday).getLifeAccumYear();
        if (!TimeCalView.isValid(colCount, calendarYears, currentYear)) {
            return;
        }

        TimeCalView timeCalView = findViewById(R.id.time_cal_years);
        timeCalView.init(colCount, calendarYears, currentYear);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            super.onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
