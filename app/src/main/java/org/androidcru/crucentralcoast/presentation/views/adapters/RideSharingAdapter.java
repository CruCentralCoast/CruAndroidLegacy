package org.androidcru.crucentralcoast.presentation.views.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import org.androidcru.crucentralcoast.presentation.views.fragments.DriverFragment;
import org.androidcru.crucentralcoast.presentation.views.fragments.PassengerFragment;

public class RideSharingAdapter extends FragmentPagerAdapter
{
    public RideSharingAdapter(FragmentManager fm)
    {
        super(fm);
    }

    @Override
    public CharSequence getPageTitle(int position)
    {
        switch (position)
        {
            case 0:
                return "Driver";
            case 1:
                return "Passenger";
        }
        return super.getPageTitle(position);
    }

    @Override
    public Fragment getItem(int position)
    {
        switch (position)
        {
            case 0:
                return new DriverFragment();
            case 1:
                return new PassengerFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount()
    {
        return 2;
    }
}
