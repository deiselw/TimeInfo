package com.deiselw.timeinfo;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.animation.DecelerateInterpolator;

/**
 * Created by Deise on 30/10/2017.
 */

public class ClocksFragmentPagerAdapter extends FragmentPagerAdapter {
    public static final DecelerateInterpolator mDecelerateInterpolator = new DecelerateInterpolator();

    public ClocksFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch(position) {
            case 0:
                return new DayFragment();
            case 1:
                return new WeekFragment();
            case 2:
                return new MonthFragment();
            case 3:
                return new YearFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 4;
    }
}
