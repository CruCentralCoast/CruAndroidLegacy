package org.androidcru.crucentralcoast.presentation.views.ridesharing.passengersignup;

import android.os.Bundle;

import org.androidcru.crucentralcoast.R;
import org.androidcru.crucentralcoast.presentation.views.forms.FormBaseActivity;

public class PassengerSignupActivity extends FormBaseActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportFragmentManager().beginTransaction().replace(R.id.content, new PassengerFormFragment()).commit();
    }
}