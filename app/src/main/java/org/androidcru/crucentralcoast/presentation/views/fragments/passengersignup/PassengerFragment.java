package org.androidcru.crucentralcoast.presentation.views.fragments.passengersignup;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.androidcru.crucentralcoast.R;
import org.androidcru.crucentralcoast.presentation.util.NonSwipeableViewPager;
import org.androidcru.crucentralcoast.presentation.views.activities.forms.FormContentFragment;
import org.androidcru.crucentralcoast.presentation.views.adapters.PassengerPagerAdapter;

import butterknife.Bind;
import butterknife.ButterKnife;

public class PassengerFragment extends FormContentFragment
{
    @Bind(R.id.viewPager) NonSwipeableViewPager viewPager;
    private PassengerPagerAdapter passengerPagerAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.form, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        passengerPagerAdapter = new PassengerPagerAdapter(getChildFragmentManager(), 1);
        viewPager.setAdapter(passengerPagerAdapter);


        formHolder.setToolbarExpansion(true);
        formHolder.setTitle("Basic info");
        formHolder.setPreviousVisibility(View.GONE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        passengerPagerAdapter.getRegisteredFragment(viewPager.getCurrentItem()).onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onNext() {
        boolean isValid = passengerPagerAdapter.getRegisteredFragment(viewPager.getCurrentItem()).validate();
        if(isValid)
        {
            switch(viewPager.getCurrentItem() + 1)
            {
                case 1:
                    formHolder.setToolbarExpansion(false);
                    formHolder.setTitle("Location info");
                    formHolder.setPreviousVisibility(View.VISIBLE);
                    break;
                case 2:
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
                formHolder.setTitle("Ride info");
                formHolder.setPreviousVisibility(View.VISIBLE);
                break;
        }
        viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
    }
}

