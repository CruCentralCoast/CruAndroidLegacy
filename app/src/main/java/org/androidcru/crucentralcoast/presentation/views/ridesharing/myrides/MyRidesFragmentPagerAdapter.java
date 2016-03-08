package org.androidcru.crucentralcoast.presentation.views.ridesharing.myrides;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;


public class MyRidesFragmentPagerAdapter extends FragmentStatePagerAdapter
{
    private String tabTitles[] = new String[] { "Driver", "Passenger" };
    final int PAGE_COUNT = tabTitles.length;

    public MyRidesFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int position) {

        Fragment frag = null;

        switch (position) {
            case 0:
                frag = new MyRidesDriverFragment();
                break;
            case 1:
                frag = new MyRidesPassengerFragment();
                break;
        }

        return frag;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return tabTitles[position];
    }
}
