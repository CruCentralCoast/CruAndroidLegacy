package org.androidcru.crucentralcoast.presentation.views.ridesharing.passengersignup;

import android.support.v4.app.FragmentManager;

import org.androidcru.crucentralcoast.presentation.views.forms.BaseFormAdapter;
import org.androidcru.crucentralcoast.presentation.views.ridesharing.ProvableFragment;

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