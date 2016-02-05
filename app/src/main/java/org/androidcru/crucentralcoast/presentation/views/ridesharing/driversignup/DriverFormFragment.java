package org.androidcru.crucentralcoast.presentation.views.ridesharing.driversignup;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.androidcru.crucentralcoast.R;
import org.androidcru.crucentralcoast.presentation.util.NonSwipeableViewPager;
import org.androidcru.crucentralcoast.presentation.views.forms.FormContentFragment;

import butterknife.Bind;
import butterknife.ButterKnife;


public class DriverFormFragment extends FormContentFragment
{
    @Bind(R.id.viewPager) NonSwipeableViewPager viewPager;

    private DriverPagerAdapter driverPagerAdapter;

    public DriverFormFragment()
    {
        super();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.form, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        driverPagerAdapter = new DriverPagerAdapter(getChildFragmentManager(), 3);
        viewPager.setAdapter(driverPagerAdapter);

        formHolder.setToolbarExpansion(true);
        formHolder.setTitle("Basic info");
        formHolder.setPreviousVisibility(View.GONE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        driverPagerAdapter.getRegisteredFragment(viewPager.getCurrentItem()).onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onNext() {
        boolean isValid = driverPagerAdapter.getRegisteredFragment(viewPager.getCurrentItem()).validate();
        if(isValid)
        {
            switch(viewPager.getCurrentItem() + 1)
            {
                case 1:
                    formHolder.setToolbarExpansion(true);
                    formHolder.setTitle("Ride info");
                    formHolder.setPreviousVisibility(View.VISIBLE);
                    break;
                case 2:
                    formHolder.setToolbarExpansion(false);
                    formHolder.setTitle("Location info");
                    formHolder.setPreviousVisibility(View.VISIBLE);
                    break;
                case 3:
                    formHolder.complete();
                    break;
            }
            viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
        }
    }

    @Override
    public void onPrevious() {
        switch(viewPager.getCurrentItem() - 1)
        {
            case 0:
                formHolder.setToolbarExpansion(true);
                formHolder.setTitle("Basic info");
                formHolder.setPreviousVisibility(View.GONE);
                break;
            case 1:
                formHolder.setToolbarExpansion(true);
                formHolder.setTitle("Ride info");
                formHolder.setPreviousVisibility(View.VISIBLE);
                break;

        }
        viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
    }
}
