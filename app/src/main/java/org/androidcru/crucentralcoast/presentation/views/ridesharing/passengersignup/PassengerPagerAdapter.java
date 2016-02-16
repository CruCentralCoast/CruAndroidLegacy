package org.androidcru.crucentralcoast.presentation.views.ridesharing.passengersignup;

import android.support.v4.app.FragmentManager;

import org.androidcru.crucentralcoast.presentation.views.forms.FormActivity;
import org.androidcru.crucentralcoast.presentation.views.forms.FormAdapter;
import org.androidcru.crucentralcoast.presentation.views.forms.FormContentFragment;

import java.util.ArrayList;

public class PassengerPagerAdapter extends FormAdapter
{
    private ArrayList<FormContentFragment> fragments;

    public PassengerPagerAdapter(FragmentManager fm, FormActivity parent, ArrayList<FormContentFragment> fragments) {
        super(fm, parent);
        this.fragments = fragments;
    }

    @Override
    public int getCount()
    {
        return fragments.size();
    }

    @Override
    public FormContentFragment getFormPage(int position)
    {
        return fragments.get(position);
    }
}