package org.androidcru.crucentralcoast.presentation.views.ridesharing.passengersignup;

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
import rx.Observable;
import rx.Subscriber;

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

        Observable<Void> onNextCallback = Observable.create(new Observable.OnSubscribe<Void>()
        {
            @Override
            public void call(Subscriber<? super Void> subscriber)
            {
                onNext();
                subscriber.onCompleted();
            }
        });

        passengerPagerAdapter = new PassengerPagerAdapter(getChildFragmentManager(), 3, onNextCallback);
        viewPager.setAdapter(passengerPagerAdapter);


        formHolder.setToolbarExpansion(false);
        formHolder.setTitle("Location Information");
        formHolder.setPreviousVisibility(View.VISIBLE);
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
                    formHolder.setTitle("Select A Driver");
                    formHolder.setPreviousVisibility(View.VISIBLE);
                    formHolder.setNextVisibility(View.GONE);
                    break;
                case 2:
                    formHolder.setToolbarExpansion(false);
                    formHolder.setTitle("Basic Information");
                    formHolder.setPreviousVisibility(View.VISIBLE);
                    formHolder.setNextVisibility(View.VISIBLE);
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
                formHolder.setToolbarExpansion(false);
                formHolder.setTitle("Location Information");
                formHolder.setPreviousVisibility(View.GONE);
                break;
            case 1:
                formHolder.setToolbarExpansion(false);
                formHolder.setTitle("Select A Driver");
                formHolder.setPreviousVisibility(View.VISIBLE);
                formHolder.setNextVisibility(View.GONE);
                break;

        }
        viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
    }
}