package org.androidcru.crucentralcoast.presentation.views.ridesharing.passengersignup;

import android.os.Bundle;

import org.androidcru.crucentralcoast.presentation.views.forms.FormActivity;
import org.androidcru.crucentralcoast.presentation.views.forms.FormContentFragment;

import java.util.ArrayList;

public class PassengerSignupActivity extends FormActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ArrayList<FormContentFragment> fragments = new ArrayList<>();

        fragments.add(new PassengerLocFragment());
        fragments.add(new DriverResultsFragment());
        fragments.add(new PassengerBasicInfoFragment());

        setAdapter(new PassengerPagerAdapter(getSupportFragmentManager(), fragments, this));
    }
}
