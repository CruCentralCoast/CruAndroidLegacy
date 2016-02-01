package org.androidcru.crucentralcoast.presentation.views.fragments.driversignup;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.androidcru.crucentralcoast.R;
import org.androidcru.crucentralcoast.presentation.util.NonSwipeableViewPager;
import org.androidcru.crucentralcoast.presentation.views.activities.forms.FormHolder;
import org.androidcru.crucentralcoast.presentation.views.activities.forms.FormPage;
import org.androidcru.crucentralcoast.presentation.views.adapters.DriverPagerAdapter;

import butterknife.Bind;
import butterknife.ButterKnife;


public class DriverFragment extends Fragment implements FormPage
{
    @Bind(R.id.viewPager) NonSwipeableViewPager viewPager;

    private FormHolder formHolder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.driver_form, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        viewPager.setAdapter(new DriverPagerAdapter(getChildFragmentManager()));
    }

    @Override
    public void onNext() {
        viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
    }

    @Override
    public void onPrevious() {
        viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
    }

    @Override
    public void setFormHolder(FormHolder holder) {
        this.formHolder = holder;
    }
}
