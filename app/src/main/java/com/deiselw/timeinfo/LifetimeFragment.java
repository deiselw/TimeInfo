package com.deiselw.timeinfo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by Deise on 10/11/2017.
 */

public class LifetimeFragment extends Fragment {
    private final BroadcastReceiver mTimeUpdatedBroadcastReceiver = new LifetimeFragment.MyBroadcastReceiver();
    private final IntentFilter mTimeUpdatedIntentFilter = new IntentFilter(MainActivity.ACTION_TIME_UPDATED);

    TextView dateText;
    TextView birthdayText;
    TextView lifeYearsText;
    TextView lifeMonthsText;
    TextView lifeDaysText;
    TextView lifeHoursText;
    Button yearsOfLifeButton;
    Button monthsOfLifeButton;
    TextView daysOfLifeText;
    TextView hoursOfLifeText;
    boolean mReady;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mReady = sharedPref.getLong(MainActivity.KEY_PREF_BIRTHDAY, 0) != 0;
        if (!mReady) {
            return super.onCreateView(inflater, container, savedInstanceState);
        }

        View rootView = inflater.inflate(R.layout.fragment_lifetime, container, false);

        dateText = rootView.findViewById(R.id.text_today);
        birthdayText = rootView.findViewById(R.id.text_birthday);
        lifeYearsText = rootView.findViewById(R.id.text_life_years);
        lifeMonthsText = rootView.findViewById(R.id.text_life_months);
        lifeDaysText = rootView.findViewById(R.id.text_life_days);
        lifeHoursText = rootView.findViewById(R.id.text_life_hours);
        yearsOfLifeButton = rootView.findViewById(R.id.text_years_of_life);
        monthsOfLifeButton = rootView.findViewById(R.id.text_months_of_life);
        daysOfLifeText = rootView.findViewById(R.id.text_days_of_life);
        hoursOfLifeText = rootView.findViewById(R.id.text_hours_of_life);

        yearsOfLifeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), TimeCalYearsActivity.class);
                startActivity(intent);
            }
        });

        monthsOfLifeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), TimeCalMonthsActivity.class);
                startActivity(intent);
            }
        });

        updateViews();

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(mTimeUpdatedBroadcastReceiver, mTimeUpdatedIntentFilter);
    }

    @Override
    public void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(mTimeUpdatedBroadcastReceiver);
    }

    private void updateViews() {
        if (!mReady) {
            return;
        }

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getContext());
        long birthday = sharedPref.getLong(MainActivity.KEY_PREF_BIRTHDAY, 0);
        if (birthday == 0) {
            return;
        }

        dateText.setText(TimeUtils.getInstance().getDate());

        BirthdayUtils birthdayUtils = new BirthdayUtils(birthday);
        birthdayText.setText(birthdayUtils.getDate());
        lifeYearsText.setText(birthdayUtils.getLifeClockYears() + "");
        lifeMonthsText.setText(birthdayUtils.getLifeClockMonths() + "");
        lifeDaysText.setText(birthdayUtils.getLifeClockDays() + "");
        lifeHoursText.setText(birthdayUtils.getLifeClockHours() + "");

        yearsOfLifeButton.setText(birthdayUtils.getLifeAccumYear() + "");
        monthsOfLifeButton.setText(birthdayUtils.getLifeAccumMonth() + "");
        daysOfLifeText.setText(birthdayUtils.getLifeAccumDay() + "");
        hoursOfLifeText.setText(birthdayUtils.getLifeAccumHour() + "");
    }

    private class MyBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(final Context context, final Intent intent) {
            String action = intent.getAction();
            if (MainActivity.ACTION_TIME_UPDATED.equals(action) && getView() != null) {
                updateViews();
            }
        }
    }
}