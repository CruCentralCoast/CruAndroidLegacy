package org.androidcru.crucentralcoast.presentation.views.forms;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.ViewGroup;

public abstract class FormAdapter extends FragmentStatePagerAdapter
{
    private FormActivity parent;
    private int lastPosition = -1;
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
        int currentPage = ((ViewPager) container).getCurrentItem();
        if (currentPage != lastPosition)
        {
            parent.setCurrentFormContent(getFormPage(currentPage));
        }
        lastPosition = currentPage;
    }
}
