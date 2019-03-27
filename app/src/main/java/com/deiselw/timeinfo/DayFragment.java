package com.deiselw.timeinfo;

import android.os.Bundle;
import android.view.View;

import com.deiselw.timeinfo.view.ClockProgressBarView;
import com.deiselw.timeinfo.view.DayProgressBarView;

/**
 * Created by Deise on 31/10/2017.
 */

public class DayFragment extends ClockFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected int getLayoutRes() { return R.layout.fragment_day; }

    protected ClockProgressBarView castClockView(View view) {
        return (DayProgressBarView) view;
    }

    protected void updateTimeViews(boolean startAnimation) {
        TimeUtils timeUtils = TimeUtils.getInstance();
        mTimeVal0Text.setText(timeUtils.getHourOfDay() + "");
        mTimeVal1Text.setText(timeUtils.getMinuteOfHour() + "");
        mTimeLeft0ValText.setText(timeUtils.getDayHoursLeft() + "");
        mTimeLeft1ValText.setText(timeUtils.getHourMinutesLeft() + "");

        int dayPercent = (int)(timeUtils.getRelativeDayAmount()*100f);
        mPercentValText.setText(dayPercent + "");

        int progressValue = dayPercent*10;
        if (startAnimation) {
            startProgressAnimation(progressValue);
        }else {
           mClockView.setProgress(progressValue);
        }
    }
}
