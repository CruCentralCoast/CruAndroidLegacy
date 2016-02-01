package org.androidcru.crucentralcoast.presentation.views.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import org.androidcru.crucentralcoast.presentation.views.fragments.driversignup.DriverBasicInfoFragment;
import org.androidcru.crucentralcoast.presentation.views.fragments.driversignup.DriverRideLocFragment;
import org.androidcru.crucentralcoast.presentation.views.fragments.driversignup.DriverRideInfoFragment;

public class DriverPagerAdapter extends FragmentPagerAdapter {

    public DriverPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position)
        {
            case 0:
                return new DriverBasicInfoFragment();
            case 1:
                return new DriverRideInfoFragment();
            case 2:
                return new DriverRideLocFragment();
            default:
                return new DriverRideInfoFragment();
        }
    }

    @Override
    public int getCount() {
        return 3;
    }
}
