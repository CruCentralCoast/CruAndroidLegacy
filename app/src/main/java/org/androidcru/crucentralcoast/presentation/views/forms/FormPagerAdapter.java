package org.androidcru.crucentralcoast.presentation.views.forms;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

public class FormPagerAdapter extends FragmentStatePagerAdapter
{
    private List<FormContentFragment> fragments;

    public FormPagerAdapter(FragmentManager fm, List<FormContentFragment> fragments)
    {
        super(fm);
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int position)
    {
        return fragments.get(position);
    }

    @Override
    public int getCount()
    {
        return fragments.size();
    }
}
