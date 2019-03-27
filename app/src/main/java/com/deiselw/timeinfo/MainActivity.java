package com.deiselw.timeinfo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.deiselw.timeinfo.preference.PreferencesActivity;

import java.util.List;

public class MainActivity extends AppCompatActivity
        implements LifetimeRequirementsDialogFragment.LifetimeRequirementsDialogListener {
    static final String STATE_NAV_LIFETIME = "navLifetimeSelected";
    public static final String TAG_FRAGMENT_PRE_LIFETIME = "frag_pre_lifetime";
    public static final String KEY_PREF_USERNAME = "pref_username";
    public static final String KEY_PREF_BIRTHDAY = "pref_birthday";
    public static final String KEY_PREF_CALENDAR_YEARS = "pref_cal_years";
    public static final String ACTION_TIME_UPDATED = "com.deiselw.timeinfo.ACTION_TIME_UPDATED";
    public static int COLOR_LINE;
    public static int COLOR_DISABLED;

    private final BroadcastReceiver mTimeTickBroadcastReceiver = new MyBroadcastReceiver();
    private final IntentFilter mTimeTickIntentFilter = new IntentFilter(Intent.ACTION_TIME_TICK);
    private final Intent mUpdateTimeIntent = new Intent();

    private final BroadcastReceiver mLocaleChangedBroadcastReceiver = new MyBroadcastReceiver();
    private final IntentFilter mLocaleChangedIntentFilter = new IntentFilter(Intent.ACTION_TIME_TICK);

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;

    private TextView mTextDate;
    private boolean mLifetimeVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("timeapp", "MainActivity.onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        NavigationView navigationView = findViewById(R.id.nav);
        setupDrawerContent(navigationView);

        mDrawerLayout = findViewById(R.id.drawer);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close) {

            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
            }
        };

        mDrawerLayout.addDrawerListener(mDrawerToggle);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        mDrawerToggle.syncState();

        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);

        mUpdateTimeIntent.setAction(ACTION_TIME_UPDATED);

        mTextDate = findViewById(R.id.text_date);
        if (savedInstanceState == null) {
            navigationView.getMenu().performIdentifierAction(R.id.nav_item_clocks, 0);
        }else {
            if (savedInstanceState.getBoolean(STATE_NAV_LIFETIME) == true) {
                mLifetimeVisible = true;
                setLifetimeToolbar();
            }else {
                mTextDate.setText(TimeUtils.getInstance().getDate());
            }
        }

        COLOR_LINE = ContextCompat.getColor(this, R.color.colorLine);
        COLOR_DISABLED = ContextCompat.getColor(this, R.color.colorDisabled);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putBoolean(STATE_NAV_LIFETIME, mLifetimeVisible);
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    protected void onStart() {
        super.onStart();
        registerReceiver(mTimeTickBroadcastReceiver, mTimeTickIntentFilter);
        registerReceiver(mLocaleChangedBroadcastReceiver, mLocaleChangedIntentFilter);

        updateAppTime();
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(mTimeTickBroadcastReceiver);
        unregisterReceiver(mLocaleChangedBroadcastReceiver);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onDialogPositiveClick(long timeInMillis) {
        if (timeInMillis == 0) {
            return;
        }

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putLong(KEY_PREF_BIRTHDAY, timeInMillis);
        editor.commit();

        setLifetimeToolbar();
        Fragment lifetimeFragment = new LifetimeFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame_content, lifetimeFragment, TAG_FRAGMENT_PRE_LIFETIME)
                .commit();
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
            new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    item.setChecked(true);
                    mDrawerLayout.closeDrawers();
                    mLifetimeVisible = false;
                    switch(item.getItemId()) {
                        case R.id.nav_item_clocks:
                            mTextDate.setText(TimeUtils.getInstance().getDate());
                            Fragment clocksFragment = new ClocksFragment();
                            getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.frame_content, clocksFragment)
                                    .commit();
                            break;
                        case R.id.nav_item_lifetime:
                            if (isBirthdayValid()) {
                                setLifetimeToolbar();
                                Fragment lifetimeFragment = new LifetimeFragment();
                                getSupportFragmentManager().beginTransaction()
                                        .replace(R.id.frame_content, lifetimeFragment)
                                        .commit();
                            }else {
                                LifetimeRequirementsDialogFragment dialog = new LifetimeRequirementsDialogFragment();
                                dialog.show(getSupportFragmentManager(), "LifetimeRequirementsDialogFragment");
                            }
                            mLifetimeVisible = true;
                            break;
                        case R.id.nav_item_pref:
                            Intent intent = new Intent(MainActivity.this, PreferencesActivity.class);
                            startActivity(intent);
                            break;
                    }
                    return true;
                }
            }
        );
    }

    private void setLifetimeToolbar() {
        mTextDate.setText(R.string.nav_item_calendars);
    }

    private boolean isBirthdayValid() {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        return sharedPref.getLong(KEY_PREF_BIRTHDAY, 0) != 0;
    }

    private void updateDate(String date) {
        if (!mLifetimeVisible) {
            mTextDate.setText(date);
        }
    }

    private void updateAppTime() {
        TimeUtils timeUtils = TimeUtils.getInstance();
        timeUtils.update();
        updateDate(timeUtils.getDate());

        LocalBroadcastManager.getInstance(this).sendBroadcast(mUpdateTimeIntent);
    }

    private class MyBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(final Context context, final Intent intent) {
            String action = intent.getAction();
            if (Intent.ACTION_TIME_TICK.equals(action)
                    || Intent.ACTION_LOCALE_CHANGED.equals(action)) {
                updateAppTime();
            }
        }
    }
}
