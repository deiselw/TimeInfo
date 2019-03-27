package com.deiselw.timeinfo;

import android.view.View;

import com.deiselw.timeinfo.view.ClockProgressBarView;
import com.deiselw.timeinfo.view.WeekProgressBarView;

/**
 * Created by Deise on 31/10/2017.
 */

public class WeekFragment extends ClockFragment {
    protected ClockProgressBarView castClockView(View view) {
        return (WeekProgressBarView) view;
    }

    protected void updateTimeViews(boolean startAnimation) {
        TimeUtils timeUtils = TimeUtils.getInstance();
        mTimeVal0Text.setText(timeUtils.getCompletedWeekDays() + "");
        mTimeVal1Text.setText(timeUtils.getHourOfDay() + "");
        mTimeLeft0ValText.setText(timeUtils.getWeekDaysLeft() + "");
        mTimeLeft1ValText.setText(timeUtils.getDayHoursLeft() + "");

        int percent = (int)(timeUtils.getRelativeWeekAmount()*100.0);
        mPercentValText.setText(percent + "");

        int progressValue = percent*10;
        if (startAnimation) {
            startProgressAnimation(progressValue);
        }else {
            mClockView.setProgress(progressValue);
        }
    }

    protected int getLayoutRes() { return R.layout.fragment_week; }
}
