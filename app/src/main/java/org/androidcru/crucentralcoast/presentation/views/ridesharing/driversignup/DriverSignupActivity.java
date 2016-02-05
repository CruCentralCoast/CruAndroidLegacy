package org.androidcru.crucentralcoast.presentation.views.ridesharing.driversignup;

import android.os.Bundle;

import org.androidcru.crucentralcoast.R;
import org.androidcru.crucentralcoast.presentation.views.forms.FormBaseActivity;

public class DriverSignupActivity extends FormBaseActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportFragmentManager().beginTransaction().replace(R.id.content, new DriverFormFragment()).commit();
    }
}
