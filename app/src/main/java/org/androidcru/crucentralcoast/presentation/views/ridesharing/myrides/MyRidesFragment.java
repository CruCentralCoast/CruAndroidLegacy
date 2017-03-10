package org.androidcru.crucentralcoast.presentation.views.ridesharing.myrides;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.androidcru.crucentralcoast.AppConstants;
import org.androidcru.crucentralcoast.R;
import org.androidcru.crucentralcoast.presentation.views.base.BaseSupportFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * MyRidesFragment launches the MyRides section of the application.
 */
public class MyRidesFragment extends BaseSupportFragment {
    @BindView(R.id.sliding_tabs)
    TabLayout tabLayout;
    @BindView(R.id.viewpager)
    ViewPager viewPager;

    public static MyRidesFragment newInstance() {
        return new MyRidesFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.sliding_tabs_my_rides, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Let ButterKnife find all injected views and bind them to member variables
        unbinder = ButterKnife.bind(this, view);
    }

    public void switchToTab() {
        Bundle bundle = getArguments();
        if (bundle != null)
            tabLayout.getTabAt(bundle.getInt(AppConstants.MY_RIDES_TAB)).select();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (viewPager.getAdapter() == null) {
            viewPager.setAdapter(new MyRidesFragmentPagerAdapter(getChildFragmentManager()));
            tabLayout.setupWithViewPager(viewPager);
            int white = ContextCompat.getColor(getContext(), android.R.color.white);
            tabLayout.setTabTextColors(white, white);
            switchToTab();
        }
    }
}
