package org.androidcru.crucentralcoast.presentation.views.forms;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

public abstract class FormAdapter extends FragmentStatePagerAdapter
{
    private FormActivity parent;
    public FormAdapter(FragmentManager fm, FormActivity parent)
    {
        super(fm);
        this.parent = parent;
    }

    public abstract FormContentFragment getFormPage(int position);

    @Override
    public final Fragment getItem(int position)
    {
        return getFormPage(position);
    }

    @Override
    public void finishUpdate(ViewGroup container)
    {
        super.finishUpdate(container);
        if (((ViewPager) container).getCurrentItem() == 0)
        {
            getFormPage(0).formHolder.clearUI();
            getFormPage(0).formHolder.setPreviousVisibility(View.GONE);
            getFormPage(0).setupUI();
        }
        parent.setCurrentFormContent(getFormPage(((ViewPager) container).getCurrentItem()));
    }
}
