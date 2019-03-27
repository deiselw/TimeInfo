package com.deiselw.timeinfo;

import android.view.View;
import android.widget.TextView;

import com.deiselw.timeinfo.view.ClockProgressBarView;
import com.deiselw.timeinfo.view.YearProgressBarView;

/**
 * Created by Deise on 01/11/2017.
 */

public class YearFragment extends ClockFragment {
    TextView mTimeVal2Text;
    TextView mTimeLeft2ValText;

    @Override
    protected void init(View rootView) {
        mTimeVal2Text = rootView.findViewById(R.id.text_time_val2);
        mTimeLeft2ValText = rootView.findViewById(R.id.text_time_left_val2);
    }

    protected ClockProgressBarView castClockView(View view) {
        return (YearProgressBarView) view;
    }

    protected void updateTimeViews(boolean startAnimation) {
        TimeUtils timeUtils = TimeUtils.getInstance();
        mTimeVal0Text.setText(timeUtils.getCompletedYearMonths() + "");
        mTimeVal1Text.setText(timeUtils.getCompletedMonthDays() + "");
        mTimeVal2Text.setText(timeUtils.getHourOfDay() + "");
        mTimeLeft0ValText.setText(timeUtils.getYearMonthsLeft() + "");
        mTimeLeft1ValText.setText(timeUtils.getMonthDaysLeft() + "");
        mTimeLeft2ValText.setText(timeUtils.getDayHoursLeft() + "");

        int percent = (int)(timeUtils.getRelativeYearAmount()*100.0);
        mPercentValText.setText(percent + "");

        int progressValue = percent*10;
        if (startAnimation) {
            startProgressAnimation(progressValue);
        }else {
            mClockView.setProgress(progressValue);
        }
    }

    protected int getLayoutRes() { return R.layout.fragment_year; }
}
