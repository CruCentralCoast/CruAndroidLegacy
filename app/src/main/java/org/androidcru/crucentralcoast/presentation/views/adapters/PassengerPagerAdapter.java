package org.androidcru.crucentralcoast.presentation.views.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;

import org.androidcru.crucentralcoast.presentation.views.fragments.driversignup.DriverBasicInfoFragment;
import org.androidcru.crucentralcoast.presentation.views.fragments.driversignup.DriverRideInfoFragment;
import org.androidcru.crucentralcoast.presentation.views.fragments.driversignup.DriverRideLocFragment;
import org.androidcru.crucentralcoast.presentation.views.fragments.passengersignup.PassengerLocFragment;

import java.lang.ref.WeakReference;

public class PassengerPagerAdapter extends FragmentStatePagerAdapter
{
    SparseArray<WeakReference<Fragment>> registeredFragments = new SparseArray<>();

    public PassengerPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch(position)
        {
            case 0:
                return new PassengerLocFragment();
            default:
                return new PassengerLocFragment();
        }
    }

    @Override
    public int getCount() {
        return 1;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment fragment = (Fragment) super.instantiateItem(container, position);
        registeredFragments.put(position, new WeakReference<>(fragment));
        return fragment;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        registeredFragments.remove(position);
        super.destroyItem(container, position, object);
    }

    public Fragment getRegisteredFragment(int position) {
        return registeredFragments.get(position).get();
    }
}
