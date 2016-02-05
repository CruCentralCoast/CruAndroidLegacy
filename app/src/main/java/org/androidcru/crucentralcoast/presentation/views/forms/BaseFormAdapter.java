package org.androidcru.crucentralcoast.presentation.views.forms;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;

import org.androidcru.crucentralcoast.presentation.views.ridesharing.ProvableFragment;

import java.lang.ref.WeakReference;

public abstract class BaseFormAdapter extends FragmentStatePagerAdapter
{
    SparseArray<WeakReference<Fragment>> registeredFragments = new SparseArray<>();

    public BaseFormAdapter(FragmentManager fm)
    {
        super(fm);
    }

    public abstract ProvableFragment getFormPage(int position);

    @Override
    public final Fragment getItem(int position)
    {
        return getFormPage(position);
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

    public ProvableFragment getRegisteredFragment(int position) {
        return (ProvableFragment) registeredFragments.get(position).get();
    }
}
