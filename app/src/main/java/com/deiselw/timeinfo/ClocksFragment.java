package com.deiselw.timeinfo;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Deise on 11/11/2017.
 */

public class ClocksFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_clocks, container, false);
        ViewPager viewPager = rootView.findViewById(R.id.pager);
        ClocksFragmentPagerAdapter pagerAdapter = new ClocksFragmentPagerAdapter(getChildFragmentManager());
        viewPager.setAdapter(pagerAdapter);

        TabLayout tabLayout = rootView.findViewById(R.id.tab);
        tabLayout.setupWithViewPager(viewPager);

        return rootView;
    }
}
