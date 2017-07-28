package com.crucentralcoast.app.presentation.views.prayers;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.crucentralcoast.app.CruApplication;
import com.crucentralcoast.app.R;

/**
 * Created by brittanyberlanga on 5/13/17.
 */

public class PrayerRequestsPagerAdapter extends FragmentStatePagerAdapter {

    private String tabTitles[] = CruApplication.getContext().getResources()
            .getStringArray(R.array.prayer_requests_titles);
    private final int PAGE_COUNT = tabTitles.length;

    public PrayerRequestsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return PrayerRequestListFragment.newInstance(position == 0);
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }
}
