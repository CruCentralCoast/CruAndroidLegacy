package com.crucentralcoast.app.presentation.views.prayers;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.crucentralcoast.app.R;
import com.crucentralcoast.app.presentation.views.base.BaseSupportFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by brittanyberlanga on 5/6/17.
 */

public class PrayerRequestsFragment extends BaseSupportFragment {

    @BindView(R.id.sliding_tabs)
    TabLayout tabLayout;
    @BindView(R.id.viewpager)
    ViewPager viewPager;

    private Unbinder unbinder;

    public static PrayerRequestsFragment newInstance() {
        return new PrayerRequestsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_prayer_requests, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);

        viewPager.setAdapter(new PrayerRequestsPagerAdapter(getChildFragmentManager()));
        tabLayout.setupWithViewPager(viewPager);
        int white = ContextCompat.getColor(getContext(), android.R.color.white);
        tabLayout.setTabTextColors(white, white);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.add_prayer_request_fab)
    public void onClickAddPrayerRequest() {
        Intent submitPrayerIntent = new Intent(getActivity(), SubmitPrayerRequestActivity.class);
        startActivity(submitPrayerIntent);
    }
}
