package org.androidcru.crucentralcoast.presentation.views.ministryteams;

import android.support.v4.app.FragmentManager;

import org.androidcru.crucentralcoast.presentation.views.forms.FormActivity;
import org.androidcru.crucentralcoast.presentation.views.forms.FormAdapter;
import org.androidcru.crucentralcoast.presentation.views.forms.FormContentFragment;

import java.util.ArrayList;

public class MinistryTeamPagerAdapter extends FormAdapter
{
    private ArrayList<FormContentFragment> fragments;

    public MinistryTeamPagerAdapter(FragmentManager fm, FormActivity parent, ArrayList<FormContentFragment> fragments)
    {
        super(fm, parent);
        this.fragments = fragments;
    }

    @Override
    public FormContentFragment getFormPage(int position)
    {
        return fragments.get(position);
    }

    @Override
    public int getCount()
    {
        return fragments.size();
    }
}
