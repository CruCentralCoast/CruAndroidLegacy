package org.androidcru.crucentralcoast.presentation.views.adapters;

import android.support.v4.app.FragmentManager;

import org.androidcru.crucentralcoast.presentation.views.fragments.ProvableFragment;
import org.androidcru.crucentralcoast.presentation.views.fragments.passengersignup.PassengerLocFragment;

public class PassengerPagerAdapter extends BaseFormAdapter
{

    private int formCount;

    public PassengerPagerAdapter(FragmentManager fm, int formCount) {
        super(fm);
        this.formCount = formCount;
    }

    @Override
    public int getCount()
    {
        return formCount;
    }

    @Override
    public ProvableFragment getFormPage(int position)
    {
        switch(position)
        {
            case 0:
                return new PassengerLocFragment();
            default:
                return new PassengerLocFragment();
        }
    }
}
