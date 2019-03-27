package com.deiselw.timeinfo;

import android.animation.ObjectAnimator;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.deiselw.timeinfo.view.ClockProgressBarView;

/**
 * Created by Deise on 28/11/2017.
 */

public abstract class ClockFragment extends Fragment {
    private final BroadcastReceiver mTimeUpdatedBroadcastReceiver = new MyBroadcastReceiver();
    private final IntentFilter mTimeUpdatedIntentFilter = new IntentFilter(MainActivity.ACTION_TIME_UPDATED);

    protected ClockProgressBarView mClockView;
    protected TextView mPercentValText;
    protected TextView mTimeVal0Text;
    protected TextView mTimeVal1Text;
    protected TextView mTimeLeft0ValText;
    protected TextView mTimeLeft1ValText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(getLayoutRes(), container, false);
        mPercentValText = (TextView) rootView.findViewById(R.id.text_percent_val);
        mClockView = castClockView(rootView.findViewById(R.id.view_clock));
        mTimeVal0Text = (TextView) rootView.findViewById(R.id.text_time_val0);
        mTimeVal1Text = (TextView) rootView.findViewById(R.id.text_time_val1);
        mTimeLeft0ValText = (TextView) rootView.findViewById(R.id.text_time_left_val0);
        mTimeLeft1ValText = (TextView) rootView.findViewById(R.id.text_time_left_val1);
        init(rootView);

        updateTimeViews(savedInstanceState == null);

        return rootView;
    }

    protected void init(View rootView) {}

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

    protected abstract int getLayoutRes();

    protected abstract void updateTimeViews(boolean startAnimation);

    protected abstract ClockProgressBarView castClockView(View view);

    protected void startProgressAnimation(int progressValue) {
        ObjectAnimator animator = ObjectAnimator.ofInt(mClockView, "progress", 0, progressValue);
        animator.setDuration(2*progressValue);
        animator.setInterpolator(ClocksFragmentPagerAdapter.mDecelerateInterpolator);
        animator.start();
    }

    private class MyBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(final Context context, final Intent intent) {
            String action = intent.getAction();
            if (MainActivity.ACTION_TIME_UPDATED.equals(action) && getView() != null) {
                updateTimeViews(false);
            }
        }
    }
}
