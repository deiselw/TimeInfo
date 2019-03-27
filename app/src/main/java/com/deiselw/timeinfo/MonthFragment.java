package com.deiselw.timeinfo;

import android.view.View;

import com.deiselw.timeinfo.view.ClockProgressBarView;
import com.deiselw.timeinfo.view.MonthProgressBarView;

/**
 * Created by Deise on 31/10/2017.
 */
public class MonthFragment extends ClockFragment {
    protected ClockProgressBarView castClockView(View view) {
        return (MonthProgressBarView) view;
    }

    protected void updateTimeViews(boolean startAnimation) {
        TimeUtils timeUtils = TimeUtils.getInstance();
        mTimeVal0Text.setText(timeUtils.getCompletedMonthDays() + "");
        mTimeVal1Text.setText(timeUtils.getHourOfDay() + "");
        mTimeLeft0ValText.setText(timeUtils.getMonthDaysLeft() + "");
        mTimeLeft1ValText.setText(timeUtils.getDayHoursLeft() + "");

        int percent = (int)(timeUtils.getRelativeMonthAmount()*100.0);
        mPercentValText.setText(percent + "");

        int progressValue = percent*10;
        if (startAnimation) {
            startProgressAnimation(progressValue);
        }else {
            mClockView.setProgress(progressValue);
        }
    }

    protected int getLayoutRes() { return R.layout.fragment_month; }
}