package org.androidcru.crucentralcoast.presentation.views.adapters;

import android.support.v4.app.FragmentManager;

import org.androidcru.crucentralcoast.presentation.views.fragments.ProvableFragment;
import org.androidcru.crucentralcoast.presentation.views.fragments.passengersignup.DriverResultsFragment;
import org.androidcru.crucentralcoast.presentation.views.fragments.passengersignup.PassengerBasicInfoFragment;
import org.androidcru.crucentralcoast.presentation.views.fragments.passengersignup.PassengerLocFragment;

import rx.Observable;

public class PassengerPagerAdapter extends BaseFormAdapter
{

    private int formCount;
    private Observable<Void> mOnNextCallback;

    public PassengerPagerAdapter(FragmentManager fm, int formCount, Observable<Void> onNextCallback) {
        super(fm);
        this.formCount = formCount;
        this.mOnNextCallback = onNextCallback;
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
            case 1:
                return new DriverResultsFragment(mOnNextCallback);
            case 2:
                return new PassengerBasicInfoFragment();
            default:
                return new PassengerLocFragment();
        }
    }
}