package org.androidcru.crucentralcoast.presentation.views.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.androidcru.crucentralcoast.R;
import org.androidcru.crucentralcoast.presentation.views.adapters.RideSharingAdapter;

import butterknife.Bind;
import butterknife.ButterKnife;

public class RideSharingFragment extends Fragment
{
    @Bind(R.id.tab_layout) TabLayout mTabLayout;
    @Bind(R.id.pager) ViewPager mViewPager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_ridesharing, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        /*mTabLayout.newTab().setText("Driver");
        mTabLayout.newTab().setText("Passenger");*/
        RideSharingAdapter rideSharingAdapter = new RideSharingAdapter(getChildFragmentManager());
        mViewPager.setAdapter(rideSharingAdapter);
        mTabLayout.setTabsFromPagerAdapter(rideSharingAdapter);
    }
}
