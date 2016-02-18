package org.androidcru.crucentralcoast.presentation.views.ridesharing.passengersignup;

import android.os.Bundle;

import com.orhanobut.logger.Logger;

import org.androidcru.crucentralcoast.CruApplication;
import org.androidcru.crucentralcoast.presentation.views.forms.FormActivity;
import org.androidcru.crucentralcoast.presentation.views.forms.FormContentFragment;

import java.util.ArrayList;

public class PassengerSignupActivity extends FormActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getIntent().getExtras();
        if(bundle == null || bundle.getString(CruApplication.EVENT_ID, "").isEmpty())
        {
            Logger.e("PassengerSignupActivity requires that you pass an event ID.");
            Logger.e("Finishing activity...");
            finish();
            return;
        }

        ArrayList<FormContentFragment> fragments = new ArrayList<>();

        fragments.add(new RideInfoFragment());

        DriverResultsFragment resultsFragment = new DriverResultsFragment();
        resultsFragment.setArguments(bundle);
        fragments.add(resultsFragment);

        fragments.add(new BasicInfoFragment());

        setAdapter(new PassengerPagerAdapter(getSupportFragmentManager(), this, fragments));
    }
}
