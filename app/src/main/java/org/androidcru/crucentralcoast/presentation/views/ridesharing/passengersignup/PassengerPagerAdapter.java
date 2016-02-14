package org.androidcru.crucentralcoast.presentation.views.ridesharing.passengersignup;

import android.support.v4.app.FragmentManager;
import android.view.View;

import org.androidcru.crucentralcoast.presentation.views.forms.FormAdapter;
import org.androidcru.crucentralcoast.presentation.views.forms.FormContentFragment;
import org.androidcru.crucentralcoast.presentation.views.forms.FormHolder;

import java.util.ArrayList;

public class PassengerPagerAdapter extends FormAdapter
{
    private ArrayList<FormContentFragment> fragments;
    private FormHolder formHolder;

    public PassengerPagerAdapter(FragmentManager fm, ArrayList<FormContentFragment> fragments, FormHolder formHolder) {
        super(fm);
        this.fragments = fragments;
        this.formHolder = formHolder;
    }

    @Override
    public int getCount()
    {
        return fragments.size();
    }

    @Override
    public FormContentFragment getFormPage(int position)
    {
        formHolder.clearUI();
        if(position == 0)
            formHolder.setPreviousVisibility(View.GONE);

        return fragments.get(position);
    }
}