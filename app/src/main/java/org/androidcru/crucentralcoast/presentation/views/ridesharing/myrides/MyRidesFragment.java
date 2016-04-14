package org.androidcru.crucentralcoast.presentation.views.ridesharing.myrides;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.androidcru.crucentralcoast.AppConstants;
import org.androidcru.crucentralcoast.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * MyRidesFragment launches the MyRides section of the application.
 */
public class MyRidesFragment extends Fragment
{
    @Bind(R.id.sliding_tabs) TabLayout tabLayout;
    @Bind(R.id.viewpager) ViewPager viewPager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.sliding_tabs_my_rides, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        //Let ButterKnife find all injected views and bind them to member variables
        ButterKnife.bind(this, view);
    }

    public void switchToTab()
    {
        Bundle bundle = getArguments();
        if (bundle != null)
            tabLayout.getTabAt(bundle.getInt(AppConstants.MY_RIDES_TAB)).select();
    }

    @Override
    public void onResume()
    {
        super.onResume();
        if(viewPager.getAdapter() == null)
        {
            viewPager.setAdapter(new MyRidesFragmentPagerAdapter(getChildFragmentManager()));
            tabLayout.setupWithViewPager(viewPager);
            switchToTab();
        }
    }
}
