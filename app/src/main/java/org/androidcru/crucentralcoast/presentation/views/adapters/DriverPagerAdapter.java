package org.androidcru.crucentralcoast.presentation.views.adapters;

import android.support.v4.app.FragmentManager;

import org.androidcru.crucentralcoast.presentation.views.fragments.ProvableFragment;
import org.androidcru.crucentralcoast.presentation.views.fragments.driversignup.DriverBasicInfoFragment;
import org.androidcru.crucentralcoast.presentation.views.fragments.driversignup.DriverRideInfoFragment;
import org.androidcru.crucentralcoast.presentation.views.fragments.driversignup.DriverRideLocFragment;

public class DriverPagerAdapter extends BaseFormAdapter
{
    private int formCount;

    public DriverPagerAdapter(FragmentManager fm, int formCount) {
        super(fm);
        this.formCount = formCount;
    }

    @Override
    public int getCount() {
        return formCount;
    }

    @Override
    public ProvableFragment getFormPage(int position)
    {
        switch(position)
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
}
