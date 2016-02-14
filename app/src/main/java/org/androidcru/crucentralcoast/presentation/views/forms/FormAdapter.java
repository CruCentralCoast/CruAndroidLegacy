package org.androidcru.crucentralcoast.presentation.views.forms;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;

import java.lang.ref.WeakReference;

public abstract class FormAdapter extends FragmentStatePagerAdapter
{
    SparseArray<WeakReference<Fragment>> registeredFragments = new SparseArray<>();

    public FormAdapter(FragmentManager fm)
    {
        super(fm);
    }

    public abstract FormContentFragment getFormPage(int position);

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

    public Fragment getRegisteredFragment(int position) {
        return registeredFragments.get(position).get();
    }
}
