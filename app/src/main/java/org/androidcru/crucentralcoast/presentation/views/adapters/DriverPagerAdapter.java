package org.androidcru.crucentralcoast.presentation.views.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;

import org.androidcru.crucentralcoast.presentation.views.fragments.driversignup.DriverBasicInfoFragment;
import org.androidcru.crucentralcoast.presentation.views.fragments.driversignup.DriverRideInfoFragment;
import org.androidcru.crucentralcoast.presentation.views.fragments.driversignup.DriverRideLocFragment;

import java.lang.ref.WeakReference;

public class DriverPagerAdapter extends FragmentStatePagerAdapter
{
    SparseArray<WeakReference<Fragment>> registeredFragments = new SparseArray<>();

    public DriverPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public Fragment getItem(int position) {
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
